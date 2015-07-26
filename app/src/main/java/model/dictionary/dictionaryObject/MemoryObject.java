package model.dictionary.dictionaryObject;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoCalendar;

/**
 * Created by pietro on 16/06/15.
 */
public class MemoryObject extends DictionaryObject {

    private long mID;
    private Date mLastLearnt;
    private long mNextLearn;
    private Date mNextLearnDate;
    private Date mDateAdded;
    private long mMemoryPhaseID;
    private Date mBeginningOfMP;
    private int  mDaysBetween;

    public boolean mLearningSessionsModified = false;

    /**
     * Constructor from cursor. Extract all informations needed
     * @param cursor cursor containing sql information for MemoryObject
     */
    protected MemoryObject(Cursor cursor){
        super(cursor);
        mID = GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.CSID);
        mLastLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.LAST_LEARNT));
        setNext(GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN));
        mDateAdded = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.DATE_ADDED));
        mBeginningOfMP = GeneralTools.getDateFromSQLDate(GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP));
        mMemoryPhaseID = GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID);
        mDaysBetween = (int)GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN);
    }

    /**
     * Increment days between sessions and update learning dates
     */
    public void incrementDaysInPhaseAndUpdateLearningDates(){
        mDaysBetween += MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mPeriodIncrement;
        Calendar next = MnemoCalendar.getInstance();
        next.add(Calendar.DAY_OF_YEAR, mDaysBetween);

        mLastLearnt = GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(MnemoCalendar.getInstance().getTime()));
        setNext(next.getTimeInMillis());
        mLearningSessionsModified = true;
    }

    /**
     * Update the memory monitoring object to the next phase
     */
    public void updateToNextPhase(){
        Calendar time = MnemoCalendar.getInstance();
        //get the next phase id (-1 if the phase is the last one, upkeeping)
        long nextid = MemoryManager.mMemoryPhaseObjectMap.get(mMemoryPhaseID).mNextPhaseID;

        //phase n to phase n+1
        if (nextid != -1) {
            mMemoryPhaseID = nextid;
            mDaysBetween = MemoryManager.mMemoryPhaseObjectMap.get(nextid).mFirstPeriod;
            mBeginningOfMP = time.getTime();
            mLastLearnt = mBeginningOfMP;
            time.add(Calendar.DAY_OF_YEAR, mDaysBetween);
            setNext(time.getTimeInMillis());
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

    public long getNextLearn(){
        return mNextLearn;
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

    public long getMemoryObjectID(){
        return mID;
    }

    @Override
    public DictionaryObjectType getType() {
        return DictionaryObjectType.MemoryObject;
    }

    public ContentValues memoryObjectToContentValues(){
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryMonitoring.BEGINING_OF_MP, GeneralTools.getSQLDate(mBeginningOfMP));
        value.put(MemoryManagerContract.MemoryMonitoring.DATE_ADDED, GeneralTools.getSQLDate(mDateAdded));
        value.put(MemoryManagerContract.MemoryMonitoring.NEXT_LEARN, mNextLearn);
        value.put(MemoryManagerContract.MemoryMonitoring.LAST_LEARNT, GeneralTools.getSQLDate(mLastLearnt));
        value.put(MemoryManagerContract.MemoryMonitoring.DAYS_BETWEEN, mDaysBetween);
        value.put(MemoryManagerContract.MemoryMonitoring.MEMORY_PHASE_ID, mMemoryPhaseID);
        return value;
    }

    public static MemoryObject LoadFromCursor(Cursor cursor){
        return new MemoryObject(cursor);
    }

    @Override
    public String toString(){
        String res = super.toString();
        res += " Date last learn: " + mLastLearnt != null ? mLastLearnt.toString() : "";
        res += " Date next learn: " + mNextLearn ;
        res += " Date added date: " + mDateAdded != null ? mDateAdded.toString() : "";
        res += " Memory phase id: " + mMemoryPhaseID;
        res += " Date beginning of memory phase: " + mBeginningOfMP != null ? mBeginningOfMP.toString() : "";
        res += " Days between: " + mDaysBetween;
        return res;
    }


    //*************************************************************************
    //**************************TEST PURPOSE*********************************//
    //*************************************************************************
    public void setNext(long next){
        mNextLearn = next;
        mNextLearnDate = GeneralTools.getDateFromSQLDate(mNextLearn);
    }
    public void setmLastLearnt(Date last){
        mLastLearnt = last;
    }
    public void setDateAdded(Date added){
        mDateAdded = added;
    }
    public void setBegMP(Date date){
        mBeginningOfMP = date;
    }
}
