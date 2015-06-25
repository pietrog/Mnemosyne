package model.dictionary.dictionary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.BaseSQLManager;

import model.dictionary.Global.Couple;
import model.dictionary.tools.GeneralTools;

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
        String sql =  "SELECT " + WordContract.Word.CID + ", " + WordContract.Word.ALL + ", " + DictionaryObjectContract.DictionaryObject.ALL
                + " FROM " + WordContract.Word.TABLE_NAME + ", " + DictionaryObjectContract.DictionaryObject.TABLE_NAME
                + " WHERE " + DictionaryObjectContract.DictionaryObject.DICTIONARYID + " = " + dictionaryID
                + " AND " + WordContract.Word.DICTIONARYOBJECTID + " = " + DictionaryObjectContract.DictionaryObject.CID
                + " ORDER BY " + WordContract.Word.WORD;

        return rawQuery(sql, null);
    }

    /**
     * Get a cursor containing a list of DictionaryObject to learn, from a date
     * @param date date to check all object to learn
     * @return cursor containing all dictionariy objects
     */
    public Cursor getDictionaryObjectsFromLearningList(String date){

        if (date == null)
            return null;

        String sql =  "SELECT " + WordContract.Word.CID + ", " + WordContract.Word.ALL + ", " + DictionaryObjectContract.DictionaryObject.ALL + ", " + MemoryManagerContract.MemoryManager.DAYS_OF_DELAY
                + " FROM " + WordContract.Word.TABLE_NAME + ", " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + ", " + MemoryManagerContract.MemoryManager.TABLE_NAME
                + " WHERE " + MemoryManagerContract.MemoryManager.DATE + " = '" + date + "'"
                + " AND " + DictionaryObjectContract.DictionaryObject.CID + " = " + MemoryManagerContract.MemoryManager.TABLE_NAME +"." + MemoryManagerContract.MemoryManager.DICTIONARYOBJECTID
                + " AND " + WordContract.Word.TABLE_NAME +"."+ WordContract.Word.DICTIONARYOBJECTID + " = " + DictionaryObjectContract.DictionaryObject.CID + " ORDER BY " + WordContract.Word.WORD;

        return rawQuery(sql, null);
    }

    /**
     * Create sql row corresponding to dictionary ID and a memoryObject ID
     * @param dictionaryID dictionary sql id poiting on existing dictionary
     * @param memoryObjectID memory monitoring sql id, can be null, or point on a memory monitoring row
     * @return id of the new row
     */
    public long createNewDictionaryObject (long dictionaryID, long memoryObjectID){
        if (dictionaryID <= 0)
            return Global.BAD_PARAMETER;

        ContentValues value = new ContentValues();
        value.put(DictionaryObjectContract.DictionaryObject.DICTIONARYID, dictionaryID);
        value.put(DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID, memoryObjectID);

        return add(value, DictionaryObjectContract.DictionaryObject.TABLE_NAME);
    }

    /**
     * Insert new word in the dictionary, related to dictionary object
     * @param dictionaryID  sql dictionary id
     * @param word word to insert
     * @param definition word definition
     * @return id of the inserted object if successful, -1 otherwise
     */
    public long addNewWord(long dictionaryID, String word, String definition){

        if (dictionaryID < 0)
            return Global.BAD_PARAMETER;

        if (word == null || word.length() == 0)
            return  Global.BAD_PARAMETER;

        //create the sql memory monitoring object
        long memoryObjID = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();

        //create the dictionary object object
        long dictObjID = createNewDictionaryObject(dictionaryID, memoryObjID);

        //create the word in sql
        ContentValues val = new ContentValues();
        val.put(WordContract.Word.DICTIONARYOBJECTID, dictObjID);
        val.put(WordContract.Word.WORD, word);
        val.put(WordContract.Word.DEFINITION, definition);
        return add(val, WordContract.Word.TABLE_NAME);
    }

    /**
     * Get Word object from its id
     * @param wordID sql id of a word
     * @return WordDefinitionObj object null otherwise
     */
    public WordDefinitionObj getWordFromID(long wordID){
        if (wordID <= 0)
            return null;

        String fullObjectSQL =
                "SELECT " +  DictionaryObjectContract.DictionaryObject.ALL + ", " + MemoryManagerContract.MemoryMonitoring.ALL + ", " + WordContract.Word.ALL
                        + " from " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + ", " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME + ", " + WordContract.Word.TABLE_NAME
                        + " WHERE " + WordContract.Word.CID + " = " + wordID
                        + " AND " + DictionaryObjectContract.DictionaryObject.CID + "=" + WordContract.Word.DICTIONARYOBJECTID
                        + " AND " + DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID + " = " + MemoryManagerContract.MemoryMonitoring.CID;

        Cursor cursor = rawQuery(fullObjectSQL, null);

        if (! cursor.moveToFirst())
            return null;

        return WordDefinitionObj.LoadFromCursor(cursor);
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
     * Get the memory object from the dictionary object id
     * Full object means object built from dictionary and memory manager tables
     * @param dictionaryObjectID long sql dictionary object id
     * @return MemoryObject object if successful, null otherwise
     */
    public MemoryObject getMemoryObjectFromDictionaryObjectID(long dictionaryObjectID){

        if (dictionaryObjectID <= 0)
            return null;

        //get the full object cursor (object from dictionaryObject + memory monitoring + memory phase)
        String fullObjectSQL =
                "SELECT " + DictionaryObjectContract.DictionaryObject.ALL + ", " + MemoryManagerContract.MemoryMonitoring.ALL
                        + " from " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + ", " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME
                        + " WHERE " + DictionaryObjectContract.DictionaryObject.CID + " = " + dictionaryObjectID
                        + " AND " + DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID + " = " + MemoryManagerContract.MemoryMonitoring.CID;

        Cursor cursor = rawQuery(fullObjectSQL, null);

        if (!cursor.moveToFirst())
            return null;

        MemoryObject res = MemoryObject.LoadFromCursor(cursor);
        cursor.close();

        return res;
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
