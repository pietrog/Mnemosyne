package model.dictionary.dictionary;


import android.database.Cursor;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 30/01/15.
 * Dictionary class. Represent a dictionary, persisted in sql db. See CatalogueContract and CatalogueSQLManager
 */
public class Dictionary {

    //description of the dictionary
    private long mID;
    private String mDictionaryName;
    private String mDescription;
    private String mCatalogueName;

    public long getID() {
        return mID;
    }

    public String getName() {
        return mDictionaryName;
    }

    public String getDescription() {
        return mDescription;
    }

    /**
     * Constructor
     * @param id sql id
     * @param dictionaryName dictionary name
     * @param description description of the dictionary
     */
    public Dictionary(long id, String dictionaryName, String description){
        mID = id;
        mDictionaryName = dictionaryName;
        mDescription = description;
    }


    /**
     * Return a Dictionary object from a cursor
     * @param cursor cursor on catalogue table
     * @return Dictionary object
     */
    public static Dictionary LoadFromCursor(Cursor cursor){
        return new Dictionary(GeneralTools.getLongElement(cursor, DictionaryContractBase.DictionaryBase._ID),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.NAME),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.DESCRIPTION));
    }

}
