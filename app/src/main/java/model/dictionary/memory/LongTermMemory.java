package model.dictionary.memory;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.DictionaryObject;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;

/**
 * Created by pietro on 30/03/15.
 *
 * Manage to add a word in the long term memory. Depends on the memory phase, added date, ...
 */
public class LongTermMemory implements IMemorisation{

    private static LongTermMemory instance = null;

    public synchronized static LongTermMemory getInstance(Context context){
        if (instance == null)
            instance = new LongTermMemory(context);

        return instance;
    }

    private Context mContext;

    private LongTermMemory(Context context){
        mContext = context;
    }


    @Override
    public Date computeNextLearningDate(DictionaryObject object) {
        if (object == null)
            return null;

        Date res = new Date();



        return res;
    }

    @Override
    public Date postponeLearningDate(DictionaryObject object) {
        return null;
    }

    @Override
    public int updateMemorisationPhase(MemoryObject object) {

        //fix the lastlearnt date for removing id from list of id to raise
        Date oldNext = object.getNextLearnt();
        //we implement a strict update of the phase, will change later
        //we just check if the begin date of the phase + number of days of this phase is gt or equal to today's date
        Calendar cal = Calendar.getInstance();
        cal.setTime(object.getBeginningOfMP());
        cal.add(Calendar.DAY_OF_YEAR, object.getDurationPhase());

        // compare with today's date, if we are not in last phase
        if (cal.compareTo(Calendar.getInstance()) <= 0)
            //we go to the next phase
            object.updateToNextPhase();
        //increment in this phase
        else
            object.incrementDaysInPhaseAndUpdateLearningDates();

        //update object in database
        int res = MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(object);

        if (res != Global.SUCCESS)
            return Global.FAILURE;

        if (MemoryManagerSQLManager.getInstance().addWordToLearnSession(object.getDictionaryObjectID(), object.getNextLearnt()) > 0)
            return Global.SUCCESS;

        return Global.FAILURE;
    }


}
