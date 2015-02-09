package model.dictionary;

import model.dictionary.dictionaryObjects.DictionaryObject;


/**
 * Created by pietro on 30/01/15.
 * Class that handle the whole dictionary. Typically contains a struct(map) with
 * the list of all elements as couple <string key, DictionaryObject object>
 * Persist this class
 */
public class DictionaryHandle {

    private final Dictionary m_oDictionary ;
    private boolean m_bHasBeenModofied = false;


    /**
     * Constructor
     */
    DictionaryHandle(Dictionary dict)
    {
        if (dict == null)
            throw new ExceptionInInitializerError("Impossible to instantiate a DictionaryHandle without dictionary");
        m_oDictionary = dict;
    }

    /**
     *
     */
    private void initDictionaryHandle(){
        loadListOfDictionaries();
    }

    /**
     * METHODS FOR HANDLING DICTIONARY
     */

    /**
     * Get the dictionary's object associated to the key
     * @param  key Key used for getting the object
     * @return the object if exists, null otherwise
     */
    public DictionaryObject getDictionaryObject(String key){
        return m_oDictionary.getDictionaryObject(key);
    }


    /**
     * MEMORY IO
     */

    /**
     * Persist and update existing dictionaries
     */
    public int persistDictionary(){

        return 0;
    }

    public int loadListOfDictionaries(){
        return 0;
    }
}
