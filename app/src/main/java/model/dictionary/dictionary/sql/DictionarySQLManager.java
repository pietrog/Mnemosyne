package model.dictionary.dictionary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.DictionaryObject;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.BaseSQLManager;

import model.dictionary.Global.Couple;

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
     * Get a list of all word of the dictionary
     * @param dictionaryID id of the dictionary
     * @return cursor
     */
    public Cursor getAllDictionaryObjectsCursor(long dictionaryID){
        String sql =  "SELECT * FROM " + WordContract.Word.TABLE_NAME + ", " + DictionaryObjectContract.DictionaryObject.TABLE_NAME
                + " WHERE " + DictionaryObjectContract.DictionaryObject.DICTIONARYID + " = " + dictionaryID
                + " AND " + WordContract.Word.DICTIONARYOBJECTID + " = " + DictionaryObjectContract.DictionaryObject.CID
                + " ORDER BY " + WordContract.Word.WORD;

        return rawQuery(sql, null);
    }

    /**
     * Get a cursor containing a list of DictionaryObject, built from a list of id given in parameter
     * @param list list of dictionary object ids, as couple(long, integer), first is the id, second one represent a delay in number of days
     * @return cursor containing all dictionariy objects from the list
     */
    public Cursor getDictionaryObjectsFromLearningList(Vector<Couple<Long, Integer>> list){
        if (list == null || list.size() == 0)
            return null;

        String sql = "SELECT * FROM " + WordContract.Word.TABLE_NAME
                + " WHERE " + DictionaryContractBase.DictionaryBase._ID + " IN (" ;

        for (Couple c : list)
            sql += c.val1 + ", ";
        sql = sql.substring(0, sql.length() - 2);
        sql += ")";

        return rawQuery(sql, null);
    }

    /**
     * Insert new word in the dictionary, related to dictionary object
     * @param dictionaryObjectID dictionary object id
     * @param word word to insert
     * @param definition word definition
     * @return id of the inserted object if successful, -1 otherwise
     */
    public long addNewWord(long dictionaryObjectID, String word, String definition){

        //create the memory monitoring object part
        DictionaryObject.MemoryMonitoringObject monitor = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();

        if (dictionaryObjectID < 0)
            return Global.BAD_PARAMETER;

        if (word == null || word.length() == 0)
            return  Global.BAD_PARAMETER;

        ContentValues val = new ContentValues();
        val.put(WordContract.Word.DICTIONARYOBJECTID, dictionaryObjectID);
        val.put(WordContract.Word.WORD, word);
        val.put(WordContract.Word.DEFINITION, definition);
        return add(val, WordContract.Word.TABLE_NAME);
    }

    /**
     * Remove dictionaries identified by ids in listIDs
     * @param listIDs list of dictionaries' ids to remove
     * @return number of affected rows
     */
    public int remove(long[] listIDs){
        return remove(listIDs, DictionaryContractBase.DictionaryBase.TABLE_NAME);
    }

    /**
     * Get the dictionary object with the memory monitoring part
     * Full object means object built from dictionary and memory manager tables
     * @param objectid long dictionary object id
     * @return DictionaryObject dictionary object if successful, null otherwise
     */
    public DictionaryObject getFullObjectFromID(long objectid){
        //get the full object cursor (object from dictionaryObject + memory monitoring + memory phase)
        String fullObjectSQL =
                "SELECT * from " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + ", " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME
                        + " ON " + MemoryManagerContract.MemoryMonitoring.CID + " = " + DictionaryObjectContract.DictionaryObject.CID
                        + " WHERE " + DictionaryObjectContract.DictionaryObject.CID + " = " + objectid
                        + " AND " + DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID + " = " + MemoryManagerContract.MemoryMonitoring.CID;

        Cursor cursor = rawQuery(fullObjectSQL, null);

        if (!cursor.moveToFirst())
            return null;

        return DictionaryObject.LoadFromCursor(cursor);
    }


    /**
     * Add a new dictionary in catalogue identified by catalogue id
     * @param catalogueID long catalogue id
     * @param name string name of the new dictionary
     * @param description string dictionary description
     * @return new row id
     */
    public long addDictionaryInCatalogue(long catalogueID, String name, String description){
        if (name == null || description == null)
            return Global.BAD_PARAMETER;
        if (name.length() < 3)
            return Global.BAD_PARAMETER;
        ContentValues val = new ContentValues();
        val.put(DictionaryContractBase.DictionaryBase.NAME, name);
        val.put(DictionaryContractBase.DictionaryBase.DESCRIPTION, description);
        val.put(DictionaryContractBase.DictionaryBase.CATALOGUEID, catalogueID);

        return add(val, DictionaryContractBase.DictionaryBase.TABLE_NAME);
    }
}
