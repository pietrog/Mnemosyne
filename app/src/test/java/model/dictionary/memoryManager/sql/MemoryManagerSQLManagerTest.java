package model.dictionary.memoryManager.sql;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.GeneralTools;


/**
 * Created by pietro on 18/05/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MemoryManagerSQLManagerTest {

    private MemoryManagerSQLManager singleton ;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        singleton = MemoryManagerSQLManager.getInstance(context);
        CatalogueSQLManager.getInstance(context);
        DictionarySQLManager.getInstance(context);
    }

    @After
    public void tearDown() throws Exception {
        //TODO test ressource closing
    }

    @Test
    public void testInitMemoryPhaseMap() throws Exception {
        Map<Long, DictionaryObject.MemoryPhaseObject> phaseMap = new HashMap<>();
        singleton.initMemoryPhaseMap(phaseMap);
        Assert.assertTrue("Should contain 3 phases", phaseMap.size() == 3);

        long idPhase1 = -999, idPhase2 = -999, idPhase3 = -999;

        for(Long key: phaseMap.keySet()){
            DictionaryObject.MemoryPhaseObject obj = phaseMap.get(key);
            if (obj.mPhaseName.compareTo(Global.FIRST_PHASE_NAME) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 5);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 1);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 0);
                idPhase1 = key;
            }
            else if (obj.mPhaseName.compareTo(Global.SECOND_PHASE_NAME) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 95);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 5);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 1);
                idPhase2 = key;
            }
            else if (obj.mPhaseName.compareTo(Global.THIRD_PHASE_NAME) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 0);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 62);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 0);
                idPhase3 = key;
            }
        }

        for(DictionaryObject.MemoryPhaseObject obj: phaseMap.values()){
            if (obj.mPhaseName.compareTo(Global.FIRST_PHASE_NAME) == 0)
                Assert.assertEquals("Phase 1 should point on phase 2", obj.mNextPhaseID, idPhase2);
            else if (obj.mPhaseName.compareTo(Global.SECOND_PHASE_NAME) == 0)
                Assert.assertEquals("Phase 2 should point on phase 3", obj.mNextPhaseID, idPhase3);
            else if (obj.mPhaseName.compareTo(Global.THIRD_PHASE_NAME) == 0)
                Assert.assertEquals("Phase 3 should point -1", obj.mNextPhaseID, -1);
        }

    }

    @Test
    public void testCreateNewMemoryMonitoringObject() throws Exception {
        DictionaryObject.initMemoryPhaseMap();
        DictionaryObject.MemoryMonitoringObject newOne = singleton.createNewMemoryMonitoringObject();
        DictionaryObject.MemoryPhaseObject firstPhase = DictionaryObject.mMemoryPhaseObjectMap.get(newOne.getMemoryPhaseID());

        //test object points really on the first phase
        Assert.assertTrue("New object should point on first phase", newOne.getMemoryPhaseName().compareTo(Global.FIRST_PHASE_NAME) == 0);

        //test object itself
        System.out.println("LastLearnt Date: " + newOne.getLastLearnt());
        String nowFormated = GeneralTools.getSQLDate(GeneralTools.getNowDate());
        System.out.println("Now Date: " + nowFormated);
        System.out.println("Next Date: " + newOne.getNextLearnt());

        //test the dates
        Assert.assertTrue("Last learnt should be today", GeneralTools.getSQLDate(newOne.getLastLearnt()).compareTo(nowFormated) == 0);
        Assert.assertTrue("Next learning date should be after last learnt", newOne.getNextLearnt().after(newOne.getLastLearnt()));
        Assert.assertEquals("Added date should be equal to last learn date", newOne.getLastLearnt(), newOne.getDateAdded());
        Assert.assertEquals("Beginning date should be equal to last learn date", newOne.getBeginningOfMP(), newOne.getDateAdded());
        Assert.assertEquals("Days between should be equal to the first period of the first memory phase", firstPhase.mFirstPeriod, newOne.getDaysBetween());

        //test the first period
        Calendar cal = new GregorianCalendar();
        cal.setTime(newOne.getNextLearnt());
        cal.add(Calendar.DAY_OF_YEAR, -1 * firstPhase.mFirstPeriod);
        Date formattedNext = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(cal.getTime()));
        Assert.assertEquals("Next learning date should be 1 day after last learnt", newOne.getLastLearnt(), formattedNext);
    }

    @Test
    public void testCreateDictionaryObject() throws Exception {
        //create the catalogue
        long idCatalogue = CatalogueSQLManager.getInstance().add("CatalogueTest", "DescTest");
        //create the dictionary
        long idDictionary = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatalogue, "DictTest", "DictDescTest");
        //add the word
        DictionaryObject.initMemoryPhaseMap();
        DictionaryObject.MemoryMonitoringObject newOne = singleton.createNewMemoryMonitoringObject();
        long idWord = singleton.createDictionaryObject(idDictionary, newOne.getID());
        Assert.assertTrue("Word id should be greater than 0", idWord > 0);

        //failure on bad ids
        Assert.assertEquals(Global.FAILURE, singleton.createDictionaryObject(-1, 2));
        Assert.assertEquals(Global.FAILURE, singleton.createDictionaryObject(3, -1));

        idWord = singleton.createDictionaryObject(idDictionary, 999);
        Assert.assertTrue("Should return FAILURE if memory phase object does not exist", idWord == Global.FAILURE);

        idWord = singleton.createDictionaryObject(999, newOne.getID());
        Assert.assertTrue("Should return FAILURE if idDictionary does not exist", idWord == Global.FAILURE);
    }

    @Test
    public void testAddWordToLearnSession() throws Exception {

        //create the catalogue
        /*DictionaryObject.initMemoryPhaseMap();
        long idCatalogue = CatalogueSQLManager.getInstance().add("CatalogueTest2", "DescTest2");
        long idDictionary = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatalogue, "DictTest2", "DictDescTest2");
        DictionaryObject.MemoryMonitoringObject newOne = singleton.createNewMemoryMonitoringObject();
        long idWord = singleton.createDictionaryObject(idDictionary, newOne.getID());
        Date dnow = GeneralTools.getNowDate();

        long idMemoryManagerObj = singleton.addWordToLearnSession(999, dnow);
        Assert.assertTrue("ID should be smaller than 0", idMemoryManagerObj == Global.FAILURE);
        idMemoryManagerObj = singleton.addWordToLearnSession(idWord, dnow);
        Assert.assertTrue("ID should be greater than 0", idMemoryManagerObj > 0);*/

    }

    @Test
    public void testGetListOfObjectsToLearn() throws Exception {

    }

    @Test
    public void testGetListOfObjectsToLearn1() throws Exception {

    }

    @Test
    public void testUpdateDictionaryObjectInDB() throws Exception {

    }

    @Test
    public void testGetListOfPastListToLearnFromDate() throws Exception {

    }

    @Test
    public void testDelayLearningSessionFromDate() throws Exception {

    }
}