package model.dictionary.catalogue;

import android.database.Cursor;

import model.dictionary.library.sql.LibraryContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 06/02/15.
 *
 * Describe a catalogue, object that contains dictionaries. Can be an history book, or english book, with
 * classic dictionary, grammatical definitions, ...
 */
public class Catalogue {

    private long mID;
    private String mName;
    private String mDescription;

    public long getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public Catalogue (long id, String name, String description){
        mID = id;
        mName = name;
        mDescription = description;
    }


    public static Catalogue LoadFromSQL(Cursor cursor){
        return new Catalogue(GeneralTools.getLongElement(cursor, LibraryContract.Library._ID),
                GeneralTools.getStringElement(cursor, LibraryContract.Library.COLUMN_CATALOGUE_NAME),
                GeneralTools.getStringElement(cursor, LibraryContract.Library.COLUMN_CATALOGUE_DESCRIPTION));

    }

}