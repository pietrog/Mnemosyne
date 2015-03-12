package model.dictionary.dictionary.sql;

import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by pietro on 06/03/15.
 */
public class DictionaryContract {

    public DictionaryContract() {}

    public static abstract class Dictionary implements BaseColumns{
        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_NAME_CATALOGUE_NAME = "catalogue";
        public static final String COLUMN_NAME_DICTIONARY_NAME = "dictionary";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_DEFINITION = "definition";
    }


    /**
     *
     */
    public static String getWord (Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(Dictionary.COLUMN_NAME_WORD));
    }
    public static String getDefinition (Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(Dictionary.COLUMN_NAME_DEFINITION));
    }
    public static String getCatalogueName (Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(Dictionary.COLUMN_NAME_CATALOGUE_NAME));
    }
    public static String getDictionaryName (Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(Dictionary.COLUMN_NAME_DICTIONARY_NAME));
    }
}
