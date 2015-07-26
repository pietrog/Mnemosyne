package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.os.Bundle;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.TodayListAdapter;

import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.ViewTools;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment{


    public static final String ALERTDATE = "ALERTDATE"; // the date corresponding to the raise alert
    private long mDate;

    public static TodayListFragment newInstance(long date){
        TodayListFragment fragment = new TodayListFragment();
        Bundle args = new Bundle();
        args.putLong(ALERTDATE, date);

        fragment.setArguments(args);

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mDate = getArguments().getLong(ALERTDATE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshListView();
        setListAdapter(mAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_todayslist);
        ViewTools.setSubtitle(getActivity(), "");
        refreshListView();

    }

    @Override
    protected void removeWord(int position) {
        super.removeWord(position);
        mAdapter.changeCursor(MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate));
    }

    public void refreshListView(){
        if (mAdapter == null){
            mAdapter = new TodayListAdapter(mDate, getActivity().getApplicationContext(), R.layout.std_list_fragment, MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate), 0);
        }
        else
            mAdapter.changeCursor(MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate));
    }
}
