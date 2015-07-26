package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.Global.Couple;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.tools.BaseSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.tools.MnemoCalendar;

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

        Calendar now = MnemoCalendar.getInstance();
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP, GeneralTools.getSQLDate(now.getTime()));
        value.put(MemoryManagerContract.MemoryMonitoring.DATE_ADDED, GeneralTools.getSQLDate(now.getTime()));
        value.put(MemoryManagerContract.MemoryMonitoring.LAST_LEARNT, GeneralTools.getSQLDate(now.getTime()));
        now.add(Calendar.DAY_OF_YEAR, MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod);
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARN, now.getTime().getTime());
        value.put(MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN, MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod);
        value.put(MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID, MemoryManager.mIDFirstPhase);

        return add(value, MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
    }

    /**
     * Return a list of couple of dictionary object for a given date
     * @param date date to fetch as long in miliseconds
     * @return vector of couple of longMemoryPhaseObject, delay in days
     */
    public Vector<Couple<Long, Integer>> getListOfObjectsToLearn(long date){
        Vector<Couple<Long, Integer>> res = new Vector<>();

        Cursor cursor = getCursorOfObjectsToLearn(date);

        //should have only one result
        if (cursor.getCount() == 0)
            return res;

        if (!cursor.moveToFirst()) {
            Logger.i("MemoryManagerSQL::getListOfObjectIDs", " cursor move to first failed");
            return res;
        }

        do {
            long nextLearn = GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN);
            int delay = (int)GeneralTools.getNumberOfDaysBetweenTwoDates(date, nextLearn);
            Couple<Long, Integer> c = new Couple<>(GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.CSID), delay);
            res.add(c);
        }while(cursor.moveToNext());

        cursor.close();

        return  res;
    }

    /**
     * Return a cursor of object to learn for a given date
     * Rows returned all contain next learn date less or equal to given date
     * @param date date to fetch as long in miliseconds
     * @return cursor all objects with a next learn date less or equal to given date
     */
    public Cursor getCursorOfObjectsToLearn(String date){
        if (date == null)
            return null;
        return getCursorOfObjectsToLearn(GeneralTools.getDateFromSQLDate(date).getTime());
    }

    public Cursor getCursorOfObjectsToLearn(long date){
        //request all the next learn dates smaller of equal to date
        String SQL_TODAYLIST =  "SELECT " + WordContract.Word.CID + ", " + WordContract.Word.ALL + ", " + DictionaryObjectContract.DictionaryObject.ALL + ", " + MemoryManagerContract.MemoryMonitoring.NEXT_LEARN
                + " FROM " + WordContract.Word.TABLE_NAME + ", " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + ", " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME
                + " WHERE " + MemoryManagerContract.MemoryMonitoring.NEXT_LEARN + " <= " + date
                + " AND " + DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID + " = " + MemoryManagerContract.MemoryMonitoring.CID
                + " AND " + WordContract.Word.TABLE_NAME +"."+ WordContract.Word.DICTIONARYOBJECTID + " = " + DictionaryObjectContract.DictionaryObject.CID + " ORDER BY " + WordContract.Word.WORD;

        return rawQuery(SQL_TODAYLIST, null);
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
}
