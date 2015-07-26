package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Calendar;
import java.util.Vector;

import model.dictionary.Global.Couple;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.memory.LongTermMemory;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.MnemoCalendar;

public class MnemoMemoryManager extends IntentService {

    public static final String ACTION_RISE_TODAY_LIST = "RISETODAYLIST";
    public static final String ACTION_ADD_WORD = "ADDWORD";
    public static final String ACTION_UPDATE_MEMORY_PHASE_WORD = "UPDATEWORD";

    /**
     * ADD WORD ACTION
     */
    private static final String DICTID = "DICTID";
    private static final String WORD = "WORD";
    private static final String DEFINITION = "DEFINITION";
    private static final String DICTIONARYOBJECTID = "DICTOBJID";

    public static void initSystem(Context context){
        CatalogueSQLManager.getInstance(context);
        DictionarySQLManager.getInstance(context);
        MemoryManagerSQLManager.getInstance(context);
        MemoryManager.initMemoryPhaseMap();
    }

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
        intent.putExtra(DICTIONARYOBJECTID, id);
        intent.setAction(ACTION_UPDATE_MEMORY_PHASE_WORD);
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
                    updateMemoryPhaseOfDictionaryObject(intent.getLongExtra(DICTIONARYOBJECTID, -1));
                    break;
                default:
                    break;
            }
    }


    private void riseTodayList() {

        Calendar now = MnemoCalendar.getInstance();

        MemoryManagerSQLManager manager = MemoryManagerSQLManager.getInstance();
        Vector<Couple<Long, Integer>> list = manager.getListOfObjectsToLearn(now.getTime().getTime());
        if (list == null)
            return;

        /**
         * test notif
         */
        int mID = 0;
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_action_new).setContentTitle(getResources().getString(R.string.hint_raised_message)).setContentText("");
        Intent intent = new Intent(getApplicationContext(), MnemoCentral.class);
        intent.putExtra(MnemoCentral.EXTRA_ALERT_DATE, now.getTimeInMillis());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MnemoCentral.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pintent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mbuilder.setContentIntent(pintent);
        NotificationManager not = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(mID, mbuilder.build());
    }


    /**
     * Add a new word (task in background)
     * @param dictionaryID dictionary id
     * @param word word
     * @param definition definition
     */
    private void addNewWordToDictionary(long dictionaryID, String word, String definition){

        if (dictionaryID == -1)
            return;

        //create the word part
        long wordID = DictionarySQLManager.getInstance(getApplicationContext()).addNewWord(dictionaryID, word, definition);

        WordDefinitionObj obj = DictionarySQLManager.getInstance().getWordFromID(wordID);

    }

    private void updateMemoryPhaseOfDictionaryObject(long dictionaryObjectID){
        if (dictionaryObjectID < 0)
            return;
        LongTermMemory.getInstance(getApplicationContext()).onLearnt(dictionaryObjectID);
    }

}