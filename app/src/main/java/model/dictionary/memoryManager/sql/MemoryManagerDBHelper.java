package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.Global;


/**
 * Created by pietro on 18/03/15.
 *
 * SQL dbHelper for MemoryManager class
 *
 * Contains 3 tables: MEMORY_MANAGER,  MEMORY_PHASE_ID and MEMORY_MONITORING
 * We use it
 * @TODO: WRITE COMMENTS OF MemoryManagerDBHelper
 */
public class MemoryManagerDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VESRION = 1;
    private static final String DATABASE_NAME = "memorymanager.db";


    private static final String SQL_CREATE_MEMORY_MANAGER_TABLE =
            "CREATE TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME + "( "
            + MemoryManagerContract.MemoryManager._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + Global.TEXT_TYPE + " UNIQUE " + Global.COMMASEP
            + MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST + Global.TEXT_TYPE
            + ")"
            ;

    private static final String SQL_CREATE_MEMORY_PHASE_TABLE =
            "CREATE TABLE " + MemoryManagerContract.MemoryPhase.TABLE_NAME + "("
            + MemoryManagerContract.MemoryPhase._ID + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryPhase.PHASE_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryPhase.DURATION_PHASE + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryPhase.FIRST_PERIOD + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryPhase.PERIOD_INCREMENT + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID +  Global.INTEGER_TYPE + Global.COMMASEP
            + " FOREIGN KEY(" + MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID + ") REFERENCES " + MemoryManagerContract.MemoryPhase.TABLE_NAME + "(" + MemoryManagerContract.MemoryPhase._ID+ ")"
            + ")"
            ;

    private static final String SQL_CREATE_MEMORY_MONITORING_TABLE =
            "CREATE TABLE " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME + "("
            + MemoryManagerContract.MemoryMonitoring._ID + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.LAST_LEARNT + Global.TEXT_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT + Global.TEXT_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.DATE_ADDED + Global.TEXT_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID + Global.INTEGER_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP + Global.TEXT_TYPE + Global.COMMASEP
            + MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN + Global.INTEGER_TYPE + Global.COMMASEP
            + " FOREIGN KEY(" + MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID + ") REFERENCES " + MemoryManagerContract.MemoryPhase.TABLE_NAME + "(" + MemoryManagerContract.MemoryPhase._ID+ ")"
            + ")";
            ;


    public MemoryManagerDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VESRION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MEMORY_MANAGER_TABLE);
        db.execSQL(SQL_CREATE_MEMORY_PHASE_TABLE);
        populateDefaultMemoryPhase(db); // populate the table
        db.execSQL(SQL_CREATE_MEMORY_MONITORING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //@todo find what to do in onUpgrade db
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryPhase.TABLE_NAME);
        onCreate(db);
    }

    private void populateDefaultMemoryPhase(SQLiteDatabase db){
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryPhase.PHASE_NAME, "UPKEEPING");
        value.put(MemoryManagerContract.MemoryPhase.DURATION_PHASE, 0);
        value.put(MemoryManagerContract.MemoryPhase.FIRST_PERIOD, 62);
        value.put(MemoryManagerContract.MemoryPhase.PERIOD_INCREMENT, 0);
        long id = db.insert(MemoryManagerContract.MemoryPhase.TABLE_NAME, null, value);
        value = new ContentValues();
        value.put(MemoryManagerContract.MemoryPhase.PHASE_NAME, "STORING");
        value.put(MemoryManagerContract.MemoryPhase.DURATION_PHASE, 95);
        value.put(MemoryManagerContract.MemoryPhase.FIRST_PERIOD, 5);
        value.put(MemoryManagerContract.MemoryPhase.PERIOD_INCREMENT, 1);
        value.put(MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID, id);
        id = db.insert(MemoryManagerContract.MemoryPhase.TABLE_NAME, null, value);
        value = new ContentValues();

        // this is the first phase, phase name should be a global constant variable because we use it for dictionary object creation
        value.put(MemoryManagerContract.MemoryPhase.PHASE_NAME, Global.FIRST_PHASE_NAME);
        value.put(MemoryManagerContract.MemoryPhase.DURATION_PHASE, 5);
        value.put(MemoryManagerContract.MemoryPhase.FIRST_PERIOD, 1);
        value.put(MemoryManagerContract.MemoryPhase.PERIOD_INCREMENT, 0);
        value.put(MemoryManagerContract.MemoryPhase.NEXT_PHASE_ID, id);
        db.insert(MemoryManagerContract.MemoryPhase.TABLE_NAME, null, value);
    }

}
