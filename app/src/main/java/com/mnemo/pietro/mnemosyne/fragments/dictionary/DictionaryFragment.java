package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.DictionaryAdapter;

import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.dictionary.sql.DictionaryContract;
import model.dictionary.dictionary.sql.DictionaryDBHelper;
import model.dictionary.tools.Logger;

/**
 * Dictionary fragment. Manages the layout of a dictionay, add and remove words, ...
 */
public class DictionaryFragment extends ListFragment implements View.OnClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DICTIONARY_NAME = "DICTIONARY_NAME";
    private static final String PARENT_CATALOGUE_NAME = "PARENT_CATALOGUE_NAME";

    private String mDictionaryName;
    private String mParentCatalogueName;
    private Cursor mRawCursor;
    private DictionaryAdapter mAdapter;

    private OnDictionaryFragmentInteractionListener mListener;

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
            mParentCatalogueName = getArguments().getString(PARENT_CATALOGUE_NAME);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DictionaryDBHelper dbhelper = new DictionaryDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM " + DictionaryContract.Dictionary.TABLE_NAME + " WHERE " + DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME + " = '" + mParentCatalogueName + "' and " + DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME + " = '" + mDictionaryName +"'";
        mRawCursor = db.rawQuery(query, null);
        mAdapter = new DictionaryAdapter(this, getActivity().getApplicationContext(), R.layout.dictionary_fragment, mRawCursor, 0);
        setListAdapter(mAdapter);
    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDictionaryFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDictionaryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.dictionaryFragmentVisible(mDictionaryName);
    }

    @Override
    public void onStop() {
        super.onStop();

        //close the cursor before leaving
        if (mRawCursor != null && ! mRawCursor.isClosed())
            mRawCursor.close();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDictionaryFragmentInteractionListener {
        public void dictionaryFragmentVisible(String dictionaryName);
    }


    @Override
    public void onClick(View v) {
        String word = (String)v.getTag();
        if (word == null){
            Logger.d("DictionaryFragment::onClick"," Word not found, tag not set.");
            return;
        }
        Dictionary current = CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mParentCatalogueName).getDictionary(mDictionaryName);
        current.removeDictionaryObject(word);
        mAdapter.notifyDataSetChanged();
        DictionaryDBHelper dbhelper = new DictionaryDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM " + DictionaryContract.Dictionary.TABLE_NAME + " WHERE " + DictionaryContract.Dictionary.COLUMN_NAME_CATALOGUE_NAME + " = '" + mParentCatalogueName + "' and " + DictionaryContract.Dictionary.COLUMN_NAME_DICTIONARY_NAME + " = '" + mDictionaryName +"'";
        mRawCursor = db.rawQuery(query, null);
        mAdapter = new DictionaryAdapter(this, getActivity().getApplicationContext(), R.layout.dictionary_fragment, mRawCursor, 0);
        setListAdapter(mAdapter);
        Logger.d("DictionaryFragment::onClick"," Word "+ word + " removed from " + mDictionaryName);
    }
}
