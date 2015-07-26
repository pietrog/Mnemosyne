package com.mnemo.pietro.mnemosyne.fragments.dictionary.tools;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 24/06/15.
 */
public class TodayListAdapter extends DictionaryAdapter {

    private long mRefDate; // reference date for the learning sessions. all object to learn should have a next learn date less or equal to refdate

    public TodayListAdapter(long refDate, Context context, int layout, Cursor cursor, int flags){
        super(context, layout, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);
        view.setBackgroundColor(GeneralTools.getColorFromDelay((int) GeneralTools.getNumberOfDaysBetweenTwoDates(mRefDate, GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN))));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        view.setBackgroundColor(GeneralTools.getColorFromDelay((int) GeneralTools.getNumberOfDaysBetweenTwoDates(mRefDate, GeneralTools.getLongElement(cursor, MemoryManagerContract.MemoryMonitoring.NEXT_LEARN))));
    }

}
