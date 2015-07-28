package model.dictionary.catalogue.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContractBase;
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

    public static CatalogueSQLManager getInstance(){
        return instance;
    }

    /**
     * IMPLEMENTATION
     */
    private CatalogueSQLManager (Context context){
        super(context);
    }


    /**
     * Get all the catalogues
     * @return cursor containing a list of catalogues
     */
    public Cursor getAll(){
        String sql = "SELECT * FROM " + CatalogueContract.Catalogue.TABLE_NAME;
        return rawQuery(sql, null);
    }
    public Cursor getAllDictionary(){
        String sql = "SELECT * FROM " + DictionaryContractBase.DictionaryBase.TABLE_NAME ;
        return rawQuery(sql, null);
    }

    /**
     * Return all dictionaries related to a catalogue
     * @param id catalogue id
     * @return cursor containing the dictionary row list
     */
    public Cursor getAllDictionaryOfCatalogue(long id){
        String sql =  "SELECT * FROM " + DictionaryContractBase.DictionaryBase.TABLE_NAME
                + " WHERE " + DictionaryContractBase.DictionaryBase.CATALOGUEID + " = " + id
                + " ORDER BY " + DictionaryContractBase.DictionaryBase.NAME;

        return rawQuery(sql, null);
    }


    /**
     * Add a new catalogue in the library
     * @param name name of the catalogue
     * @param description catalogue description
     * @return sql row id of the new catalogue
     */
    public long add(String name, String description){
        if (name == null || name.length() < 3 )
            return Global.BAD_PARAMETER;
        ContentValues val = new ContentValues();
        val.put(CatalogueContract.Catalogue.NAME, name);
        val.put(CatalogueContract.Catalogue.DESCRIPTION, description);
        return add(val, CatalogueContract.Catalogue.TABLE_NAME);
    }

    /**
     * Remove catalogue identified by ids in listIDs
     * @param listIDs list of catalogue's ids to remove
     * @return number of affected rows
     */
    public int remove(long[] listIDs){
        return remove(listIDs, CatalogueContract.Catalogue.TABLE_NAME);
    }

}
