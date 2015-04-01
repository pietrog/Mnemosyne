package model.dictionary.library.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import model.dictionary.tools.BaseSQLManager;

/**
 * Created by pietro on 23/03/15.
 * This class is a Singleton
 * Deals with library table, containing catalogues, so with get/add/remove/modify catalogues
 * Give cursors, manage dbHelper,...
 */
public class LibrarySQLManager extends BaseSQLManager{

    private static LibrarySQLManager instance = null;

    public static synchronized LibrarySQLManager getInstance(Context context){
        if (instance == null){
            instance = new LibrarySQLManager(context);
        }
        return instance;
    }

    /**
     * IMPLEMENTATION
     */
    public LibrarySQLManager (Context context){
        super(new LibraryDBHelper(context));
    }

    public Cursor getAll(){
        String sql = "SELECT * FROM " + LibraryContract.Library.TABLE_NAME;
        return rawQuery(sql, null);
    }

    /**
     * Add a new Catalogue in database
     * @param catalogueName catalogue's name
     * @param description description
     * @return row id of the new catalogue, -1 otherwise
     */
    public long add(String catalogueName, String description){
        ContentValues val = new ContentValues();
        val.put(LibraryContract.Library.COLUMN_CATALOGUE_NAME, catalogueName);
        val.put(LibraryContract.Library.COLUMN_CATALOGUE_DESCRIPTION, description);
        val.put(LibraryContract.Library.COLUMN_COUNT_ITEM, 0);
        return add(val, LibraryContract.Library.TABLE_NAME);
    }

    /**
     * Remove catalogues identified by ids in listIDs
     * @param listIDs list of catalogue ids to remove
     * @return number of affected rows
     */
    public int remove(long[] listIDs){
        return remove(listIDs, LibraryContract.Library.TABLE_NAME);
    }

}
