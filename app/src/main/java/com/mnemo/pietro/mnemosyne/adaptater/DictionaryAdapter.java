package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        return inflater.inflate(R.layout.dictionary_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String word = DictionaryContract.getWord(cursor);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(word);
        /*ImageButton removeButton = (ImageButton) view.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(mFrag);
        removeButton.setTag(word);*/

    }
}
