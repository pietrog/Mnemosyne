package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.Vector;

import model.dictionary.memoryManager.MemoryManager;

public class MnemoMemoryManager extends IntentService {

    private static final String ACTION_RISE_TODAY_LIST = "com.mnemo.pietro.mnemosyne.action.RISETODAYLIST";

    private static final String EXTRA_PARAM1 = "com.mnemo.pietro.mnemosyne.extra.PARAM1";

    public static void startActionRiseTodayList(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.setAction(ACTION_RISE_TODAY_LIST);
        context.startService(intent);
    }

    public MnemoMemoryManager() {
        super("MnemoMemoryManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            handleActionRiseTodayList(param1);
        }
    }

    private void handleActionRiseTodayList(String param1) {
        MemoryManager mg = new MemoryManager(getApplicationContext());
        Vector<Integer> list = mg.getTodayListOfObjectIDs();

    }

}
