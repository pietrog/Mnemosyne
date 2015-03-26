package model.dictionary.dictionary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

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
        super(new DictionaryDBHelper(context));
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
                + "' and " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + " = '" + dictionaryName +"'";

        return rawQuery(sql);
    }

    /**
     * Insert new word in the dictionary
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     * @param word word to insert
     * @param definition word definition
     * @return id of the inserted object if successful, -1 otherwise
     */
    public long add(String catalogueName, String dictionaryName, String word, String definition){

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
     * Update the next alert date for the word identified by id
     * @param id id of the word to update
     * @param nextDate next date for word alert
     * @return row affected
     */
    public int updateNextDateForWord(long id, Date nextDate){
        Cursor cursor = rawQuery(DictionaryOfWordContract.getWordSQL(id));

        if (!cursor.moveToFirst()){
            Logger.w("DictionarySQLManager::updateNextDateForWord", " no word to update for this id "+id);
            return -1;
        }

        //replace the last by the today s date, and next by the new one
        String newNext = GeneralTools.getSQLDate(nextDate);
        String oldNext = GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_DATE_NEXT_LEARNING);
        //if new next and old next are different, we remove id from the oldNext date list
        if (newNext.compareTo(oldNext) != 0)
            MemoryManagerSQLManager.getInstance().removeIDsFromList(id, oldNext);

        String today = GeneralTools.getSQLDate(Calendar.getInstance().getTime());

        ContentValues value = new ContentValues();
        value.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_DATE_NEXT_LEARNING, newNext);
        value.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_DATE_LAST_LEARNING, today);
        return update(value, DictionaryOfWordContract.DictionaryOfWord.TABLE_NAME, DictionaryOfWordContract.getWhereWordSQL(id));
    }
}
