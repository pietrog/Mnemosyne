package model.dictionary.memoryManager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by pietro on 18/03/15.
 *
 * SQL dbHelper for MemoryManager class
 */
public class MemoryMNanagerDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VESRION = 1;
    private static final String DATABASE_NAME = "memorymanager.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMASEP = " ,";


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME + "( "
            + MemoryManagerContract.MemoryManager._ID + " INTEGER PRIMARY KEY" + COMMASEP
            + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + TEXT_TYPE + COMMASEP
            + MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST + TEXT_TYPE + COMMASEP
            + ")"
            ;

    private static final String SQL_DROP_TABLE = "DROP TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME;


    public MemoryMNanagerDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VESRION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //@todo find what to do in onUpgrade db
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
