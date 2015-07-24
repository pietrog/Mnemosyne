package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionaryObject.DictionaryObject;
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

        for(MemoryManager.MemoryPhaseObject obj: phaseMap.values()){
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
        long memObjID1 = singleton.createNewMemoryMonitoringObject();
        String sql = "SELECT * FROM " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME + ", " + MemoryManagerContract.MemoryPhase.TABLE_NAME
                + " WHERE " + MemoryManagerContract.MemoryMonitoring.CID + " = " + memObjID1
                + " AND " + MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID + " = " + MemoryManagerContract.MemoryPhase.CID;

        Cursor cursor = MemoryManagerSQLManager.getInstance().rawQuery(sql, null);

        Assert.assertTrue("Move cursor to first row", cursor.moveToFirst());
        //test object points really on the first phase
        Assert.assertTrue("New object should point on first phase", GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryPhase.PHASE_NAME).compareTo(Global.FIRST_PHASE_NAME) == 0);

        //test object itself
        String nowFormated = GeneralTools.getSQLDate(GeneralTools.getNowDate());
        Date nowDateFormat = GeneralTools.getDateFromSQLDate(nowFormated);

        Date nextLearn = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT));
        Date lastlearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT));
        Date dateadded = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.DATE_ADDED));
        Date begMP = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP));
        int firstPeriod = (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.FIRST_PERIOD);
        //test the dates
        Assert.assertTrue("Last learnt should be today", GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT).compareTo(nowFormated) == 0);
        Assert.assertTrue("Next learning date should be after last learnt", nextLearn.after(lastlearnt));
        Assert.assertEquals("Added date should be equal to last learn date", lastlearnt, dateadded);
        Assert.assertEquals("Beginning date should be equal to last learn date", begMP, dateadded);
        Assert.assertTrue("Added, beginning and last learn date should be equal to today's date", nowDateFormat.compareTo(dateadded) == 0 && nowDateFormat.compareTo(lastlearnt) == 0 && nowDateFormat.compareTo(begMP) == 0);
        Assert.assertEquals("Days between should be equal to the first period of the first memory phase", firstPeriod, GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN));

        //test the first period
        Calendar cal = new GregorianCalendar();
        cal.setTime(nextLearn);
        cal.add(Calendar.DAY_OF_YEAR, -1 * firstPeriod);
        Date formattedNext = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(cal.getTime()));
        Assert.assertEquals("Next learning date should be 1 day after last learnt", lastlearnt, formattedNext);
    }

    /**
     * method AddWordToLearnSession
     */
    @Test
    public void testAddWordToLearnSession() throws Exception {

        //create the catalogue
        Date dnow = GeneralTools.getNowDate();

        long idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj1, dnow);
        Assert.assertTrue("ID should be greater than 0", idMemoryManagerObj > 0);
        //add a learning session
        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj2, dnow);
        Assert.assertTrue("ID should be greater than 0", idMemoryManagerObj > 0);
        //add another learning session for another word
        Calendar cal = MnemoCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 3);
        idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj3, cal.getTime());
        Assert.assertTrue("Add a word session for a future date should success", idMemoryManagerObj > 0);
    }

    @Test
    public void testAddWordToLearnSessionTwiceForTheSameWord() throws Exception {
        //TODO implement this part
        /*//create the catalogue
        Date dnow = MnemoCalendar.getInstance().getTime();
        //add another learning session for the same word should fail
        singleton.addWordToLearnSession(midDictObj2, dnow);
        long idMemoryManagerObj = singleton.addWordToLearnSession(midDictObj2, dnow);
        Assert.assertEquals("Should not be able to add the same word twice for the same date", Global.FAILURE, idMemoryManagerObj);*/
    }
    @Test
    public void testAddWordToLearnSessionWhenWordIDIsWrong() throws Exception {
        Date dnow = MnemoCalendar.getInstance().getTime();
        long idMemoryManagerObj = singleton.addWordToLearnSession(999, dnow);
        Assert.assertTrue("ID should be smaller than 0", idMemoryManagerObj == Global.FAILURE);
    }


    /**
     * method GetListOfObjectsToLearn
     */
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
        singleton.addWordToLearnSession(midDictObj3, dnow);
        list = singleton.getListOfObjectsToLearn(dnow);
        Assert.assertEquals("List should contain two ids", 2, list.size());

        //add for date in one week
        Calendar cal = MnemoCalendar.getInstance();
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


    /**
     * method DelayLearningSessionFromDate
     */
    @Test
    public void testDelayLearningSessionFromDate() throws Exception {

        //initially
        Calendar today = MnemoCalendar.getInstance();
        Calendar todayMinusOne = MnemoCalendar.getInstance();
        todayMinusOne.add(Calendar.DAY_OF_YEAR, -1);
        Calendar todayMinusThree = MnemoCalendar.getInstance();
        todayMinusThree.add(Calendar.DAY_OF_YEAR, -3);
        Calendar todayMinusSeven = MnemoCalendar.getInstance();
        todayMinusSeven.add(Calendar.DAY_OF_YEAR, -7);

        int result = singleton.DelayLearningSessionFromDate(0);
        Assert.assertEquals("We should not have any elements updated", 0, result);

        //update some dates, in the past
        //first object set to yesterday
        DictionaryObject dictObj = DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(midDictObj1);
        //update memory monitoring object
        String where = MemoryManagerContract.MemoryMonitoring._ID + " = '" + dictObj.getMemoryMonitoringID() + "'";
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(todayMinusOne.getTime()));
        long resUpd = MnemoDBHelper.getInstance(Robolectric.getShadowApplication().getApplicationContext()).getWritableDatabase().update(MemoryManagerContract.MemoryMonitoring.TABLE_NAME, value, where, null);
        Assert.assertEquals("Just check that update is successfull", 1, resUpd);
        //add word to past learn session
        resUpd = singleton.addWordToLearnSession(midDictObj1, todayMinusOne.getTime());
        Assert.assertTrue("Just check that update is successfull", resUpd > 0);

        result = singleton.DelayLearningSessionFromDate(1);
        Assert.assertEquals("We should update one element", 1, result);
        //check that today, we now have the id back
        Vector<Global.Couple<Long, Integer>> list = singleton.getListOfObjectsToLearn(today.getTime());
        Assert.assertTrue("Check ids", list.size() == 1 && list.get(0).val1 == midDictObj1 && list.get(0).val2 == 1);
        MnemoDBHelper.getInstance(Robolectric.getShadowApplication().getApplicationContext()).getWritableDatabase().delete(MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID + " = " + midDictObj1, null);

        //check with three words at different dates
        //word 1
        dictObj = DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(midDictObj1);
        where = MemoryManagerContract.MemoryMonitoring._ID + " = '" + dictObj.getMemoryMonitoringID() + "'";
        value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(todayMinusOne.getTime()));
        MnemoDBHelper.getInstance(Robolectric.getShadowApplication().getApplicationContext()).getWritableDatabase().update(MemoryManagerContract.MemoryMonitoring.TABLE_NAME, value, where, null);
        singleton.addWordToLearnSession(midDictObj1, todayMinusOne.getTime());
        //word 2
        dictObj = DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(midDictObj2);
        where = MemoryManagerContract.MemoryMonitoring._ID + " = '" + dictObj.getMemoryMonitoringID() + "'";
        value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(todayMinusThree.getTime()));
        MnemoDBHelper.getInstance(Robolectric.getShadowApplication().getApplicationContext()).getWritableDatabase().update(MemoryManagerContract.MemoryMonitoring.TABLE_NAME, value, where, null);
        singleton.addWordToLearnSession(midDictObj2, todayMinusThree.getTime());
        //word 3
        dictObj = DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(midDictObj3);
        where = MemoryManagerContract.MemoryMonitoring._ID + " = '" + dictObj.getMemoryMonitoringID() + "'";
        value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(todayMinusSeven.getTime()));
        MnemoDBHelper.getInstance(Robolectric.getShadowApplication().getApplicationContext()).getWritableDatabase().update(MemoryManagerContract.MemoryMonitoring.TABLE_NAME, value, where, null);
        singleton.addWordToLearnSession(midDictObj3, todayMinusSeven.getTime());


        result = singleton.DelayLearningSessionFromDate(7);
        Assert.assertEquals("We should update three elements", 3, result);
        //check that today, we now have the id back
        list = singleton.getListOfObjectsToLearn(today.getTime());
        Assert.assertTrue("Check ids", list.size() == 3);

        //@todo check ids and delay
    }
}