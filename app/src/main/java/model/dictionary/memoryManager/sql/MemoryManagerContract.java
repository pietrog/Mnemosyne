package model.dictionary.memoryManager.sql;

import android.database.Cursor;
import android.provider.BaseColumns;


/**
 * Created by pietro on 18/03/15.
 * SQL contract for MemoryManager
 */
public class MemoryManagerContract {

    public MemoryManagerContract(){}

    public static abstract class MemoryManager implements BaseColumns{
        public static final String TABLE_NAME = "memorymanager";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IDLIST = "idlist";
    }

}
