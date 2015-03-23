package model.dictionary.dictionary;

import android.content.ContentValues;

import java.util.Date;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.tools.GeneralTools;


/**
 * Created by pietro on 30/01/15.
 * DictionaryObject is an object associated to a key in the dictionary
 * For example, in a classic dictionary, the key would be the word, while DictionaryObject would be the translation
 * But DictionaryObject can also be more complex, store links, pictures or audio description
 */
public abstract class DictionaryObject {

    //object content
    private long mID; // Unique identifier of this object
    private String mCatalogueName;
    private String mDictionaryName;

    //memory management
    private Date mLastTimeLearnt; // last time the user checked this object
    private Date mNextTimeToLearn; // first time the user checked this object

    /**
     * Empty constructor. Used when we create a new dictionary object
     */
    protected DictionaryObject(){}

    protected DictionaryObject(long id, String catalogueName, String dictionaryName, String lastTimeLearnt, String nextTimeToLearn){
        mID = id;
        mCatalogueName = catalogueName;
        mDictionaryName = dictionaryName;
        mLastTimeLearnt = GeneralTools.getDateFromSQLDate(lastTimeLearnt);
        mNextTimeToLearn = GeneralTools.getDateFromSQLDate(nextTimeToLearn);
    }

    public long getID() {
        return mID;
    }

    public Date getLastTimeLearnt(){
        return mLastTimeLearnt;
    }

    public Date getNextTimeToLearn(){
        return mNextTimeToLearn;
    }

    /**
     * Return type of the current object
     * @return enum Type of the dictionary object
     */
    public abstract DictionaryObjectType getType();

    /**
     * Get the object in ContentValues container, for sql. Method to override in all subclasses
     * @return ContentValues of this object
     */
    public ContentValues toContentValues(){
        ContentValues value = new ContentValues();
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME, mCatalogueName);
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME, mDictionaryName);
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DATE_LAST_LEARNING, GeneralTools.getSQLDate(mLastTimeLearnt));
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DATE_NEXT_LEARNING, GeneralTools.getSQLDate(mNextTimeToLearn));
        return value;
    }

}