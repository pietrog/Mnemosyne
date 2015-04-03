package model.dictionary.library.sql;

import android.provider.BaseColumns;

import model.dictionary.Global;

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


    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + LibraryContract.Library.TABLE_NAME + "( "
            + LibraryContract.Library._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + LibraryContract.Library.COLUMN_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + LibraryContract.Library.COLUMN_CATALOGUE_DESCRIPTION + Global.TEXT_TYPE + Global.COMMASEP
            + LibraryContract.Library.COLUMN_COUNT_ITEM + Global.INTEGER_TYPE + Global.COMMASEP
            + "UNIQUE (" + LibraryContract.Library.COLUMN_CATALOGUE_NAME + ") ON CONFLICT ABORT )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + LibraryContract.Library.TABLE_NAME;

}
