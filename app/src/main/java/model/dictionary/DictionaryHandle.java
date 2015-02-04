package model.dictionary;

import model.dictionary.dictionaryObjects.DictionaryObject;


/**
 * Created by pietro on 30/01/15.
 * Class that handle the whole dictionary. Typically contains a struct(map) with
 * the list of all elements as couple <string key, DictionaryObject object>
 * Persist this class
 */
public class DictionaryHandle {

    private Dictionary m_oDictionary;

    /**
     * Constructor
     * @param dictionary Dictionary to manage
     */
    DictionaryHandle(Dictionary dictionary){
        m_oDictionary = dictionary;
    }

    /**
     * Get the dictionary's object associated to the key
     * @param  key Key used for getting the object
     * @return the object if exists, null otherwise
     */
    public DictionaryObject getDictionaryObject(String key){
        return m_oDictionary.getDictionaryObject(key);
    }

    /**
     * Add object value associated to key to the current dictionary
     * @param key object's key
     * @param value object to insert
     * @param merge if true and key already exists in the dictionary, merge the existing object with the given one
     * @return
     */
    public int addDictionaryObject(String key, DictionaryObject value, Boolean merge){
        return m_oDictionary.addDictionaryObject(key, value, merge);
    }

    public int getDictionarySize(){
        return m_oDictionary.size();
    }
}
