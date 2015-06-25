package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.database.Cursor;
import android.os.Bundle;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.TodayListAdapter;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.ViewTools;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment{


    public static final String ALERTDATE = "ALERTDATE"; // the date corresponding to the raise alert
    private String mDate;

    public static TodayListFragment newInstance(String date){
        TodayListFragment fragment = new TodayListFragment();
        Bundle args = new Bundle();
        args.putString(ALERTDATE, date);

        fragment.setArguments(args);

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mDate = getArguments().getString(ALERTDATE);
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
        mAdapter.changeCursor(DictionarySQLManager.getInstance().getDictionaryObjectsFromLearningList(mDate));
    }

    public void refreshListView(){
        if (mAdapter == null){
            Cursor mRawCursor = DictionarySQLManager.getInstance().getDictionaryObjectsFromLearningList(mDate);
            mAdapter = new TodayListAdapter(getActivity().getApplicationContext(), R.layout.std_list_fragment, mRawCursor, 0);
        }
        else
            mAdapter.changeCursor(DictionarySQLManager.getInstance().getDictionaryObjectsFromLearningList(mDate));
    }
}
