package model.dictionary.memory;

import android.content.Context;

import java.util.Calendar;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.MnemoCalendar;

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
    public int onLearnt(MemoryObject object){
        if (object == null)
            return Global.NOT_FOUND;

        if (object.getNextLearn() <= MnemoCalendar.getInstance().getTimeInMillis()){
            //we implement a strict update of the phase, will change later
            //we just check if the begin date of the phase + number of days of this phase is gt or equal to today's date
            /*Calendar cal = MnemoCalendar.getInstance();
            cal.setTime(object.getBeginningOfMP());
            cal.add(Calendar.DAY_OF_YEAR, object.getDurationPhase());

            // compare with today's date, if we are not in last phase
            if (cal.compareTo(MnemoCalendar.getInstance()) <= 0)
                //we go to the next phase
                object.updateToNextPhase();
                //increment in this phase
            else
                object.incrementDaysInPhaseAndUpdateLearningDates();*/

            Calendar endOfPhase = MnemoCalendar.getInstance();
            endOfPhase.setTime(object.getBeginningOfMP());
            endOfPhase.add(Calendar.DAY_OF_YEAR, object.getDurationPhase());

            // compare with today's date, if we are not in last phase
            if (object.getDurationPhase() != 0 && endOfPhase.getTimeInMillis() < MnemoCalendar.getInstance().getTimeInMillis())
                //we go to the next phase
                object.updateToNextPhase();
                //increment in this phase
            else
                object.incrementDaysInPhaseAndUpdateLearningDates();

            //update object in database
            int res = MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(object);

            if (res != Global.SUCCESS)
                return Global.FAILURE;

            return Global.SUCCESS;
        }

        return Global.NOTHING_DONE;
    }

    @Override
    public int onLearnt(long dictionaryObjectID){
        if (dictionaryObjectID < 0)
            return Global.BAD_PARAMETER;
        return onLearnt(DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(dictionaryObjectID));
    }

}
