package model.dictionary.dictionary;

import android.content.ContentValues;

import java.util.Date;


/**
 * Created by pietro on 30/01/15.
 * DictionaryObject is an object associated to a key in the dictionary
 * For example, in a classic dictionary, the key would be the word, while DictionaryObject would be the translation
 * But DictionaryObject can also be more complex, store links, pictures or audio description
 */
public abstract class DictionaryObject {

    //object content
    private long mID; // Unique identifier of this object

    //memory management
    private Date lastTimeLearnt; // last time the user checked this object
    private Date nextTimeToLearn; // first time the user checked this object

    /**
     * Constructor
     * @param id object's identifier
     */
    DictionaryObject(long id){
        mID = id;
    }


    /**
     * Return type of the current object
     * @return enum Type of the dictionary object
     */
    public abstract DictionaryObjectType getType();

    public abstract ContentValues toContentValues();

}