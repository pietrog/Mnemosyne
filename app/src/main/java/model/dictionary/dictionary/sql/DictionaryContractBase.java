package model.dictionary.dictionary.sql;

import android.database.Cursor;
import android.provider.BaseColumns;

import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 06/03/15.
 *
 */
public class DictionaryContractBase {

    public DictionaryContractBase() {}

    public static abstract class DictionaryBase implements BaseColumns{
        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_NAME_CATALOGUE_NAME = "catalogue";
        public static final String COLUMN_NAME_DICTIONARY_NAME = "dictionary";

        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_DEFINITION = "definition";
        /**
         * MEMORY MANAGER DATAS
         */
        public static final String COLUMN_NAME_DATE_LAST_LEARNING = "date_last_learning"; // date of the last learning of this object
        public static final String COLUMN_NAME_DATE_NEXT_LEARNING = "date_next_learning"; // date of the next alert on this object for learning
        public static final String COLUMN_NAME_MEMORY_PHASE = "memory_phase"; // phase of the memory cycle
    }


    /**
     * CURSOR GETTER/SETTER FOR COLUMNS
     */

    public static String getCatalogueName (Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryBase.COLUMN_NAME_CATALOGUE_NAME);
    }
    public static String getDictionaryName (Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryBase.COLUMN_NAME_DICTIONARY_NAME);
    }
    public static String getDateLastLearning(Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryBase.COLUMN_NAME_DATE_LAST_LEARNING);
    }
    public static String getDateNextLearning(Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryBase.COLUMN_NAME_DATE_NEXT_LEARNING);
    }
    public static String getMemoryPhase(Cursor cursor){
        return GeneralTools.getStringElement(cursor, DictionaryBase.COLUMN_NAME_MEMORY_PHASE);
    }
    public static long getID(Cursor cursor){
        return GeneralTools.getLongElement(cursor, DictionaryBase._ID);
    }


}
