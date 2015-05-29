package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Calendar;
import java.util.Vector;

import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.memory.LongTermMemory;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.Global.Couple;

public class MnemoMemoryManager extends IntentService {

    public static final String ACTION_RISE_TODAY_LIST = "RISETODAYLIST";
    public static final String ACTION_ADD_WORD = "ADDWORD";
    public static final String ACTION_UPDATE_MEMORY_PHASE_WORD = "UPDATEWORD";
    public static final String ACTION_POSTPONE_LEARNING_SESSION = "POSTPONE";

    /**
     * ADD WORD ACTION
     */
    private static final String DICTID = "DICTID";
    private static final String WORD = "WORD";
    private static final String DEFINITION = "DEFINITION";
    private static final String WORDID = "WORDID";


    public static void startActionRiseTodayList(Context context) {
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.setAction(ACTION_RISE_TODAY_LIST);
        context.startService(intent);
    }

    /**
     * Public method for asking MemoryManager service to add a new word
     * @param context app context
     * @param dictionaryID long dictionary's id
     * @param word word to add
     * @param definition defintion of the word
     */
    public static void startActionAddWord(Context context, long dictionaryID, String word, String definition){
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.putExtra(DICTID, dictionaryID);
        intent.putExtra(WORD, word);
        intent.putExtra(DEFINITION, definition);
        intent.setAction(ACTION_ADD_WORD);
        context.startService(intent);
    }

    /**
     * Update a word
     * @param context application context
     * @param id dictionary object id
     */
    public static void startActionUpdateWord(Context context, long id){
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.putExtra(WORDID, id);
        intent.setAction(ACTION_UPDATE_MEMORY_PHASE_WORD);
        context.startService(intent);
    }

    public static void startActionPostponeLearningSessions(Context context){
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.setAction(ACTION_POSTPONE_LEARNING_SESSION);
        context.startService(intent);
    }

    public MnemoMemoryManager() {
        super("MnemoMemoryManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null)
            switch (intent.getAction()){
                case ACTION_RISE_TODAY_LIST:
                    riseTodayList();
                    break;
                case ACTION_ADD_WORD:
                    addNewWordToDictionary(intent.getLongExtra(DICTID, -1),
                            intent.getStringExtra(WORD),
                            intent.getStringExtra(DEFINITION));
                    break;
                case ACTION_UPDATE_MEMORY_PHASE_WORD:
                    updateMemoryPhaseOfDictionaryObject(intent.getLongExtra(WORDID,-1));
                    break;
                case ACTION_POSTPONE_LEARNING_SESSION:
                    reportNonLearntObjects();
                    break;
                default:
                    break;
            }
    }


    private void riseTodayList() {

        Calendar now = Calendar.getInstance();
        //now.add(Calendar.DAY_OF_YEAR, 1);

        MemoryManagerSQLManager manager = MemoryManagerSQLManager.getInstance();
        Vector<Couple<Long, Integer>> list = manager.getListOfObjectsToLearn(now.getTime());
        if (list == null)
            return;

        /**
         * test notif
         */
        int mID = 0;
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_action_new).setContentTitle("ID PRESENT").setContentText("" + list.toString());
        Intent intent = new Intent(getApplicationContext(), MnemoCentral.class);
        intent.putExtra(MnemoCentral.EXTRA_ALERT_DATE, GeneralTools.getSQLDate(now.getTime()));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MnemoCentral.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pintent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mbuilder.setContentIntent(pintent);
        NotificationManager not = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(mID, mbuilder.build());
    }


    /**
     * Add a word in dictionary and memory monitoring system
     * @param dictionaryID dictionary id
     * @param word word
     * @param definition definition
     */
    private void addNewWordToDictionary(long dictionaryID, String word, String definition){

        if (dictionaryID == -1)
            return;

        //create the memory monitoring part
        DictionaryObject.MemoryMonitoringObject monitor = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();

        //create the dictionary base object
        long dictObjID = MemoryManagerSQLManager.getInstance().createDictionaryObject(dictionaryID, monitor.getID());

        //create the word part
        long wordID = DictionarySQLManager.getInstance(getApplicationContext()).addNewWord(dictObjID, word, definition);

        //add to the first learning session
        MemoryManagerSQLManager.getInstance().addWordToLearnSession(wordID, monitor.getNextLearnt());

    }

    private void updateMemoryPhaseOfDictionaryObject(long dictionaryObjectID){
        if (dictionaryObjectID < 0)
            return;
        DictionaryObject object = DictionarySQLManager.getInstance(getApplicationContext()).getFullObjectFromID(dictionaryObjectID);
        if (object == null) {
            Logger.i("MnemoMemoryManager::updateMemoryPhaseOfDictionaryObject", " object whith id " + dictionaryObjectID + " is null");
            return;
        }
        //check if object was already updated today
        //if it was, do not update it again
        String now = GeneralTools.getSQLDate(Calendar.getInstance().getTime());
        if (now.compareTo(GeneralTools.getSQLDate(object.getMemoryMonitoring().getLastLearnt())) != 0)
            LongTermMemory.getInstance(getApplicationContext()).updateMemorisationPhase(object);
    }


    private void reportNonLearntObjects(){
        //for each older date than today in memory manager table, update next learning date and delay of objects
        //we take a depth of 7 days, will change it in the future
        //@TODO: change the way we fix the depth in days, should be persisted in memory
        MemoryManagerSQLManager.getInstance().DelayLearningSessionFromDate(7);
    }
}