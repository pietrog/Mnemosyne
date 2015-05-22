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
        public static final String CATALOGUE_NAME = "catalogueName";
        public static final String CATALOGUE_DESC = "catalogueDescription";
    }


    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + Catalogue.TABLE_NAME + "( "
            + Catalogue._ID + Global.INTEGER_TYPE + " PRIMARY KEY" + Global.COMMASEP
            + Catalogue.CATALOGUE_NAME + Global.TEXT_TYPE + "  " + Global.COMMASEP
            + Catalogue.CATALOGUE_DESC + Global.TEXT_TYPE + Global.COMMASEP
            + "UNIQUE (" + Catalogue.CATALOGUE_NAME + ") ON CONFLICT ABORT )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + Catalogue.TABLE_NAME;


}
