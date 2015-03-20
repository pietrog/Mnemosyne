package model.dictionary.memoryManager;

import android.content.Context;

/**
 * Created by pietro on 20/03/15.
 */
public class MemoryManagerSingleton {

    private static MemoryManager instance = null;

    public static synchronized MemoryManager getInstance(Context context){
        if (instance == null)
            instance = new MemoryManager(context);

        return instance;
    }
}
