package com.mnemo.pietro.mnemosyne.fragments.library.tools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;


import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.library.sql.LibraryContract;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.ViewTools;

/**
 * Created by pietro on 12/02/15.
 *
 *
 */
public class LibraryAdapter extends ResourceCursorAdapter {

    public LibraryAdapter (Context context, int layout, Cursor cursor, int flags){
        super(context, layout, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.library_view, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = GeneralTools.getStringElement(cursor, LibraryContract.Library.COLUMN_CATALOGUE_NAME);
        String desc = GeneralTools.getStringElement(cursor, LibraryContract.Library.COLUMN_CATALOGUE_DESCRIPTION);
        ViewTools.setStringOfTextView((TextView)view.findViewById(R.id.name), name);
        ViewTools.setStringOfTextView((TextView)view.findViewById(R.id.description), desc);
    }

}


