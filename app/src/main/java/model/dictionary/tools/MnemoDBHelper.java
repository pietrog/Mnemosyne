package model.dictionary.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.dictionary.catalogue.sql.CatalogueContract;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
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

    public static synchronized void release(){
        if (instance != null){
            instance.close();
            instance = null;
        }
    }

    private static final String DATABASE_NAME = "mnemosyneDATABASE";
    private static final int DATABASE_VERSION = 1;

    private MnemoDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //memory monitoring
        //System.out.println(MemoryManagerContract.SQL_CREATE_MEMORY_MANAGER_TABLE);
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_MANAGER_TABLE);
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_PHASE_TABLE);
        MemoryManagerContract.populateDefaultMemoryPhase(db); // populate the table
        db.execSQL(MemoryManagerContract.SQL_CREATE_MEMORY_MONITORING_TABLE);
        //dictionary
        db.execSQL(CatalogueContract.SQL_CREATE_TABLE);
        db.execSQL(DictionaryContractBase.SQL_CREATE_TABLE);
        db.execSQL(DictionaryObjectContract.SQL_CREATE_TABLE);
        db.execSQL(WordContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDrop(db);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void initTestDatabase(SQLiteDatabase db){
        //add a catalogue
        ContentValues val = new ContentValues();
        val.put(CatalogueContract.Catalogue.CATALOGUE_NAME, "Dictionnaires de langue");
        val.put(CatalogueContract.Catalogue.CATALOGUE_DESC, "Contient des dictionnaires de langue");
        long idCatalogue = db.insert(CatalogueContract.Catalogue.TABLE_NAME, null, val);

        //add dictionaries
        val = new ContentValues();
        val.put(DictionaryContractBase.DictionaryBase.NAME, "Anglais");
        val.put(DictionaryContractBase.DictionaryBase.DESCRIPTION, "Dictionnaire d'anglais");
        val.put(DictionaryContractBase.DictionaryBase.CATALOGUEID, idCatalogue);
        long idDictUK = db.insert(DictionaryObjectContract.DictionaryObject.TABLE_NAME, null, val);
        val = new ContentValues();
        val.put(DictionaryContractBase.DictionaryBase.NAME, "Italien");
        val.put(DictionaryContractBase.DictionaryBase.DESCRIPTION, "Dictionnaire d'italien");
        val.put(DictionaryContractBase.DictionaryBase.CATALOGUEID, idCatalogue);
        long idDictIT = db.insert(DictionaryObjectContract.DictionaryObject.TABLE_NAME, null, val);

        //add words at different dates
    }



    /** CREATE AND DROP DB FROM INSTANCE **/
    private void onDrop(SQLiteDatabase db){
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryManager.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryMonitoring.TABLE_NAME);
        db.execSQL("DROP TABLE " + MemoryManagerContract.MemoryPhase.TABLE_NAME);
        db.execSQL(WordContract.SQL_DROP_TABLE);
        db.execSQL(CatalogueContract.SQL_DROP_TABLE);
        db.execSQL(DictionaryContractBase.SQL_DROP_TABLE);
        db.execSQL(DictionaryObjectContract.SQL_DROP_TABLE);
    }

    public static void onDrop(){
        if (instance != null)
            instance.onDrop(instance.getWritableDatabase());
    }
    public static void onCreate(){
        if (instance != null)
            instance.onCreate(instance.getWritableDatabase());
    }

}