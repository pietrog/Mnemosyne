package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.os.Bundle;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment{

    public static final String LISTOFID = "LISTOFID"; // contains a list of word id the user should learn today
    private String mListOfId;

    public static TodayListFragment newInstance(String listOfid){
        TodayListFragment fragment = new TodayListFragment();
        Bundle args = new Bundle();
        args.putString(LISTOFID, listOfid);
        fragment.setArguments(args);

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mListOfId = getArguments().getString(LISTOFID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
