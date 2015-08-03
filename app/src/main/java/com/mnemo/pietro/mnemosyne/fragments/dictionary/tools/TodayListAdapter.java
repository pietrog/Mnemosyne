package com.mnemo.pietro.mnemosyne.fragments.dictionary.tools;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.tools.CursorRecycleViewAdapter;

import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 24/06/15.
 */
public class TodayListAdapter extends CursorRecycleViewAdapter<TodayListAdapter.ViewHolder> {

    private long mRefDate;

    public TodayListAdapter(long refDate, Cursor cursor, Context context){
        super(cursor, context);
        mRefDate = refDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.dictionary_view, parent, false));
    }

    /**
     * Override method from abstract class {CursorRecycleViewAdapter}
     * @param holder Holder implented here
     * @param cursor cursor, already checked, and position set
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.mName.setText(GeneralTools.getStringElement(cursor, WordContract.Word.WORD));
        holder.mName.setBackgroundColor(GeneralTools.getColorFromDelay((int) GeneralTools.getNumberOfDaysBetweenTwoDates(mRefDate, GeneralTools.getLongElement(mCursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN))));
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mName;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mName = (TextView)view.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(mContext, WordFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MnemoDictionary.NAME, mName.getText());
            mContext.startActivity(intent);*/
        }
    }

}
