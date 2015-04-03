package model.dictionary.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.catalogue.sql.CatalogueContract;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.library.sql.LibraryContract;
import model.dictionary.memoryManager.sql.MemoryManagerContract;


/**
 * Created by pietro on 02/04/15.
 *
 */
public class MnemoDBHelper extends SQLiteOpenHelper {

    private static MnemoDBHelper instance;

    public static synchronized MnemoDBHelper getInstance(Context context){
        if (instance == null)
            instance = new MnemoDBHelper(context);

        return instance;
    }

    private static final String DATABASE_NAME = "mnemosyneDATABASE";
    private static final int DATABASE_VERSION = 1;

    private MnemoDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //memory monitoring
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_MANAGER_TABLE);
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_PHASE_TABLE);
        MemoryManagerContract.populateDefaultMemoryPhase(db); // populate the table
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_MONITORING_TABLE);
        //dictionary
        db.execSQL(CatalogueContract.SQL_CREATE_TABLE);
        db.execSQL(LibraryContract.SQL_CREATE_TABLE);
        db.execSQL(DictionaryContractBase.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryPhase.TABLE_NAME);
        db.execSQL(CatalogueContract.SQL_DROP_TABLE);
        db.execSQL(LibraryContract.SQL_DROP_TABLE);
        db.execSQL(DictionaryContractBase.SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

}