package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.DictionaryAdapter;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import com.mnemo.pietro.mnemosyne.fragments.word.CreateWordFragment;
import model.dictionary.tools.ViewTools;

/**
 * Dictionary fragment. Manages the layout of a dictionay, add and remove words, ...
 */
public class DictionaryFragment extends BaseDictionaryFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DICTIONARY_NAME = "DICTIONARY_NAME";
    private static final String PARENT_CATALOGUE_NAME = "PARENT_CATALOGUE_NAME";

    private String mDictionaryName;
    private String mCatalogueName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dictionaryName name of the dictionary
     * @return A new instance of fragment DictionaryFragment.
     */
    public static DictionaryFragment newInstance(String parentCatalogueName, String dictionaryName) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(DICTIONARY_NAME, dictionaryName);
        args.putString(PARENT_CATALOGUE_NAME, parentCatalogueName);

        fragment.setArguments(args);
        return fragment;
    }

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDictionaryName = getArguments().getString(DICTIONARY_NAME);
            mCatalogueName = getArguments().getString(PARENT_CATALOGUE_NAME);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DictionarySQLManager sqlManager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        Cursor mRawCursor = sqlManager.getAllDictionaryObjectsCursor(mCatalogueName, mDictionaryName);
        mAdapter = new DictionaryAdapter(getActivity().getApplicationContext(), R.layout.std_list_fragment, mRawCursor, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_dictionary);
        ViewTools.setSubtitle(getActivity(), mDictionaryName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //go to create wordFragment
        CreateWordFragment fragment = CreateWordFragment.newInstance(mDictionaryName, mCatalogueName);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();
        return true;
    }

}
