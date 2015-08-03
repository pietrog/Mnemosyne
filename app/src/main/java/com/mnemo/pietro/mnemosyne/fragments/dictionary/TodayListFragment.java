package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.database.Cursor;
import android.os.Bundle;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.TodayListAdapter;

import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.MnemoCalendar;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment {


    public static final String ALERTDATE = "ALERTDATE"; // the date corresponding to the raise alert
    private long mDate;

    public static TodayListFragment newInstance(long date){
        TodayListFragment fragment = new TodayListFragment();

        if (date > 0) {
            Bundle args = new Bundle();
            args.putLong(ALERTDATE, date);
            fragment.setArguments(args);
        }

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mDate = getArguments().getLong(ALERTDATE);
        else
            mDate = MnemoCalendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Cursor mRawCursor = MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate);
        mAdapter = new TodayListAdapter(mDate, mRawCursor, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }


    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), MnemoWord.class);
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        intent.putExtra(WordFragment.WORDID, GeneralTools.getLongElement(cursor, WordContract.Word.CID));
        intent.putExtra(WordFragment.DICTIONARYOBJECTID, GeneralTools.getLongElement(cursor, WordContract.Word.DICTIONARYOBJECTID));
        intent.putExtra(WordFragment.WORD, GeneralTools.getStringElement(cursor, WordContract.Word.WORD));
        intent.putExtra(WordFragment.DEFINITION, GeneralTools.getStringElement(cursor, WordContract.Word.DEFINITION));
        startActivity(intent);
    }*/



    @Override
    public void onResume() {
        super.onResume();
    }

}
