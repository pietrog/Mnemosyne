package model.dictionary.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionaryDBHelper;
import model.dictionary.dictionary.sql.DictionaryOfWordContract;
import model.dictionary.tools.Logger;

/**
 * Created by pietro on 30/01/15.
 *
 */
public class Dictionary {

    private static final String TABLE_NAME = DictionaryContractBase.DictionaryBase.TABLE_NAME;

    //description of the dictionary
    private String mName;
    private String mDescription;
    private String mCatalogueName;
    private SQLDictionary mDictionaryPersitence;

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
        mDictionaryPersitence = new SQLDictionary(dbHelper);
    }


    /**
     * Add object value associated to key to the current dictionary
     * @param value object to insert
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    public long addDictionaryObject(DictionaryObject value){
        long res = mDictionaryPersitence.addObject(mCatalogueName, mName, value);

        return res;
    }


    public int removeDictionaryObject(String name){
        return mDictionaryPersitence.removeObject(mCatalogueName, mName, name);
    }


    /**
     * Remove all words in database of current dictionary
     * @return {Global.SUCCESS} if successfull, {Global.NOT_FOUND} otherwise
     */
    public int clear(){
        return mDictionaryPersitence.clearAll(mCatalogueName, mName);
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDBHelper(DictionaryDBHelper helper){
        mDictionaryPersitence.setDBHelper(helper);
    }


    private class SQLDictionary{

        private DictionaryDBHelper mDBHelper = null;
        private SQLiteDatabase mDB;

        public SQLDictionary(DictionaryDBHelper dbHelper){
            setDBHelper(dbHelper);
        }

        public void setDBHelper(DictionaryDBHelper dbHelper){
            if (dbHelper != null && mDBHelper == null){
                mDBHelper = dbHelper;
                mDB = mDBHelper.getWritableDatabase();
            }
        }

        private boolean isDBReady(){
            boolean res = (mDB != null && mDB.isOpen());
            Logger.w("SQLDictionary::isDBReady", " mDB is not ready");
            return res;
        }


        public long addObject(String catalogueName, String dictionaryName, DictionaryObject value){
            ContentValues val = value.toContentValues();
            val.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME, catalogueName);
            val.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME, dictionaryName);
            return mDB.insert(TABLE_NAME, null, val);
        }
        public int removeObject(String catalogueName, String dictionaryName, String objectName){
            String sqlclause = DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + "= '" + dictionaryName +"'";
            sqlclause += " and " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + "= '" + catalogueName+"'";
            sqlclause += " and " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_WORD + "= '" + objectName +"'";
            return mDB.delete(TABLE_NAME, sqlclause, null);
        }


        public DictionaryObject getObjectFromID(int _id){
            if (!isDBReady())
                return null;

            String sqlclause = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + DictionaryContractBase.DictionaryBase._ID + " = " + _id;
            Cursor cursor = mDB.rawQuery(sqlclause, null);
            if (!cursor.moveToFirst())
                return null;

            DictionaryObject res= new WordDefinitionObj(1, DictionaryOfWordContract.getWord(cursor), DictionaryOfWordContract.getDefinition(cursor));
            return res;
        }

        public int clearAll(String catalogueName, String dictionaryName){
            String sqlclause = DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + "= '" + dictionaryName+"'";
            sqlclause += " and " + DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + "= '" + catalogueName+"'";
            return mDB.delete(TABLE_NAME, sqlclause, null);
        }
    }

}
