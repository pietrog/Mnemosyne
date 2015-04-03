package model.dictionary.dictionary.sql;

import android.database.Cursor;
import android.provider.BaseColumns;

import model.dictionary.Global;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
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


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DictionaryBase.TABLE_NAME + " ( "
                    + DictionaryBase._ID + " INTEGER PRIMARY KEY" + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_WORD + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DEFINITION + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.MEMORY_MONITORING_ID + Global.INTEGER_TYPE + Global.COMMASEP
                    + " UNIQUE ( " + DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + Global.COMMASEP + DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + Global.COMMASEP + DictionaryBase.COLUMN_NAME_WORD +")" + Global.COMMASEP
                    + " FOREIGN KEY(" + DictionaryBase.MEMORY_MONITORING_ID + ") REFERENCES "+ MemoryManagerContract.MemoryMonitoring.TABLE_NAME +" ON DELETE CASCADE"
                    + " )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + DictionaryBase.TABLE_NAME;


}
