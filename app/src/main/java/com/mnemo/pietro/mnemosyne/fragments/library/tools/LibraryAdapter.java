package com.mnemo.pietro.mnemosyne.fragments.library.tools;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 12/02/15.
 *
 *
 */
public class LibraryAdapter extends RecyclerView.Adapter {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView mName;
        public TextView mDescription;

        public ViewHolder(View view) {
            super(view);
            mName = (TextView)view.findViewById(R.id.name);
            mDescription = (TextView)view.findViewById(R.id.description);
        }
    }

    private Cursor mCursor;

    public LibraryAdapter (Cursor cursor){
        mCursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.library_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder)holder;
        if (mCursor != null && mCursor.moveToPosition(position)){
            h.mName.setText(GeneralTools.getStringElement(mCursor, DictionaryContractBase.DictionaryBase.NAME));
            h.mDescription.setText(GeneralTools.getStringElement(mCursor, DictionaryContractBase.DictionaryBase.DESCRIPTION));
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }
}


