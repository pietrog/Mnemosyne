package model.dictionary.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import model.dictionary.Global;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionaryContract;
import model.dictionary.dictionary.sql.DictionaryDBHelper;

/**
 * Created by pietro on 30/01/15.
 */
public class Dictionary {

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
    }

    /**
     * Get the dictionary's object associated to the key
     * @param  key Key used for getting the object
     * @return the object if exists, null otherwise
     */
    public DictionaryObject getDictionaryObject(String key){
        return null;
    }

    /**
     * Add object value associated to key to the current dictionary
     * @param value object to insert
     * @return
     */
    public int addDictionaryObject(DictionaryObject value){

        //if key or value is empty
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues val = value.toContentValues();
        val.put(DictionaryContract.Dictionary.COLUMN_NAME_ID, mCatalogueName+"_"+mName);
        if (db.insert(DictionaryContract.Dictionary.TABLE_NAME, null, val) == -1)
            return Global.FAILURE;
        return Global.SUCCESS;
    }



    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCatalogueName() {
        return mCatalogueName;
    }

    public void setDBHelper(DictionaryDBHelper helper){
        mDBHelper = helper;
    }
}
