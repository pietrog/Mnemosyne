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
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoDBHelper;


/**
 * Created by pietro on 18/05/15.
 *
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MemoryManagerSQLManagerTest {

    private MemoryManagerSQLManager singleton ;
    private long midCatalogue, midDictionary, midWord1, midDictObj1, midWord2, midDictObj2, midWord3, midDictObj3;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        singleton = MemoryManagerSQLManager.getInstance(context);
        CatalogueSQLManager.getInstance(context);
        DictionarySQLManager.getInstance(context);
        DictionaryObject.initMemoryPhaseMap();

        //create the catalogue
        midCatalogue = CatalogueSQLManager.getInstance().add("CatalogueTest", "DescTest");
        //create the dictionary
        midDictionary = DictionarySQLManager.getInstance().addDictionaryInCatalogue(midCatalogue, "DictTest", "DictDescTest");

        DictionaryObject.MemoryMonitoringObject one = singleton.createNewMemoryMonitoringObject();
        DictionaryObject.MemoryMonitoringObject two = singleton.createNewMemoryMonitoringObject();
        midDictObj1 = singleton.createDictionaryObject(midDictionary, one.getID());
        midDictObj2 = singleton.createDictionaryObject(midDictionary, two.getID());
        midDictObj3 = singleton.createDictionaryObject(midDictionary, two.getID());
        midWord1 = DictionarySQLManager.getInstance().addNewWord(midDictObj1, "wordTest", "defTest");
        midWord2 = DictionarySQLManager.getInstance().addNewWord(midDictObj2, "wordTest2", "defTest2");
        midWord3 = DictionarySQLManager.getInstance().addNewWord(midDictObj3, "wordTest2", "defTest2");


    }

    @After
    public void tearDown() throws Exception {
        //TODO test ressource closing
        MnemoDBHelper.release();
    }

    @Test
    public void testInitMemoryPhaseMap() throws Exception {
        Map<Long, DictionaryObject.MemoryPhaseObject> phaseMap = new HashMap<>();
        singleton.initMemoryPhaseMap(phaseMap);
        Assert.assertTrue("Should contain 3 phases", phaseMap.size() == 3);

        long idPhase2 = -999, idPhase3 = -999;

        for(Long key: phaseMap.keySet()){
            DictionaryObject.MemoryPhaseObject obj = phaseMap.get(key);
            if (obj.mPhaseName.compareTo(Global.FIRST_PHASE_NAME) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 5);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 1);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 0);
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

        //add the word
        DictionaryObject.initMemoryPhaseMap();
        DictionaryObject.MemoryMonitoringObject newOne = singleton.createNewMemoryMonitoringObject();
        long idObj = singleton.createDictionaryObject(midDictionary, newOne.getID());
        Assert.assertTrue("Word id should be greater than 0", idObj > 0);

        //failure on bad ids
        Assert.assertEquals(Global.FAILURE, singleton.createDictionaryObject(-1, 2));
        Assert.assertEquals(Global.FAILURE, singleton.createDictionaryObject(3, -1));

        idObj = singleton.createDictionaryObject(midDictionary, 999);
        Assert.assertTrue("Should return FAILURE if memory phase object does not exist", idObj == Global.FAILURE);

        idObj = singleton.createDictionaryObject(999, newOne.getID());
        Assert.assertTrue("Should return FAILURE if dictionary id does not exist", idObj == Global.FAILURE);
    }

    @Test
    public void testAddWordToLearnSession() throws Exception {

        //create the catalogue
        Date dnow = GeneralTools.getNowDate();

        long idMemoryManagerObj = singleton.addWordToLearnSession(999, dnow);
        Assert.assertTrue("ID should be smaller than 0", idMemoryManagerObj == Global.FAILURE);

        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj1, dnow);
        Assert.assertTrue("ID should be greater than 0", idMemoryManagerObj > 0);
        //add a learning session
        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj2, dnow);
        Assert.assertTrue("ID should be greater than 0", idMemoryManagerObj > 0);
        //add another learning session for the same word should fail
        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj2, dnow);
        Assert.assertEquals("Should not be able to add the same word twice for the same date", Global.FAILURE, idMemoryManagerObj);
        //add another learning session for another word
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 3);
        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj3, cal.getTime());
        Assert.assertTrue("Add a word session for a future date should success", idMemoryManagerObj > 0);

    }

    @Test
    public void testGetListOfObjectsToLearn() throws Exception {
        Vector<Global.Couple<Long, Integer>> list;

        Date dnow = GeneralTools.getNowDate();
        list = singleton.getListOfObjectsToLearn(dnow);
        Assert.assertTrue("List should be empty", list.size() == 0);

        //add the word to learn session
        singleton.addWordToLearnSession(midDictObj2, dnow);
        list = singleton.getListOfObjectsToLearn(dnow);
        Assert.assertEquals("List should contain one id", 1, list.size());
        Assert.assertTrue("Id in the list should be equal to the id of the original word", list.get(0).val1 == midDictObj2);

        //add another word to learn session
        singleton.addWordToLearnSession(midWord3, dnow);
        list = singleton.getListOfObjectsToLearn(dnow);
        Assert.assertEquals("List should contain two ids", 2, list.size());

        //add for date in one week
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        singleton.addWordToLearnSession(midDictObj1, cal.getTime());
        singleton.addWordToLearnSession(midDictObj2, cal.getTime());
        singleton.addWordToLearnSession(midDictObj3, cal.getTime());
        list = singleton.getListOfObjectsToLearn(cal.getTime());
        Assert.assertEquals("List should contain 3 elements", 3, list.size());
        for (Global.Couple<Long, Integer> c : list){
            Assert.assertTrue("Should contain these ids", c.val1 == midDictObj1 || c.val1 == midDictObj2 || c.val1 == midDictObj3);
        }

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