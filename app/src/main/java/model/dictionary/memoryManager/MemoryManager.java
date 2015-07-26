package model.dictionary.memoryManager;


import java.util.HashMap;
import java.util.Map;

import model.dictionary.Global;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;

/**
 * @author Pierre Gaulard
 * Class used to manage the memory cycle for all the dictionaries objects
 *
 */
public class MemoryManager {


    public MemoryManager (){    }


    /**
     * STATIC SECTION
     */
    public static Map<Long, MemoryPhaseObject> mMemoryPhaseObjectMap = new HashMap<>();
    public static boolean mHasBeenInitialized = false;
    public static long mIDFirstPhase;

    public static synchronized boolean initMemoryPhaseMap(){
        mHasBeenInitialized = MemoryManagerSQLManager.getInstance().initMemoryPhaseMap(mMemoryPhaseObjectMap) == Global.SUCCESS;
        if (mHasBeenInitialized)
            for(Long current: mMemoryPhaseObjectMap.keySet())
                if (mMemoryPhaseObjectMap.get(current).mPhaseName.compareTo(Global.PHASE_NAME_P1) == 0) {
                    mIDFirstPhase = current;
                    break;
                }
        return mHasBeenInitialized;
    }

    /**
     * Memory phase nested class
     */
    public static class MemoryPhaseObject{

        public String mPhaseName;
        public int mDurationPhase;
        public int mFirstPeriod;
        public int mPeriodIncrement;
        public long mNextPhaseID = -1;
        public long mID;

        public MemoryPhaseObject(String phaseName, int durationPhase, int firstPeriod, int periodIncrement, long nextPhaseID){
            mPhaseName = phaseName;
            mDurationPhase = durationPhase;
            mFirstPeriod = firstPeriod;
            mPeriodIncrement = periodIncrement;
            mNextPhaseID = nextPhaseID;
        }
    }



    public class MemoryPhase{
        public static final int BeginLearning = 1;
        public static final int Learning = 2;
        public static final int KeepInMemory = 3;
        public static final String sBeginLearning = "BeginLearning";
        public static final String sLearning = "BeginLearning";
        public static final String sKeepInMemory = "BeginLearning";


        public String toString(int memoryPhase){
            switch (memoryPhase){
                case BeginLearning:
                    return sBeginLearning;
                case Learning:
                    return sLearning;
                case KeepInMemory:
                    return sKeepInMemory;
                default:
                    return "ERROR";
            }
        }
        public int fromString(String memoryPhase){
            if (memoryPhase.compareTo(sBeginLearning) == 0)
                return BeginLearning;
            if (memoryPhase.compareTo(sLearning) == 0)
                return Learning;
            if (memoryPhase.compareTo(sKeepInMemory) == 0)
                return KeepInMemory;

            return -1;
        }
    }

}
