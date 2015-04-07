package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.memory.LongTermMemory;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;

public class MnemoMemoryManager extends IntentService {

    public static final String ACTION_RISE_TODAY_LIST = "RISETODAYLIST";
    public static final String ACTION_ADD_WORD = "ADDWORD";
    public static final String ACTION_UPDATE_MEMORY_PHASE_WORD = "UPDATEWORD";

    /**
     * ADD WORD ACTION
     */
    private static final String CATNAME = "CATALOGUE_NAME";
    private static final String DICTNAME = "DICTIONARY_NAME";
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
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     * @param word word to add
     * @param definition defintion of the word
     */
    public static void startActionAddWord(Context context, String catalogueName, String dictionaryName, String word, String definition){
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.putExtra(CATNAME, catalogueName);
        intent.putExtra(DICTNAME, dictionaryName);
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
                    addNewWordToDictionary(intent.getStringExtra(CATNAME),
                            intent.getStringExtra(DICTNAME),
                            intent.getStringExtra(WORD),
                            intent.getStringExtra(DEFINITION));
                    break;
                case ACTION_UPDATE_MEMORY_PHASE_WORD:
                    updateMemoryPhaseOfDictionaryObject(intent.getLongExtra(WORDID,-1));
                    break;
                default:
                    break;
            }
    }


    private void riseTodayList() {
        MemoryManagerSQLManager manager = MemoryManagerSQLManager.getInstance();
        Vector<Integer> list = manager.getTodayList();
        if (list == null)
            return;

        /**
         * test notif
         */
        int mID = 0;
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_action_new).setContentTitle("ID PRESENT").setContentText("" + list.toString());
        NotificationManager not = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(mID, mbuilder.build());
    }


    /**
     * Add word in dictionary and memory monitoring system
     * @param catalogueName catalogue name
     * @param dictionaryName dictionary name
     * @param word word
     * @param definition definition
     */
    private void addNewWordToDictionary(String catalogueName, String dictionaryName, String word, String definition){

        //create the dictionary part
        long id = DictionarySQLManager.getInstance(getApplicationContext()).addNewWord(catalogueName, dictionaryName, word, definition);

        //create the memory monitoring part
        if (id == Global.FAILURE)
            Toast.makeText(getApplicationContext(), word + " already exists.", Toast.LENGTH_SHORT).show();
        //else add it in memoryManager
        else {
            Toast.makeText(getApplicationContext(), word + " added to " + dictionaryName, Toast.LENGTH_SHORT).show();
            //add word in memory manager via MemoryManager
            DictionaryObject objectToUpdate = DictionarySQLManager.getInstance(getApplicationContext()).getFullObjectFromID(id);
            //update object in db
            MemoryManagerSQLManager.getInstance(getApplicationContext()).updateDictionaryObjectInDB(objectToUpdate);
        }
    }

    private void updateMemoryPhaseOfDictionaryObject(long id){
        if (id == -1)
            return;
        DictionaryObject object = DictionarySQLManager.getInstance(getApplicationContext()).getFullObjectFromID(id);
        LongTermMemory.getInstance(getApplicationContext()).updateMemorisationPhase(object);
        if (MemoryManagerSQLManager.getInstance(getApplicationContext()).updateDictionaryObjectInDB(object) == Global.SUCCESS)
            Toast.makeText(getApplicationContext(), " Object updated",Toast.LENGTH_SHORT).show();
    }
}