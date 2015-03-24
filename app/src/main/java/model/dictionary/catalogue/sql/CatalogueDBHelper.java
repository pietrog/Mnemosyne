package model.dictionary.catalogue.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueContract.Catalogue;

/**
 * Created by pietro on 23/03/15.
 * Manage Catalogue database. Contains all existing catalogues
 */
public class CatalogueDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "catalogue.db";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + Catalogue.TABLE_NAME + "( "
            + Catalogue._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + Catalogue.COLUMN_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_DESCRIPTION + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_COUNT_DICT + Global.INTEGER_TYPE + Global.COMMASEP
            + "UNIQUE (" + Catalogue.COLUMN_CATALOGUE_NAME + Global.COMMASEP + Catalogue.COLUMN_DICTIONARY_NAME + ") ON CONFLICT ABORT )";

    private static final String SQL_DROP_TABLE = "DROP TABLE " + Catalogue.TABLE_NAME;

    public CatalogueDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

}
