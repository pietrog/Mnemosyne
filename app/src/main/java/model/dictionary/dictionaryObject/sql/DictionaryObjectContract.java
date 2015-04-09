package model.dictionary.dictionaryObject.sql;

import android.provider.BaseColumns;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.memoryManager.sql.MemoryManagerContract;

/**
 * Created by pietro on 08/04/15.
 */
public class DictionaryObjectContract {

    public static abstract class DictionaryObject implements BaseColumns{
        public static final String TABLE_NAME = "dictionaryObjects";
        public static final String CID = TABLE_NAME+"."+_ID;

        public static final String DICTIONARYID = "dictionaryID";
        public static final String MEMORY_MONITORING_ID = "memoryMonitoringID"; // phase of the memory cycle
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DictionaryObject.TABLE_NAME + " ( "
                    + DictionaryObject._ID + " INTEGER PRIMARY KEY" + Global.COMMASEP
                    + DictionaryObject.DICTIONARYID + Global.INTEGER_TYPE + " REFERENCES "+ DictionaryContractBase.DictionaryBase.TABLE_NAME + "(" + DictionaryContractBase.DictionaryBase._ID + ") ON DELETE CASCADE " + Global.COMMASEP
                    + DictionaryObject.MEMORY_MONITORING_ID + Global.INTEGER_TYPE + " REFERENCES "+ MemoryManagerContract.MemoryMonitoring.TABLE_NAME +" ON DELETE CASCADE"
                    + " )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + DictionaryObject.TABLE_NAME;

}
