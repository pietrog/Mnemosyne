package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;


/**
 * Created by pietro on 18/03/15.
 * SQL contract for MemoryManager
 */
public class MemoryManagerContract {

    public MemoryManagerContract(){}

    public static abstract class MemoryManager implements BaseColumns{
        public static final String TABLE_NAME = "memorymanager";
        public static final String CID = TABLE_NAME + "." + _ID;
        public static final String DATE = "date";
        public static final String DICTIONARYOBJECTID = "dictionaryobjectid";
        public static final String DAYS_OF_DELAY = "daysdelay"; // delay in number of days from the initially computed next learning date
    }

    /**
     * Monitoring of the memory process for each dictionary object
     */
    public static abstract class MemoryMonitoring implements BaseColumns{
        public static final String TABLE_NAME = "memorymonitoring";
        public static final String CID = TABLE_NAME + "." + _ID;
        public static final String LAST_LEARNT = "last_learnt"; // date of the last learning session of this object
        public static final String NEXT_LEARNT = "next_learnt"; // date of the next learning session on this object
        public static final String DATE_ADDED = "dateadded"; // date word was added for the first time
        public static final String MEMORY_PHASE_ID = "memoryphase"; // the learning memory phase
        public static final String BEGINING_OF_MP = "endofmp"; // date of the beginning of the memory phase
        public static final String DAYS_BETWEEN = "daysbetween"; // current number of days between two learning sessions

    }

    /**
     * Configuration of the memorisation process, values defaulted, cannot be modified by user
     */
    public static abstract class MemoryPhase implements BaseColumns{
        public static final String TABLE_NAME = "memoryphase";
        public static final String CID = TABLE_NAME + "." + _ID;
        public static final String PHASE_NAME = "phasename";
        public static final String DURATION_PHASE = "durationphase"; // total duration of the phase (in days)
        public static final String FIRST_PERIOD = "firstPeriod"; // first period for the rappel of the object (in days)
        public static final String PERIOD_INCREMENT = "periodIncr"; // increment of the period (in days)
        public static final String NEXT_PHASE_ID = "nextphaseid"; // id of the next phase
    }

    public static final String SQL_CREATE_MEMORY_MANAGER_TABLE =
            "CREATE TABLE " + MemoryManager.TABLE_NAME + "( "
                    + MemoryManager._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
                    + MemoryManager.DATE + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryManager.DICTIONARYOBJECTID + Global.INTEGER_TYPE + " REFERENCES " + DictionaryObjectContract.DictionaryObject.TABLE_NAME + "(" + DictionaryObjectContract.DictionaryObject._ID + ") ON DELETE CASCADE " + Global.COMMASEP
                    + MemoryManager.DAYS_OF_DELAY + Global.INTEGER_TYPE + " DEFAULT 0 " + Global.COMMASEP
                    + " UNIQUE (" + MemoryManager.DICTIONARYOBJECTID + "," + MemoryManager.DATE +") ON CONFLICT ABORT"
                    + ")"
            ;

    public static final String SQL_CREATE_MEMORY_PHASE_TABLE =
            "CREATE TABLE " + MemoryPhase.TABLE_NAME + "("
                    + MemoryPhase._ID + Global.INTEGER_TYPE + " PRIMARY KEY " + Global.COMMASEP
                    + MemoryManagerContract.MemoryPhase.PHASE_NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryPhase.DURATION_PHASE + Global.INTEGER_TYPE + Global.COMMASEP
                    + MemoryPhase.FIRST_PERIOD + Global.INTEGER_TYPE + Global.COMMASEP
                    + MemoryPhase.PERIOD_INCREMENT + Global.INTEGER_TYPE + Global.COMMASEP
                    + MemoryPhase.NEXT_PHASE_ID +  Global.INTEGER_TYPE + Global.COMMASEP
                    + " FOREIGN KEY(" + MemoryPhase.NEXT_PHASE_ID + ") REFERENCES " + MemoryPhase.TABLE_NAME + "(" + MemoryManagerContract.MemoryPhase._ID+ ")"
                    + ")"
            ;

    public static final String SQL_CREATE_MEMORY_MONITORING_TABLE =
            "CREATE TABLE " + MemoryMonitoring.TABLE_NAME + "("
                    + MemoryMonitoring._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
                    + MemoryMonitoring.LAST_LEARNT + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryMonitoring.NEXT_LEARNT + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryMonitoring.DATE_ADDED + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryMonitoring.MEMORY_PHASE_ID + Global.INTEGER_TYPE + Global.COMMASEP
                    + MemoryMonitoring.BEGINING_OF_MP + Global.TEXT_TYPE + Global.COMMASEP
                    + MemoryMonitoring.DAYS_BETWEEN + Global.INTEGER_TYPE + Global.COMMASEP
                    + " FOREIGN KEY(" + MemoryMonitoring.MEMORY_PHASE_ID + ") REFERENCES " + MemoryPhase.TABLE_NAME + "(" + MemoryManagerContract.MemoryPhase._ID+ ")"
                    + ")";


    public static void populateDefaultMemoryPhase(SQLiteDatabase db){
        ContentValues value = new ContentValues();
        value.put(MemoryPhase.PHASE_NAME, Global.THIRD_PHASE_NAME);
        value.put(MemoryPhase.DURATION_PHASE, 0);
        value.put(MemoryPhase.FIRST_PERIOD, 62);
        value.put(MemoryPhase.PERIOD_INCREMENT, 0);
        long id = db.insert(MemoryPhase.TABLE_NAME, null, value);
        value = new ContentValues();
        value.put(MemoryPhase.PHASE_NAME, Global.SECOND_PHASE_NAME);
        value.put(MemoryPhase.DURATION_PHASE, 95);
        value.put(MemoryPhase.FIRST_PERIOD, 5);
        value.put(MemoryPhase.PERIOD_INCREMENT, 1);
        value.put(MemoryPhase.NEXT_PHASE_ID, id);
        id = db.insert(MemoryPhase.TABLE_NAME, null, value);
        value = new ContentValues();

        // this is the first phase, phase name should be a global constant variable because we use it for dictionary object creation
        value.put(MemoryPhase.PHASE_NAME, Global.FIRST_PHASE_NAME);
        value.put(MemoryPhase.DURATION_PHASE, 5);
        value.put(MemoryPhase.FIRST_PERIOD, 1);
        value.put(MemoryPhase.PERIOD_INCREMENT, 0);
        value.put(MemoryPhase.NEXT_PHASE_ID, id);
        db.insert(MemoryPhase.TABLE_NAME, null, value);
    }

}