package model.dictionary.dictionary.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.Global;

import model.dictionary.dictionary.sql.DictionaryContractBase.DictionaryBase;

/**
 * Created by pietro on 06/03/15.
 * 
 */
public class DictionaryDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "dictionary.db";


    //sql request for dictionary table creation
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DictionaryContractBase.DictionaryBase.TABLE_NAME + " ( "
                    + DictionaryBase._ID + " INTEGER PRIMARY KEY" + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_WORD + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DEFINITION + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DATE_LAST_LEARNING + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.COLUMN_NAME_DATE_NEXT_LEARNING + Global.TEXT_TYPE + Global.COMMASEP
                    + " UNIQUE ( " + DictionaryBase.COLUMN_NAME_CATALOGUE_NAME + Global.COMMASEP + DictionaryBase.COLUMN_NAME_DICTIONARY_NAME + Global.COMMASEP + DictionaryBase.COLUMN_NAME_WORD +")"
                    + " )";

    private static final String SQL_DROP_TABLE = "DROP TABLE " + DictionaryContractBase.DictionaryBase.TABLE_NAME;


    public DictionaryDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //@todo find what to do in onUpgrade db
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
