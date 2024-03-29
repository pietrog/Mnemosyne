package com.mnemo.pietro.mnemosyne.fragments.library.tools;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.MnemoDictionary;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.tools.CursorRecycleViewAdapter;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 12/02/15.
 *
 *
 */
public class LibraryAdapter extends CursorRecycleViewAdapter<LibraryAdapter.ViewHolder> {

    public LibraryAdapter (Cursor cursor, Context context){
        super(cursor, context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.library_view, parent, false));
    }

    /**
     * Override method from abstract class {CursorRecycleViewAdapter}
     * @param holder Holder implented here
     * @param cursor cursor, already checked, and position set
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.mName.setText(GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.NAME));
        holder.mDescription.setText(GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.DESCRIPTION));
        holder.mID = GeneralTools.getLongElement(cursor, DictionaryContractBase.DictionaryBase._ID);
    }
    private long mRefDate; // reference date for the learning sessions. all object to learn should have a next learn date less or equal to refdate



    /**
     * Class describing an item in library recycle view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case

        public TextView mName;
        public TextView mDescription;
        public long mID;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mName = (TextView)view.findViewById(R.id.name);
            mDescription = (TextView)view.findViewById(R.id.description);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MnemoDictionary.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MnemoDictionary.ID, mID);
            intent.putExtra(MnemoDictionary.NAME, mName.getText());
            v.getContext().startActivity(intent);
        }
    }


}


