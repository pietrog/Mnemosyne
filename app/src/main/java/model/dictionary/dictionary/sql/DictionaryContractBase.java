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
        public static final String MEMORY_MONITORING_ID = "memoryMonitoringID"; // phase of the memory cycle
    }

}
