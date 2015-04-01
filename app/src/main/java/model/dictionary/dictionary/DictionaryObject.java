package model.dictionary.dictionary;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.GeneralTools;


/**
 * Created by pietro on 30/01/15.
 * DictionaryObject is an object associated to a key in the dictionary
 * For example, in a classic dictionary, the key would be the word, while DictionaryObject would be the translation
 * But DictionaryObject can also be more complex, store links, pictures or audio description
 */
public class DictionaryObject {

    //object content
    private long mID; // Unique identifier of this object
    private String mCatalogueName;
    private String mDictionaryName;



    private long mMemoryMonitoringID;


    private MemoryMonitoringObject mMemoryMonitoring;

    /**
     * Constructor for a dictionary, only used with extending classes
     * @param id sql id id -1, we do not get this object from sql
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     */
    protected DictionaryObject(long id, String catalogueName, String dictionaryName){
        mID = id;
        mCatalogueName = catalogueName;
        mDictionaryName = dictionaryName;
    }

    /**
     * Constructor used for Memory Management
     * @param id object id
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     * @param monitor memory monitoring object
     */
    public DictionaryObject(long id, String catalogueName, String dictionaryName, long memoryMonitoringID, MemoryMonitoringObject monitor){
        mID = id;
        mCatalogueName = catalogueName;
        mDictionaryName = dictionaryName;
        mMemoryMonitoringID = memoryMonitoringID;
        mMemoryMonitoring = monitor;

        /**
         * CASE FOR A NEW WORD HERE
         * if memory monitoring is still not built, we build it here
         * after this, object is in the normal cycle
         */
        if (mMemoryMonitoring == null){
            mMemoryMonitoring = new MemoryMonitoringObject();
            mMemoryMonitoring.mMemoryPhaseID = mIDFirstPhase;
            mMemoryMonitoring.mDateAdded = Calendar.getInstance().getTime();
            mMemoryMonitoring.mBeginningOfMP = mMemoryMonitoring.mDateAdded;
            mMemoryMonitoring.mDaysBetween = 0;
            mMemoryMonitoring.updateToNextPhase();
        }
    }

    /**
     * Return type of the current object
     * @return enum Type of the dictionary object
     */
    public DictionaryObjectType getType() {
        return null;
    };

    /**
     * Get the object in ContentValues container, for sql. Method to override in all subclasses
     * @return ContentValues of this object
     */
    public ContentValues toContentValues(){
        ContentValues value = new ContentValues();
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME, mCatalogueName);
        value.put(DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME, mDictionaryName);
        if (mMemoryMonitoringID != -1)
            value.put(DictionaryContractBase.DictionaryBase.MEMORY_MONITORING_ID, mMemoryMonitoringID);
        return value;
    }

    /**
     * ToString method
     * @return string representation of the object
     */
    public String toString(){
        String res = "DictionaryObject : ID "+ mID + ", catalogue: " + mCatalogueName + ", dictionary: " + mDictionaryName ;
        if (mMemoryMonitoring != null)
            res += mMemoryMonitoring.toString();
        return res;
    }


    /**
     * GETTER
     */
    public long getID() {
        return mID;
    }

    public String getCatalogueName() {
        return mCatalogueName;
    }

    public String getDictionaryName() {
        return mDictionaryName;
    }

    public MemoryMonitoringObject getMemoryMonitoring() {
        return mMemoryMonitoring;
    }

    public long getMemoryMonitoringID() {
        return mMemoryMonitoringID;
    }

    public void setMemoryMonitoringID(long memoryMonitoringID) {
        mMemoryMonitoringID = memoryMonitoringID;
    }


    /**
     * STATIC SECTION
     */
    public static Map<Integer, MemoryPhaseObject> mMemoryPhaseObjectMap;
    public static boolean mHasBeenInitialized = false;
    public static long mIDFirstPhase;

    public static boolean initMemoryPhaseMap(){
        mHasBeenInitialized = MemoryManagerSQLManager.getInstance().initMemoryPhaseMap(mMemoryPhaseObjectMap) == Global.SUCCESS;
        if (mHasBeenInitialized)
            for(Integer current: mMemoryPhaseObjectMap.keySet())
                if (mMemoryPhaseObjectMap.get(current).mPhaseName.compareTo(Global.FIRST_PHASE_NAME) == 0) {
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
        public int mNextPhaseID = -1;

        public MemoryPhaseObject(String phaseName, int durationPhase, int firstPeriod, int periodIncrement, int nextPhaseID){
            mPhaseName = phaseName;
            mDurationPhase = durationPhase;
            mFirstPeriod = firstPeriod;
            mPeriodIncrement = periodIncrement;
            mNextPhaseID = nextPhaseID;
        }
    }

    /**
     * Memory Monitoring nested class
     */
    public static class MemoryMonitoringObject{
        public Date mLastLearnt;
        public Date mNextLearnt;
        public Date mDateAdded;
        public long mMemoryPhaseID;
        public Date mBeginningOfMP;
        public int  mDaysBetween;

        public boolean mLearningSessionsModified = false;

        /**
         * Increment days between sessions and update learning dates
         */
        public void incrementDaysInPhaseAndUpdateLearningDates(){
            mDaysBetween += DictionaryObject.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mPeriodIncrement;
            Calendar next = Calendar.getInstance();
            next.add(Calendar.DAY_OF_YEAR, mDaysBetween);

            mLastLearnt = Calendar.getInstance().getTime();
            mNextLearnt = next.getTime();
            mLearningSessionsModified = true;
        }

        public void updateToNextPhase(){
            long nextid = DictionaryObject.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mNextPhaseID;
            mMemoryPhaseID = nextid != -1 ? nextid : mMemoryPhaseID;
        }

        public int getDurationPhase(){
            return DictionaryObject.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mDurationPhase;
        }

        public ContentValues toContentValues(){
            ContentValues value = new ContentValues();
            value.put(MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP, GeneralTools.getSQLDate(mBeginningOfMP));
            value.put(MemoryManagerContract.MemoryMonitoring.DATE_ADDED, GeneralTools.getSQLDate(mDateAdded));
            value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT, GeneralTools.getSQLDate(mNextLearnt));
            value.put(MemoryManagerContract.MemoryMonitoring.LAST_LEARNT, GeneralTools.getSQLDate(mLastLearnt));
            value.put(MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN, mDaysBetween);
            value.put(MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID, mMemoryPhaseID);
            return value;
        }
    }

}