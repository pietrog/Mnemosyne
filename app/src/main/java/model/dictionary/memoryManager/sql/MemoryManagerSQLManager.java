package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.tools.BaseSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.Global.Couple;

/**
 * Created by pietro on 24/03/15.
 *
 */
public class MemoryManagerSQLManager extends BaseSQLManager{

    private static MemoryManagerSQLManager instance = null;


    /**
     * Only called from MnemoCentral, in MnemoCentral, InitSystem
     * @param context context of application
     * @return unique instance of MemoryManagerSQLManager
     */
    public static synchronized MemoryManagerSQLManager getInstance(Context context){
        if (instance == null)
            instance = new MemoryManagerSQLManager(context);
        return instance;
    }

    /**
     * Convenience caller for singleton, should be initialized first with getInstance(Context context)
     * @return unique instance of MemoryManagerSQLManager
     */
    public static synchronized MemoryManagerSQLManager getInstance(){
        return instance;
    }


    /**
     * IMPLEMENTATION
     */

    /**
     * Private cstor for MemoryManagerSQLManager (singleton)
     * @param context application context
     */
    private MemoryManagerSQLManager (Context context){
        super(context);
    }


    /**
     * Fill in the MemoryPhase map given in parameter from what we have in database
     * @param memoryPhaseMap map to fill
     * @return {Global.SUCCESS} if successful {Global.FAILURE} otherwise
     */
    public int initMemoryPhaseMap(Map<Long, MemoryManager.MemoryPhaseObject> memoryPhaseMap){
        String sql = "SELECT * FROM " + MemoryManagerContract.MemoryPhase.TABLE_NAME;
        Cursor cursor = rawQuery(sql, null);
        if (!cursor.moveToFirst())
            return Global.FAILURE;

        do {
            MemoryManager.MemoryPhaseObject current = new MemoryManager.MemoryPhaseObject(
                    GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryPhase.PHASE_NAME),
                    (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.DURATION_PHASE),
                    (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.FIRST_PERIOD),
                    (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.PERIOD_INCREMENT),
                    GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID));

