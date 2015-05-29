package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.DictionaryObject.MemoryMonitoringObject;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
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
    public int initMemoryPhaseMap(Map<Long, DictionaryObject.MemoryPhaseObject> memoryPhaseMap){
        String sql = "SELECT * FROM " + MemoryManagerContract.MemoryPhase.TABLE_NAME;
        Cursor cursor = rawQuery(sql, null);
        if (!cursor.moveToFirst())
            return Global.FAILURE;

        do {
            DictionaryObject.MemoryPhaseObject current = new DictionaryObject.MemoryPhaseObject(
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
     * Create a new memory monitoring object. The init object points on the first memory phase and everything is set for the init process
     * @return id of the new row
     */
    public MemoryMonitoringObject createNewMemoryMonitoringObject(){
        MemoryMonitoringObject obj = new MemoryMonitoringObject();
        long id = add(obj.toContentValues(), MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
        obj.setID(id);
        return obj;
    }

    /**
     * Create the base dictionary object from a dictionary id and memory monitoring id
     * @param dictionaryID long dictionary id
     * @param memoryMonitoringID long memory monitoring id
     * @return id of the new row
     */
    public long createDictionaryObject(long dictionaryID, long memoryMonitoringID){
        if (dictionaryID < 0 || memoryMonitoringID < 0)
            return Global.FAILURE;
        ContentValues value = new ContentValues();
        value.put(DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID, memoryMonitoringID);
        value.put(DictionaryObjectContract.DictionaryObject.DICTIONARYID, dictionaryID);
        return add(value, DictionaryObjectContract.DictionaryObject.TABLE_NAME);
    }


    /**
     * Add the word to the learn session given by date parameter
     * @param id id of the word
     * @param date alert date
     * @return the id of the memory monitoring database object
     */
    public long addWordToLearnSession(long id, Date date){
        ContentValues value = new ContentValues();
        String strdate = GeneralTools.getSQLDate(date);
        value.put(MemoryManagerContract.MemoryManager.DATE, strdate);
        value.put(MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID, id);
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
    public int updateDictionaryObjectInDB(DictionaryObject object){
        if (object == null) {
            Logger.i("MemoryManagerSQLManager::updateDictionaryObjectInDB"," null object");
            return Global.BAD_PARAMETER;
        }

        int res = Global.SUCCESS;
        //if it is the first time we update this object
        if (object.getMemoryMonitoringID() == -1){
            //create the memory monitoring object
            long id = add(object.getMemoryMonitoring().toContentValues(), MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
            if (id != Global.FAILURE)
                object.setMemoryMonitoringID(id);
        }

        //process for object in normal cycle
        //update dictionary object and memory monitoring object
        if (update(object.toContentValues(), DictionaryContractBase.DictionaryBase.TABLE_NAME, " _ID = " + object.getID()) == 0)
            res = Global.FAILURE;
        if (update(object.getMemoryMonitoring().toContentValues(), MemoryManagerContract.MemoryMonitoring.TABLE_NAME, "_ID = "+object.getMemoryMonitoringID()) == 0)
            res = Global.FAILURE;
        //if memory monitoring object changes next dates, keep it up to date
        if (addWordToLearnSession(object.getID(), object.getMemoryMonitoring().getNextLearnt()) == -1)
            res = Global.FAILURE;

        if (res == Global.FAILURE)
            Logger.w("MemoryManagerSQLManager::updateDictionaryObjectInDB"," something wrong occured during update of object");
        return res;
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
            if (list == null) {
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

}
