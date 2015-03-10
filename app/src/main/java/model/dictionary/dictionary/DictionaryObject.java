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
    private String str_desc; // the string description of the object

    //memory management
    private Date lastTimeChecked; // last time the user checked this object
    private Date firstTimeChecked; // first time the user checked this object

    /**
     * Constructor
     * @param desc string description of the object
     */
    DictionaryObject(String desc){
        str_desc = desc;
    }

    /**
     * Empty constructor, just return an empty object for type checking
     * Private, can be used only by internals methods like getEmptyForType
     */
    private DictionaryObject(){}


    /**
     * Merge this object with one given as parameter
     * @param toMerge object to merge with current object
     */
    public void mergeObjectWith(DictionaryObject toMerge){
        //TODO merge two objects together
    }

    /**
     * Get the string description of the object
     * @return string
     */
    public String getStrDesc(){
        return str_desc;
    }


    /**
     * Return type of the current object
     * @return enum Type of the dictionary object
     */
    public abstract DictionaryObjectType getType();

    public abstract ContentValues toContentValues();

}