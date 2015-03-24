package model.dictionary.catalogue.sql;

import android.provider.BaseColumns;

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


}
