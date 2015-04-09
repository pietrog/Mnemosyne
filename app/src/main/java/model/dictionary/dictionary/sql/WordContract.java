package model.dictionary.dictionary.sql;

import android.provider.BaseColumns;

import model.dictionary.Global;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.memoryManager.sql.MemoryManagerContract;

/**
 * Created by pietro on 20/03/15.
 * SQL contract class for dictionary of word, extends DictionaryContractBase
 */
public class WordContract {


    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "wordObjects";
        public static final String WORD = "word";
        public static final String DEFINITION = "definition";
        public static final String DICTIONARYOBJECTID = "dictionaryobjectID";
    }


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Word.TABLE_NAME + " ( "
                    + Word._ID + " INTEGER PRIMARY KEY" + Global.COMMASEP
                    + Word.WORD + Global.TEXT_TYPE + Global.COMMASEP
                    + Word.DEFINITION + Global.TEXT_TYPE + Global.COMMASEP
                    + Word.DICTIONARYOBJECTID + Global.INTEGER_TYPE + " REFERENCES "+ DictionaryObjectContract.DictionaryObject.TABLE_NAME + "(" + DictionaryObjectContract.DictionaryObject._ID + ") ON DELETE CASCADE"
                    + " )";


    public static final String SQL_DROP_TABLE = "DROP TABLE " + Word.TABLE_NAME;

}
