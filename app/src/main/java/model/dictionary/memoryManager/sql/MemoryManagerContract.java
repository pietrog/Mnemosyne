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

    /**
     * Monitoring of the memory process for each dictionary object
     */
    public static abstract class MemoryMonitoring implements BaseColumns{
        public static final String TABLE_NAME = "memorymonitoring";
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
        public static final String PHASE_NAME = "phasename";
        public static final String DURATION_PHASE = "durationphase"; // total duration of the phase (in days)
        public static final String FIRST_PERIOD = "firstPeriod"; // first period for the rappel of the object (in days)
        public static final String PERIOD_INCREMENT = "periodIncr"; // increment of the period (in days)
        public static final String NEXT_PHASE_ID = "nextphaseid"; // id of the next phase
    }

}