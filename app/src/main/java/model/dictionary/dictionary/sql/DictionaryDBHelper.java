package model.dictionary.dictionary.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pietro on 06/03/15.
 * 
 */
public class DictionaryDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "dictionary.db";

    /**
     * Here we describe the db schema
     */
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMASEP = " ,";

    //sql request for dictionary table creation
    private static final String SQL_CREATE_DICTIONARY_TABLE =
            "CREATE TABLE " + DictionaryContract.Dictionary.TABLE_NAME + " ( "
                    + DictionaryContract.Dictionary._ID + " INTEGER PRIMARY KEY" + COMMASEP
                    + DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME + TEXT_TYPE + COMMASEP
                    + DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME + TEXT_TYPE + COMMASEP
                    + DictionaryContract.Dictionary.COLUMN_NAME_WORD + TEXT_TYPE + COMMASEP
                    + DictionaryContract.Dictionary.COLUMN_NAME_DEFINITION + TEXT_TYPE + " )";

    private static final String SQL_DROP_TABLE = "DROP TABLE " + DictionaryContract.Dictionary.TABLE_NAME;


    public DictionaryDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DICTIONARY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //@todo find what to do in onUpgrade db
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
