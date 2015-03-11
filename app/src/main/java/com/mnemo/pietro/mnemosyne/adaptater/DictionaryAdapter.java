package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;

import model.dictionary.dictionary.sql.DictionaryContract;


/**
 * Created by pietro on 06/03/15.
 *
 *
 */
public class DictionaryAdapter extends ResourceCursorAdapter {

    private DictionaryFragment mFrag;

    public DictionaryAdapter (DictionaryFragment frag, Context context, int layout, Cursor cursor, int flags){
        super(context, layout, cursor, flags);
        mFrag = frag;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootview = inflater.inflate(R.layout.dictionary_view, parent, false);
        return rootview;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(cursor.getString(cursor.getColumnIndex(DictionaryContract.Dictionary.COLUMN_NAME_WORD)));
        TextView desc = (TextView) view.findViewById(R.id.description);
        desc.setText(cursor.getString(cursor.getColumnIndex(DictionaryContract.Dictionary.COLUMN_NAME_DEFINITION)));
        Button removeButton = (Button) view.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(mFrag);
        removeButton.setTag(cursor.getString(cursor.getColumnIndex(DictionaryContract.Dictionary.COLUMN_NAME_WORD)));

    }
}
