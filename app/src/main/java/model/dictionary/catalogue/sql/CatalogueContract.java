package model.dictionary.catalogue.sql;

import android.provider.BaseColumns;

import model.dictionary.Global;

/**
 * Created by pietro on 23/03/15.
 * Contract class for Catalogue database
 */
public class CatalogueContract {

    public CatalogueContract(){}

    public static abstract class Catalogue implements BaseColumns{
        public static final String TABLE_NAME = "catalogue";
        public static final String COLUMN_CATALOGUE_NAME = "catalogueName";
        public static final String COLUMN_DICTIONARY_NAME = "dictName";
        public static final String COLUMN_DICTIONARY_DESCRIPTION = "dictDesc";
        public static final String COLUMN_DICTIONARY_COUNT_DICT = "dictCount";
    }


    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + Catalogue.TABLE_NAME + "( "
            + Catalogue._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + Catalogue.COLUMN_CATALOGUE_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_NAME + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_DESCRIPTION + Global.TEXT_TYPE + Global.COMMASEP
            + Catalogue.COLUMN_DICTIONARY_COUNT_DICT + Global.INTEGER_TYPE + Global.COMMASEP
            + "UNIQUE (" + Catalogue.COLUMN_CATALOGUE_NAME + Global.COMMASEP + Catalogue.COLUMN_DICTIONARY_NAME + ") ON CONFLICT ABORT )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + Catalogue.TABLE_NAME;


}
