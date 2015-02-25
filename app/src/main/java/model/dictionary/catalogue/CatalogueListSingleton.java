package model.dictionary.catalogue;

import android.content.Context;

/**
 * Created by pietro on 25/02/15.
 *
 * Very simple version of singleton. We do not need a more complex version right now
 */
public class CatalogueListSingleton {

    private static CatalogueList singleton = null;


    private CatalogueListSingleton(){
    }

    public static synchronized CatalogueList getInstance(Context context){
        if (singleton == null)
            singleton = CatalogueList.LoadCatalogueListFromJSONFile(context);

        return singleton;
    }

}
