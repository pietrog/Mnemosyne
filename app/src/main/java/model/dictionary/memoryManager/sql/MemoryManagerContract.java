package model.dictionary.memoryManager.sql;

import android.database.Cursor;
import android.provider.BaseColumns;

import model.dictionary.memoryManager.MemoryManager;

/**
 * Created by pietro on 18/03/15.
 */
public class MemoryManagerContract {

    public MemoryManagerContract(){}

    public static abstract class MemoryManager implements BaseColumns{
        public static final String TABLE_NAME = "memorymanager";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IDLIST = "idlist";
    }


    public static String getIDList (Cursor cursor){
        if (cursor == null)
            return "";

        return cursor.getString(cursor.getColumnIndex(MemoryManager.COLUMN_NAME_IDLIST));
    }


}
