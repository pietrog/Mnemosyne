package model.dictionary.dictionary.sql;

import android.provider.BaseColumns;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueContract;

/**
 * Created by pietro on 06/03/15.
 *
 */
public class DictionaryContractBase {

    public DictionaryContractBase() {}

    public static abstract class DictionaryBase implements BaseColumns{
        public static final String TABLE_NAME = "dictionary";
        public static final String CID = TABLE_NAME + "." + _ID;

        public static final String CATALOGUEID = "catalogueID";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DictionaryBase.TABLE_NAME + " ( "
                    + DictionaryBase._ID + " INTEGER PRIMARY KEY" + Global.COMMASEP
                    + DictionaryBase.NAME + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.DESCRIPTION + Global.TEXT_TYPE + Global.COMMASEP
                    + DictionaryBase.CATALOGUEID + Global.INTEGER_TYPE + " REFERENCES "+ CatalogueContract.Catalogue.TABLE_NAME + "(" + CatalogueContract.Catalogue._ID + ") ON DELETE CASCADE" + Global.COMMASEP
                    + " UNIQUE ( " + DictionaryBase.CATALOGUEID + "," + DictionaryBase.NAME + " ) ON CONFLICT ABORT "
                    + " )";

    public static final String SQL_DROP_TABLE = "DROP TABLE " + DictionaryBase.TABLE_NAME;


}
