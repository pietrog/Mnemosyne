package com.mnemo.pietro.mnemosyne.tools;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by pietro on 28/07/15.
 * Abstract
 */
public abstract class CursorRecycleViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Cursor  mCursor;
    protected Context mContext;

    public CursorRecycleViewAdapter(Cursor cursor, Context context){
        mCursor = cursor;
        mContext = context;
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            onBindViewHolder(holder, mCursor);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }
}
