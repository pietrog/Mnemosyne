package com.mnemo.pietro.mnemosyne.fragments.dictionary.tools;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;
import com.mnemo.pietro.mnemosyne.tools.CursorRecycleViewAdapter;

import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.tools.GeneralTools;


/**
 * Created by pietro on 06/03/15.
 *
 *
 */
public class DictionaryAdapter extends CursorRecycleViewAdapter<DictionaryAdapter.ViewHolder> {

    private final FragmentManager fm;

    public DictionaryAdapter (Cursor cursor, Context context, FragmentManager fragmentManager){
        super(cursor, context);
        fm = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.dictionary_view, parent, false), fm);
    }

    /**
     * Override method from abstract class {CursorRecycleViewAdapter}
     * @param holder Holder implemented here
     * @param cursor cursor, already checked, and position set
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.mName.setText(GeneralTools.getStringElement(cursor, WordContract.Word.WORD));
        holder.definition = GeneralTools.getStringElement(mCursor, WordContract.Word.DEFINITION);
        holder.dictionaryID = GeneralTools.getLongElement(mCursor, DictionaryObjectContract.DictionaryObject.CSID);
        holder.wordID = GeneralTools.getLongElement(mCursor, WordContract.Word.CSID);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mName;
        public long wordID;
        public long dictionaryID;
        public String definition;
        private final FragmentManager mFm;

        public ViewHolder(View view, FragmentManager fm){
            super(view);
            mFm = fm;
            view.setOnClickListener(this);
            mName = (TextView)view.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            WordFragment fragment = WordFragment.newInstance(mName.getText().toString(), definition, wordID, dictionaryID);
            mFm.beginTransaction().add(R.id.main_subscreen, fragment).addToBackStack(null).commit();
        }
    }
}
