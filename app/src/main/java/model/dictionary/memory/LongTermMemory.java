package model.dictionary.memory;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import model.dictionary.dictionary.DictionaryObject;
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
    public void updateMemorisationPhase(DictionaryObject object) {
        //we implement a strict update of the phase, will change later
        //we just check if the begin date of the phase + number of days of this phase is gt or equal to today's date
        Calendar cal = Calendar.getInstance();
        cal.setTime(object.getMemoryMonitoring().mBeginningOfMP);
        cal.add(Calendar.DAY_OF_YEAR, object.getMemoryMonitoring().getDurationPhase());

        // compare with today's date, if we are not in last phase
        if (cal.compareTo(Calendar.getInstance()) <= 0)
            //we go to the next phase
            object.getMemoryMonitoring().updateToNextPhase();
        //increment in this phase
        else
            object.getMemoryMonitoring().incrementDaysInPhaseAndUpdateLearningDates();

        //update object in database
        MemoryManagerSQLManager.getInstance().updateDictionaryObjectInDB(object);
    }


}
