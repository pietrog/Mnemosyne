package model.dictionary.library.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.Global;

/**
 * Created by pietro on 23/03/15.
 * Manage library database. Contains all Catalogues entities
 */
public class LibraryDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "library.db";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + LibraryContract.Library.TABLE_NAME + "( "
            + LibraryContract.Library._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + LibraryContract.Library.COLUMN_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + LibraryContract.Library.COLUMN_CATALOGUE_DESCRIPTION + Global.TEXT_TYPE + Global.COMMASEP
            + LibraryContract.Library.COLUMN_COUNT_ITEM + Global.INTEGER_TYPE + Global.COMMASEP
            + "UNIQUE (" + LibraryContract.Library.COLUMN_CATALOGUE_NAME + ") ON CONFLICT ABORT )";

    private static final String SQL_DROP_TABLE = "DROP TABLE " + LibraryContract.Library.TABLE_NAME;

    public LibraryDBHelper(Context context){
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
