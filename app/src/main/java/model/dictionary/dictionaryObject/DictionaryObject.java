package model.dictionary.dictionaryObject;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.memoryManager.MemoryManager;
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
    private long mID; // DictionaryObject ID
    private long mDictionaryID; // parent Dictionary ID

    private long mMemoryMonitoringID;


    private MemoryMonitoringObject mMemoryMonitoring;

    /**
     * Empty constructor used when we construct from descendant classes, no need to have all this part
     */
    protected DictionaryObject(){}

    /**
     * Constructor used for Memory Management
     * @param id object id
     * @param monitor memory monitoring object
     */
    protected DictionaryObject(long id, long dictionaryId, long memMonitoringId, MemoryMonitoringObject monitor){
        mID = id;
        mDictionaryID = dictionaryId;
        mMemoryMonitoringID = memMonitoringId;
        mMemoryMonitoring = monitor;
    }


    public static DictionaryObject LoadFromCursor(Cursor cursor){
        MemoryMonitoringObject monitor = new MemoryMonitoringObject(GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT)),
                GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARNT)),
                GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.DATE_ADDED)),
                GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP)),
                GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID),
                (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN));

        return new DictionaryObject(GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject._ID),
                GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.DICTIONARYID),
                GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID),
                monitor);
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
        return new ContentValues();
    }

    /**
     * ToString method
     * @return string representation of the object
     */
    public String toString(){
        String res = "DictionaryObject : ID "+ mID ;
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
     * Memory Monitoring nested class
     */
    public static class MemoryMonitoringObject{
        private long mID;
        protected Date mLastLearnt;
        protected Date mNextLearnt;
        protected Date mDateAdded;
        protected long mMemoryPhaseID;
        protected Date mBeginningOfMP;
        protected int  mDaysBetween;

        public boolean mLearningSessionsModified = false;

        /**
         * Constructor for a defaulted memory monitoring object
         */
        public MemoryMonitoringObject(){
            Calendar now = Calendar.getInstance();
            mMemoryPhaseID = MemoryManager.mIDFirstPhase;
            mLastLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(now.getTime()));
            mDateAdded = mLastLearnt;
            mBeginningOfMP = mLastLearnt;
            now.add(Calendar.DAY_OF_YEAR,MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod);
            mNextLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(now.getTime()));
            mDaysBetween = MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod;
        }

        protected MemoryMonitoringObject(Date lastLearnt, Date nextLearning, Date added, Date beginning, long memPhaseID, int daysBetween){
            mLastLearnt = lastLearnt;
            mNextLearnt = nextLearning;
            mDateAdded = added;
            mBeginningOfMP = beginning;
            mMemoryPhaseID = memPhaseID;
            mDaysBetween = daysBetween;
        }

        /**
         * Increment days between sessions and update learning dates
         */
        public void incrementDaysInPhaseAndUpdateLearningDates(){
            mDaysBetween += MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mPeriodIncrement;
            Calendar next = Calendar.getInstance();
            next.add(Calendar.DAY_OF_YEAR, mDaysBetween);

            mLastLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(Calendar.getInstance().getTime()));
            mNextLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(next.getTime()));
            mLearningSessionsModified = true;
        }

        /**
         * Update the memory monitoring object to the next phase
         */
        public void updateToNextPhase(){
            Calendar time = Calendar.getInstance();
            // new word, first phase
            if (mMemoryPhaseID == -1){
                mMemoryPhaseID = MemoryManager.mIDFirstPhase;
                mDateAdded = time.getTime();
                mBeginningOfMP = mDateAdded;
                mDaysBetween = MemoryManager.mMemoryPhaseObjectMap.get(MemoryManager.mIDFirstPhase).mFirstPeriod;
                mLastLearnt = mDateAdded;
                time.add(Calendar.DAY_OF_YEAR, mDaysBetween);
                mNextLearnt = time.getTime();
            }
            else {
                //get the next phase id (-1 if the phase is the last one, upkeeping)
                long nextid = MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mNextPhaseID;

                //phase n to phase n+1
                if (nextid != -1) {
                    mMemoryPhaseID = nextid;
                    mDaysBetween = MemoryManager.mMemoryPhaseObjectMap.get(nextid).mFirstPeriod;
                    mBeginningOfMP = time.getTime();
                    mLastLearnt = mBeginningOfMP;
                    time.add(Calendar.DAY_OF_YEAR, mDaysBetween);
                    mNextLearnt = time.getTime();
                }
            }
        }

        public int getDurationPhase(){
            return MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mDurationPhase;
        }

        public String getMemoryPhaseName(){
            return MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mPhaseName;
        }

        public long getMemoryPhaseID(){
            return mMemoryPhaseID;
        }

        public Date getLastLearnt(){
            return mLastLearnt;
        }

        public Date getNextLearnt(){
            return mNextLearnt;
        }

        public Date getBeginningOfMP(){
            return mBeginningOfMP;
        }

        public Date getDateAdded(){
            return mDateAdded;
        }

        public int getDaysBetween(){
            return mDaysBetween;
        }

        public long getID(){
            return mID;
        }
        public void setID(long id){
            mID = id;
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