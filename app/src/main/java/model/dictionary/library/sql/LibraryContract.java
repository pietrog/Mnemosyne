package model.dictionary.library.sql;

import android.provider.BaseColumns;

/**
 * Created by pietro on 23/03/15.
 * Contract class for Library database
 */
public class LibraryContract {

    public LibraryContract(){}

    public static abstract class Library implements BaseColumns{
        public static final String TABLE_NAME = "library";
        public static final String COLUMN_CATALOGUE_NAME = "catalogueName";
        public static final String COLUMN_CATALOGUE_DESCRIPTION = "catalogueDescription";
        public static final String COLUMN_COUNT_ITEM = "catalogueCount"; //number of dictionaries in this catalogue
    }
}
