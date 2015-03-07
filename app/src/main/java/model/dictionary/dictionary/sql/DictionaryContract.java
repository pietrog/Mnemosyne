package model.dictionary.dictionary.sql;

import android.provider.BaseColumns;

/**
 * Created by pietro on 06/03/15.
 */
public class DictionaryContract {

    public DictionaryContract() {}

    public static abstract class Dictionary implements BaseColumns{
        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_NAME_ID = "entryid";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_DEFINITION = "definition";
    }
}
