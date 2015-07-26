package com.mnemo.pietro.mnemosyne;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Manage to start Mnemosyne service when BOOT_COMPLETED action occurs
 */
public class MnemosyneBroadcastReceiver extends BroadcastReceiver {
    public MnemosyneBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case Intent.ACTION_BOOT_COMPLETED:

                startMnemoryManagerService(context);
                break;
            default:
                break;
        }
    }

    /**
     * Launch MnemoryManagerService when boot completed
     * @param context context of the application
     */
    private void startMnemoryManagerService(Context context){
        MnemoMemoryManager.initSystem(context);
        MnemoMemoryManager.startActionRiseTodayList(context);
    }


}
