package com.mnemo.pietro.mnemosyne;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.Vector;

import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;

public class MnemoMemoryManager extends IntentService {

    public static final String ACTION_RISE_TODAY_LIST = "RISETODAYLIST";


    public static void startActionRiseTodayList(Context context) {
        Intent intent = new Intent(context, MnemoMemoryManager.class);
        intent.setAction(ACTION_RISE_TODAY_LIST);
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
                default:
                    break;
            }
    }


    private void riseTodayList() {
        MemoryManagerSQLManager manager = MemoryManagerSQLManager.getInstance(getApplicationContext());
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

}
