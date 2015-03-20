package model.dictionary.dictionary.sql;

import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by pietro on 06/03/15.
 *
 */
public class DictionaryContract {

    public DictionaryContract() {}

    public static abstract class Dictionary implements BaseColumns{
        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_NAME_CATALOGUE_NAME = "catalogue";
        public static final String COLUMN_NAME_DICTIONARY_NAME = "dictionary";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_DEFINITION = "definition";
        public static final String COLUMN_NAME_DATE_NEXT_ALERT = "date_next_alert"; // date for the next alert on this object
        public static final String COLUMN_NAME_MEMORY_PHASE = "memory_phase"; // phase of the memory cycle
    }


    /**
     * CURSOR GETTER/SETTER
     */
    public static String getWord (Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_WORD);
    }
    public static String getDefinition (Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_DEFINITION);
    }
    public static String getCatalogueName (Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_CATALOGUE_NAME);
    }
    public static String getDictionaryName (Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_DICTIONARY_NAME);
    }
    public static String getDateNextAlert(Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_DATE_NEXT_ALERT);
    }
    public static String getMemoryPhase(Cursor cursor){
        return getColumnElement(cursor, Dictionary.COLUMN_NAME_MEMORY_PHASE);
    }

    private static String getColumnElement(Cursor cursor, String column){
        return cursor.getString(cursor.getColumnIndex(column));
    }
}
