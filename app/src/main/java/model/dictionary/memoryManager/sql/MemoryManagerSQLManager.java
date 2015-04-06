package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.BaseSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;

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
    public int initMemoryPhaseMap(Map<Integer, DictionaryObject.MemoryPhaseObject> memoryPhaseMap){
        memoryPhaseMap = new HashMap<>();
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
                    (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID));

            memoryPhaseMap.put((int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryPhase._ID), current);
            if (!cursor.moveToNext()){
                memoryPhaseMap.clear();
                return Global.FAILURE;
            }
        }
        while (!cursor.isAfterLast());

        return Global.SUCCESS;
    }


    /**
     * Add the word to the learn session given by date parameter
     * @param id id of the word
     * @param date alert date
     * @return row id of the memory database row
     */
    public long addWordToLearnSession(long id, Date date){
        //check if there is already a list at this date (CREATE or UPDATE the line)
        Vector<Integer> list = getListOfObjectIDs(date);

        //two possibilities: insert new one if nothing is scheduled to this date, or update it
        //insert
        if (list == null){
            ContentValues value = new ContentValues();
            String strdate = GeneralTools.getSQLDate(date);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE, strdate);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, id);
            return add(value, MemoryManagerContract.MemoryManager.TABLE_NAME);
        }
        //update
        else{
            ContentValues value = new ContentValues();
            //@todo: remove this int cast because we want a long
            list.add((int)id);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, GeneralTools.getStringFrom(list));
            return update(value, MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + GeneralTools.getSQLDate(date) + "'");
        }
    }

    /**
     * Get the list of id for today
     * @return list of long ids
     */
    public Vector<Integer> getTodayList(){
        return getListOfObjectIDs(Calendar.getInstance().getTime());
    }

    /**
     * Return a list of dictionary object for a given date
     * @param date date
     * @return vector of longMemoryPhaseObject
     */
    public Vector<Integer> getListOfObjectIDs(Date date){
        return getListOfObjectIDs(GeneralTools.getSQLDate(date));
    }

    public Vector<Integer> getListOfObjectIDs(String date){
        Vector<Integer> res = new Vector<>();

        String SQL_TODAYLIST = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME + " WHERE "
                + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + date + "'";

        Logger.i("MemoryManagerSQL::getListOfObjectIDs", " sql " + SQL_TODAYLIST);
        Cursor cursor = rawQuery(SQL_TODAYLIST, null);

        //should have only one result
        if (cursor.getCount() == 0)
            return null;

        if (!cursor.moveToFirst())
            Logger.i("MemoryManagerSQL::getListOfObjectIDs", " cursor move to first failed");

        String listBrut = GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST);
        String[] listSplited = listBrut.split(",");
        for (String curr : listSplited)
            res.add(Integer.parseInt(curr));

        return  res;
    }

    /**
     * Remove an object id from a day alert list
     * @param id dictionary object id
     * @param date date alert of the list
     * @return row impacted
     */
    public int removeIDsFromList(long id, Date date){
        return removeIDsFromList(id, GeneralTools.getSQLDate(date));
    }

    /**
     * Remove id from list given a date
     * @param id id to remove
     * @param date string storing the date
     * @return number of impacted rows
     */
    public int removeIDsFromList(long id, String date) {
        //get the list and remove given id
        Vector<Integer> list = getListOfObjectIDs(date);
        if (list == null)
            return -1;
        list.remove((int)id);
        //update the list
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, GeneralTools.getStringFrom(list));
        return update(value, MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + date + "'");
    }

    /**
     * Persist dictionary object in database
     * @param object object to persist
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    public int updateDictionaryObjectInDB(DictionaryObject object){
        if (object == null) {
            Logger.i("MemoryManagerSQLManager::updateDictionaryObjectInDB"," null object");
            return Global.NOT_AVAILABLE;
        }

        int res = Global.FAILURE;
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
        if (addWordToLearnSession(object.getID(), object.getMemoryMonitoring().mNextLearnt) == -1)
            res = Global.FAILURE;

        if (res == Global.FAILURE)
            Logger.w("MemoryManagerSQLManager::updateDictionaryObjectInDB"," something wrong occured during update of object");
        return res;
    }
}
