package model.dictionary.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContract;
import model.dictionary.dictionary.sql.DictionaryDBHelper;

/**
 * Created by pietro on 30/01/15.
 *
 */
public class Dictionary {

    private static final String TABLE_NAME = DictionaryContract.Dictionary.TABLE_NAME;

    //description of the dictionary
    private String mName;
    private String mDescription;
    private String mCatalogueName;
    private DictionaryDBHelper mDBHelper;

    //structure that store the elements


    /**
     * Constructor
     * @param name NMame of the dictionary
     * @param description Description of the disctionary
     */
    public Dictionary(String name, String description, String catalogueName, DictionaryDBHelper dbHelper){
        mDescription = description;
        mName = name;
        mCatalogueName = catalogueName;
        mDBHelper = dbHelper;
    }


    /**
     * Add object value associated to key to the current dictionary
     * @param value object to insert
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    public int addDictionaryObject(DictionaryObject value){

        //if key or value is empty
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues val = value.toContentValues();
        val.put(DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME, mCatalogueName);
        val.put(DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME, mName);
        if (db.insert(TABLE_NAME, null, val) == -1)
            return Global.FAILURE;
        return Global.SUCCESS;
    }


    public int removeDictionaryObject(String name){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sqlclause = DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME + "= '" + mName +"'";
        sqlclause += " and " + DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME + "= '" + mCatalogueName+"'";
        sqlclause += " and " + DictionaryContract.Dictionary.COLUMN_NAME_WORD + "= '" + name +"'";
        return db.delete(TABLE_NAME, sqlclause, null);
    }


    /**
     * Remove current dictionary and all words in database
     * @return {Global.SUCCESS} if successfull, {Global.NOT_FOUND} otherwise
     */
    public int clear(){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sqlclause = DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME + "= '" + mName+"'";
        sqlclause += " and " + DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME + "= '" + mCatalogueName+"'";
        return db.delete(TABLE_NAME, sqlclause, null);
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDBHelper(DictionaryDBHelper helper){
        mDBHelper = helper;
    }
}
