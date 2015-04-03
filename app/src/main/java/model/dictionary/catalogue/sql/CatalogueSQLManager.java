package model.dictionary.catalogue.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import model.dictionary.tools.BaseSQLManager;


/**
 * Created by pietro on 23/03/15.
 * This class is a Singleton
 * Deals with catalogue table, containing dictionaries, so with get/add/remove/modify dictionaries
 * Give cursors, manage dbHelper,...
 */
public class CatalogueSQLManager extends BaseSQLManager{

    private static CatalogueSQLManager instance = null;

    public static synchronized CatalogueSQLManager getInstance(Context context){
        if (instance == null){
            instance = new CatalogueSQLManager(context);
        }
        return instance;
    }

    /**
     * IMPLEMENTATION
     */
    private CatalogueSQLManager (Context context){
        super(context);
    }


    public Cursor getAll(String catalogueName){
        String sql = "SELECT * FROM " + CatalogueContract.Catalogue.TABLE_NAME + " WHERE " + CatalogueContract.Catalogue.COLUMN_CATALOGUE_NAME + " = '" + catalogueName +"'";
        return rawQuery(sql, null);
    }


    /**
     * Add a new dictionary in catalogueName. Check if it does not exist before
     * @param catalogueName name of the catalogue
     * @param dictionaryName name of the dictionary
     * @param description a brief description of the disctionary
     * @return sql row id of the new dictionary
     */
    public long add(String catalogueName, String dictionaryName, String description){
        ContentValues val = new ContentValues();
        val.put(CatalogueContract.Catalogue.COLUMN_CATALOGUE_NAME, catalogueName);
        val.put(CatalogueContract.Catalogue.COLUMN_DICTIONARY_NAME, dictionaryName);
        val.put(CatalogueContract.Catalogue.COLUMN_DICTIONARY_DESCRIPTION, description);
        val.put(CatalogueContract.Catalogue.COLUMN_DICTIONARY_COUNT_DICT, 0);
        return add(val, CatalogueContract.Catalogue.TABLE_NAME);
    }

    /**
     * Remove dictionaries identified by ids in listIDs
     * @param listIDs list of dictionaries' ids to remove
     * @return number of affected rows
     */
    public int remove(long[] listIDs){
        return remove(listIDs, CatalogueContract.Catalogue.TABLE_NAME);
    }



}
