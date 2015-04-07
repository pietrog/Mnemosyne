package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.database.Cursor;
import android.os.Bundle;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.DictionaryAdapter;

import java.util.Vector;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.ViewTools;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment{

    public static final String LISTOFID = "LISTOFID"; // contains a list of word id the user should learn today
    private Vector<Integer> mListOfId;

    public static TodayListFragment newInstance(Vector<Integer> listOfid){
        TodayListFragment fragment = new TodayListFragment();
        Bundle args = new Bundle();
        args.putSerializable(LISTOFID, listOfid);

        fragment.setArguments(args);

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mListOfId = (Vector<Integer>)getArguments().getSerializable(LISTOFID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListOfId == null)
            return;

        DictionarySQLManager sqlManager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        Cursor mRawCursor = sqlManager.getDictionaryObjectsFromLearningList(mListOfId);
        mAdapter = new DictionaryAdapter(getActivity().getApplicationContext(), R.layout.std_list_fragment, mRawCursor, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_todayslist);
        ViewTools.setSubtitle(getActivity(), "");
    }
}
