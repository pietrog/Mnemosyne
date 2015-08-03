package com.mnemo.pietro.mnemosyne.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

/**
 * Created by pietro on 28/07/15.
 * Abstract
 */
public abstract class CursorRecycleViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor  mCursor;
    private boolean mDataValid;
    protected Context mContext;
    private DataSetObserver mDataSetObserver;

    public CursorRecycleViewAdapter(Cursor cursor, Context context){
        mCursor = cursor;
        mDataValid = cursor != null;
        mContext = context;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mDataValid)
            cursor.registerDataSetObserver(mDataSetObserver);
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            onBindViewHolder(holder, mCursor);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    public void changeCursor(Cursor cursor){
        if (cursor != null){
            mCursor.unregisterDataSetObserver(mDataSetObserver);
            mCursor.close();
            mCursor = cursor;
            mDataValid = cursor != null;
            cursor.registerDataSetObserver(mDataSetObserver);
            notifyDataSetChanged();
        }
    }


    private class NotifyingDataSetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    }
}
