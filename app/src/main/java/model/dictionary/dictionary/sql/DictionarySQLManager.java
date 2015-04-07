package model.dictionary.dictionary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.BaseSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;

/**
 * Created by pietro on 23/03/15.
 *
 * This class is a Singleton
 * Deals with dictionary table, so with get/add/remove/modify words
 * Give cursors, manage dbHelper,...
 */
public class DictionarySQLManager extends BaseSQLManager{


    private static DictionarySQLManager instance;

    public static synchronized DictionarySQLManager getInstance(Context context){
        if (instance == null){
            instance = new DictionarySQLManager(context);
        }
        return instance;
    }

    public static synchronized DictionarySQLManager getInstance(){
        return instance;
    }

    /**
     * IMPLEMENTATION
     */

    /**
     * Private constructor, DictionarySQLManager is a singleton
     */
    private DictionarySQLManager (Context context){
        super(context);
    }

    /**
     * Get a list of all dictionary items in dictionary dictionaryName of catalogueName
     * @param catalogueName name of the catalogue
     * @param dictionaryName dictionary name
     * @return cursor
     */
    public Cursor getAllDictionaryObjectsCursor(String catalogueName, String dictionaryName){
        String sql =  "SELECT * FROM " + DictionaryContractBase.DictionaryBase.TABLE_NAME
                + " WHERE " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + " = '" + catalogueName
                + "' and " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + " = '" + dictionaryName +"'"
                + " ORDER BY " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_WORD;

        return rawQuery(sql, null);
    }

    public Cursor getDictionaryObjectsFromLearningList(Vector<Integer> list){
        String sql = "SELECT * FROM " + DictionaryContractBase.DictionaryBase.TABLE_NAME
                + " WHERE " + DictionaryContractBase.DictionaryBase._ID + " IN (" ;

        for(Integer i : list)
            sql += i + ", ";

        sql = sql.substring(0, sql.length()-2);
        sql += ")";

        return rawQuery(sql, null);
    }

    /**
     * Insert new word in the dictionary
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     * @param word word to insert
     * @param definition word definition
     * @return id of the inserted object if successful, -1 otherwise
     */
    public long addNewWord(String catalogueName, String dictionaryName, String word, String definition){

        ContentValues val = new ContentValues();
        val.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME, catalogueName);
        val.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME, dictionaryName);
        val.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_WORD, word);
        val.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_DEFINITION, definition);
        return add(val, DictionaryContractBase.DictionaryBase.TABLE_NAME);
    }

    /**
     * Remove dictionaries identified by ids in listIDs
     * @param listIDs list of dictionaries' ids to remove
     * @return number of affected rows
     */
    public int remove(long[] listIDs){
        return remove(listIDs, DictionaryOfWordContract.DictionaryBase.TABLE_NAME);
    }

    /**
     * Get the full dictionary object from sql given an id
     * Full object means object built from dictionary and memory manager tables
     * If the object was created just before, the constructor of DictionaryObject will initialize the memory monitoring part
     * @param objectid object id to get
     * @return dictionary object if successful, null otherwise
     */
    public DictionaryObject getFullObjectFromID(long objectid){
        //get the full object cursor (object from dictionary + memory monitoring + memory phase)
        String fullObjectSQL =
                "SELECT * from " + DictionaryContractBase.DictionaryBase.TABLE_NAME + " LEFT JOIN " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME
                        + " ON " + MemoryManagerContract.MemoryMonitoring.CID + " = " + DictionaryContractBase.DictionaryBase.MEMORY_MONITORING_ID
                        + " WHERE " + DictionaryContractBase.DictionaryBase.CID + " = ? "
                ;
        Cursor cursor = rawQuery(fullObjectSQL, new String[] {String.valueOf(objectid)});

        if (!cursor.moveToFirst())
            return null;

        //construct the memory monitoring object
        DictionaryObject.MemoryMonitoringObject monitor = new DictionaryObject.MemoryMonitoringObject();
        monitor.mLastLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT));
        monitor.mNextLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT));
        monitor.mDateAdded = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.DATE_ADDED));
        monitor.mBeginningOfMP = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP));
        monitor.mDaysBetween = (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN);
        monitor.mMemoryPhaseID = GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID);

        DictionaryObject object = new DictionaryObject(
                objectid,
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME),
                GeneralTools.getLongElement(cursor, DictionaryContractBase.DictionaryBase.MEMORY_MONITORING_ID),
                monitor.mMemoryPhaseID == -1 ? null : monitor);

        Logger.i("DictionarySQLManager::DictionaryObject", " dictionary loaded: " + object);

        return object;
    }

}
