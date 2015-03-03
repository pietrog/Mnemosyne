package model.dictionary.dictionary;

import java.util.HashMap;

import model.dictionary.dictionary.DictionaryObject;

/**
 * Created by pietro on 30/01/15.
 */
public class Dictionary {

    //description of the dictionary
    private String mName;
    private String mDescription;

    //structure that store the elements
    private HashMap<String, DictionaryObject> m_hmContent = new HashMap<String, DictionaryObject>(0);


    /**
     * Constructor
     * @param name NMame of the dictionary
     * @param description Description of the disctionary
     */
    public Dictionary(String name, String description){
        mDescription = description;
        mName = name;
    }

    /**
     * Get the dictionary's object associated to the key
     * @param  key Key used for getting the object
     * @return the object if exists, null otherwise
     */
    public DictionaryObject getDictionaryObject(String key){
        return m_hmContent.get(key);
    }

    /**
     * Add object value associated to key to the current dictionary
     * @param key object's key
     * @param value object to insert
     * @param merge if true and key already exists in the dictionary, merge the existing object with the given one
     * @return
     */
    public int addDictionaryObject(String key, DictionaryObject value, Boolean merge){

        //if key or value is empty
        if (key.isEmpty() || value == null)
            return -1;

        //if key already exists
        if (m_hmContent.containsKey(key)){
            //if the user wants to insert this couple only if it is not existing
            if (!merge)
                return 1;
            //otherwise we merge the value of the existing one with the new one
            m_hmContent.get(key).mergeObjectWith(value);
        }
        else{
            m_hmContent.put(key, value);
        }

        return 0;
    }

    /**
     * Get the size of the dictionary
     * @return int size of the dictionary
     */
    public int size(){
        return m_hmContent.size();
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }
}
