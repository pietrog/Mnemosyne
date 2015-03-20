package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.Vector;

import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.MemoryManagerSingleton;

public class MnemoMemoryManager extends IntentService {

    private static final String ACTION_RISE_TODAY_LIST = "com.mnemo.pietro.mnemosyne.action.RISETODAYLIST";

    private static final String EXTRA_PARAM1 = "com.mnemo.pietro.mnemosyne.extra.PARAM1";

    public static void startActionRiseTodayList(Context context, String param1) {
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.setAction(ACTION_RISE_TODAY_LIST);
        intent.putExtra(EXTRA_PARAM1, param1);
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
        MemoryManager mg = MemoryManagerSingleton.getInstance(getApplicationContext());
        Vector<Integer> list = mg.getTodayListOfObjectIDs();

        /**
         * test notif
         */
        int mID = 0;
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_action_new).setContentTitle("ID PRESENT").setContentText("" + list.toString());
        NotificationManager not = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(mID, mbuilder.build());
    }

}
