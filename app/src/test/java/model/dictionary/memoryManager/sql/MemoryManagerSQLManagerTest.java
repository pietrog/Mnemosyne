package model.dictionary.memoryManager.sql;

import android.content.Context;
import android.database.Cursor;

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
import java.util.HashMap;
import java.util.Map;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoCalendar;
import model.dictionary.tools.MnemoDBHelper;


/**
 * Created by pietro on 18/05/15.
 *
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MemoryManagerSQLManagerTest {

    private MemoryManagerSQLManager singleton ;
    private long midCatalogue, midDictionary, midDictObj1, midDictObj2, midDictObj3;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        singleton = MemoryManagerSQLManager.getInstance(context);
        CatalogueSQLManager.getInstance(context);
        DictionarySQLManager.getInstance(context);
        MemoryManager.initMemoryPhaseMap();

        //create the catalogue
        midCatalogue = CatalogueSQLManager.getInstance().add("CatalogueTest", "DescTest");
        //create the dictionary
        midDictionary = DictionarySQLManager.getInstance().addDictionaryInCatalogue(midCatalogue, "DictTest", "DictDescTest");

        long memObjID1 = singleton.createNewMemoryMonitoringObject();
        long memObjID2 = singleton.createNewMemoryMonitoringObject();
        long memObjID3 = singleton.createNewMemoryMonitoringObject();
        midDictObj1 = DictionarySQLManager.getInstance().createNewDictionaryObject(midDictionary, memObjID1);
        midDictObj2 = DictionarySQLManager.getInstance().createNewDictionaryObject(midDictionary, memObjID2);
        midDictObj3 = DictionarySQLManager.getInstance().createNewDictionaryObject(midDictionary, memObjID3);
    }

    @After
    public void tearDown() throws Exception {
        MnemoDBHelper.release();
    }

    @Test
    public void testInitMemoryPhaseMap() throws Exception {
        Map<Long, MemoryManager.MemoryPhaseObject> phaseMap = new HashMap<>();
        singleton.initMemoryPhaseMap(phaseMap);
        Assert.assertTrue("Should contain 3 phases", phaseMap.size() == 3);

        long idPhase2 = -999, idPhase3 = -999;

        for(Long key: phaseMap.keySet()){
            MemoryManager.MemoryPhaseObject obj = phaseMap.get(key);
            if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P1) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 5);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 1);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 0);
            }
            else if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P2) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 95);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 5);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 1);
                idPhase2 = key;
            }
            else if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P3) == 0){
                Assert.assertTrue("Duration phase should be equal to", obj.mDurationPhase == 0);
                Assert.assertTrue("Duration phase should be equal to", obj.mFirstPeriod == 62);
                Assert.assertTrue("Duration phase should be equal to", obj.mPeriodIncrement == 0);
                idPhase3 = key;
            }
        }

        for(MemoryManager.MemoryPhaseObject obj: phaseMap.values()){
            if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P1) == 0)
                Assert.assertEquals("Phase 1 should point on phase 2", obj.mNextPhaseID, idPhase2);
            else if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P2) == 0)
                Assert.assertEquals("Phase 2 should point on phase 3", obj.mNextPhaseID, idPhase3);
            else if (obj.mPhaseName.compareTo(Global.PHASE_NAME_P3) == 0)
                Assert.assertEquals("Phase 3 should point -1", obj.mNextPhaseID, -1);
        }

    }

    @Test
    public void testCreateNewMemoryMonitoringObject() throws Exception {
        long memObjID1 = singleton.createNewMemoryMonitoringObject();
        String sql = "SELECT * FROM " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME + ", " + MemoryManagerContract.MemoryPhase.TABLE_NAME
                + " WHERE " + MemoryManagerContract.MemoryMonitoring.CID + " = " + memObjID1
                + " AND " + MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID + " = " + MemoryManagerContract.MemoryPhase.CID;

        Cursor cursor = MemoryManagerSQLManager.getInstance().rawQuery(sql, null);

        Assert.assertTrue("Move cursor to first row", cursor.moveToFirst());
        //test object points really on the first phase
        Assert.assertTrue("New object should point on first phase", GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryPhase.PHASE_NAME).compareTo(Global.PHASE_NAME_P1) == 0);

        //test object itself
        String nowFormated = GeneralTools.getSQLDate(GeneralTools.getNowDate());
        Date nowDateFormat = GeneralTools.getDateFromSQLDate(nowFormated);

        long nextLearn = GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN);
        Date lastlearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT));
        Date dateadded = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.DATE_ADDED));
        Date begMP = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP));
        int firstPeriod = (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.FIRST_PERIOD);
        //test the dates
        Assert.assertTrue("Last learnt should be today", GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT).compareTo(nowFormated) == 0);
        Assert.assertTrue("Next learning date should be after last learnt", nextLearn > lastlearnt.getTime());
        Assert.assertEquals("Added date should be equal to last learn date", lastlearnt, dateadded);
        Assert.assertEquals("Beginning date should be equal to last learn date", begMP, dateadded);
        Assert.assertTrue("Added, beginning and last learn date should be equal to today's date", nowDateFormat.compareTo(dateadded) == 0 && nowDateFormat.compareTo(lastlearnt) == 0 && nowDateFormat.compareTo(begMP) == 0);
        Assert.assertEquals("Days between should be equal to the first period of the first memory phase", firstPeriod, GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN));

        //test the first period
        Calendar cal = MnemoCalendar.getInstance();
        cal.setTimeInMillis(nextLearn);
        cal.add(Calendar.DAY_OF_YEAR, -1 * firstPeriod);
        Date formattedNext = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(cal.getTime()));
        Assert.assertEquals("Next learning date should be 1 day after last learnt", lastlearnt, formattedNext);
    }

    /**
     * method updateMemoryObjectInDB
     */
    @Test
    public void testUpdateMemoryObjectInDBWhenObjectIsNull() throws Exception {
        Assert.assertEquals("Should return bad parameter", Global.BAD_PARAMETER, singleton.updateMemoryObjectInDB(null));
    }
    @Test
    public void testUpdateMemoryObjectInDBWhenChangeToNextPhase() throws Exception {
    }

}