package model.dictionary.dictionary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import model.dictionary.tools.BaseSQLManager;

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

}
