package model.dictionary.dictionary.sql;

import android.database.Cursor;

import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 20/03/15.
 * SQL contract class for dictionary of word, extends DictionaryContractBase
 */
public class DictionaryOfWordContract extends DictionaryContractBase{


    public static abstract class DictionaryOfWord extends DictionaryBase {
    }

    /**
     * CURSOR GETTER/SETTER FOR COLUMNS
     */
    public static String getWord (Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryOfWord.COLUMN_NAME_WORD);
    }
    public static String getDefinition (Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryOfWord.COLUMN_NAME_DEFINITION);
    }

}
