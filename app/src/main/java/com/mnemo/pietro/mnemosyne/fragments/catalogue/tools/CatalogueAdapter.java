package com.mnemo.pietro.mnemosyne.fragments.catalogue.tools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.catalogue.sql.CatalogueContract;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.ViewTools;

/**
 * Created by pietro on 03/03/15.
 *
 */
public class CatalogueAdapter extends ResourceCursorAdapter {


    public CatalogueAdapter (Context context, int layout, Cursor cursor, int flags){
        super(context, layout, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.library_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.NAME);
        String desc = GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.DESCRIPTION);
        ViewTools.setStringOfTextView(view.findViewById(R.id.name), name);
        ViewTools.setStringOfTextView(view.findViewById(R.id.description), desc);
    }

}