            memoryPhaseMap.put(GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase._ID), current);
        }
        while (cursor.moveToNext());

        cursor.close();

        return Global.SUCCESS;
    }


    /**
     * Create a new basic memory monitoring object. The init object points on the first memory phase and everything is set for the init process
     * @return id of the new row
     */
    public long createNewMemoryMonitoringObject(){

        Calendar now = Calendar.getInstance();
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP, GeneralTools.getSQLDate(now.getTime()));
        value.put(MemoryManagerContract.MemoryMonitoring.DATE_ADDED, GeneralTools.getSQLDate(now.getTime()));
        value.put(MemoryManagerContract.MemoryMonitoring.LAST_LEARNT, GeneralTools.getSQLDate(now.getTime()));
        now.add(Calendar.DAY_OF_YEAR, MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod);
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(now.getTime()));
        value.put(MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN, MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod);
        value.put(MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID, MemoryManager.mIDFirstPhase);

        return add(value, MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
    }

    /**
     * Add the word to the learn session given by date parameter
     * @param dictObjID long sql dictionary object id
     * @param date alert date
     * @return the id of the memory monitoring database object
     */
    public long addWordToLearnSession(long dictObjID, Date date){
        ContentValues value = new ContentValues();
        String strdate = GeneralTools.getSQLDate(date);
        value.put(MemoryManagerContract.MemoryManager.DATE, strdate);
        value.put(MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID, dictObjID);
        return add(value, MemoryManagerContract.MemoryManager.TABLE_NAME);
    }

    /**
     * Return a list of dictionary object for a given date
     * @param date date
     * @return vector of longMemoryPhaseObject
     */
    public Vector<Couple<Long, Integer>> getListOfObjectsToLearn(Date date){
        if (date == null)
            return null;
        return getListOfObjectsToLearn(GeneralTools.getSQLDate(date));
    }

    public Vector<Couple<Long, Integer>> getListOfObjectsToLearn(String date){
        Vector<Couple<Long, Integer>> res = new Vector<>();


        String SQL_TODAYLIST = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME
                + " WHERE " + MemoryManagerContract.MemoryManager.DATE + " = '" + date + "'";

        Logger.i("MemoryManagerSQL::getListOfObjectIDs", " sql " + SQL_TODAYLIST);
        Cursor cursor = rawQuery(SQL_TODAYLIST, null);

        //should have only one result
        if (cursor.getCount() == 0)
            return res;

        if (!cursor.moveToFirst()) {
            Logger.i("MemoryManagerSQL::getListOfObjectIDs", " cursor move to first failed");
            return res;
        }

        do {
            Couple<Long, Integer> c = new Couple<>(GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID),
                    (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryManager.DAYS_OF_DELAY));
            res.add(c);
        }while(cursor.moveToNext());

        cursor.close();

        return  res;
    }

    /**
     * Persist dictionary object in database
     * @param object object to persist
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    public int updateMemoryObjectInDB(MemoryObject object){
        if (object == null) {
            Logger.i("MemoryManagerSQLManager::updateDictionaryObjectInDB"," null object");
            return Global.BAD_PARAMETER;
        }

        Logger.i("MemoryManagerSQLManager::updateMemoryObjectInDB", " Update memory object: " + object.toString());
        //process for object in normal cycle
        //update memory monitoring object
        if (update(object.memoryObjectToContentValues(), MemoryManagerContract.MemoryMonitoring.TABLE_NAME, MemoryManagerContract.MemoryMonitoring.CID + " = "+object.getMemoryMonitoringID()) <= 0) {
            Logger.w("MemoryManagerSQLManager::updateDictionaryObjectInDB", " something wrong occured during update of object");
            return Global.FAILURE;
        }
        return Global.SUCCESS;
    }

    /**
     * Remove dictionary object identified by dictionaryObjectID from learning session
     * @param dictionaryObjectID dictionary object id
     * @return
     */
    public int removeObjectFromLearningSession(long dictionaryObjectID){
        long[] ids = { dictionaryObjectID };
        return remove(ids, MemoryManagerContract.MemoryManager.TABLE_NAME);
    }


    /**
     * Retrieve all missed learning session(from today to pastDate), update dely and nextLearningSession to today
     * @param daysInThePast Number of days in the past to check
     * @return return the number of object updated
     */
    public int DelayLearningSessionFromDate(int daysInThePast){
        Calendar cal = Calendar.getInstance();
        String today = GeneralTools.getSQLDate(Calendar.getInstance().getTime());
        int nbUpdated = 0;
        int incr = 0;

        if (daysInThePast < 0)
            daysInThePast *= -1;
        if (daysInThePast == 0)
            return nbUpdated;

        int delay = 1;
        cal.add(Calendar.DAY_OF_YEAR, -1);

        while (incr++ < daysInThePast){
            Calendar pastDate = Calendar.getInstance();
            pastDate.add(Calendar.DAY_OF_YEAR, -1*incr);

            //for each day before, increment the delay
            Vector<Couple<Long, Integer>> list = getListOfObjectsToLearn(pastDate.getTime());
            // maybe there is nothing at this date
            if (list == null || list.size() == 0) {
                cal.add(Calendar.DAY_OF_YEAR, -1);
                ++delay;
                continue;
            }
            // update each entry in memory manager, update the delay and the new date
            //couple contains val1 => dictionaryObjectID, val2 => delay
            for (Couple <Long, Integer> c: list) {
                ContentValues val = new ContentValues();
                val.put(MemoryManagerContract.MemoryManager.DATE, today);
                val.put(MemoryManagerContract.MemoryManager.DAYS_OF_DELAY, c.val2 + delay);
                nbUpdated += update(val, MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID + " = " + c.val1);
            }

            //finally, update all entries with this old next date to the new one
            // update next learning date in memory monitoring
            String where = MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT + " = '" + GeneralTools.getSQLDate(pastDate.getTime()) + "'";
            ContentValues val = new ContentValues();
            val.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, today);
            update(val, MemoryManagerContract.MemoryMonitoring.TABLE_NAME, where);

            // go 1 more day in the past, until pastDateStr
            cal.add(Calendar.DAY_OF_YEAR, -1);
            ++delay;
        }
        return nbUpdated;
    }

    public void getall(){
        String sql = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME;

        Cursor cursor = rawQuery(sql, null);
        Vector<String> vect = new Vector<>();

        cursor.moveToFirst();

        do{
            String str = GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryManager.DATE)
                    + " -- ID " + GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryManager.CID)
                    + " -- DELAY " + GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryManager.DAYS_OF_DELAY)
                    + " -- DICTID " + GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID);
            vect.add(str);
        }while (cursor.moveToNext());

        int t = 5;
        t+= -6;
    }

}
