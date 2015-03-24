package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.DictionaryAdapter;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import com.mnemo.pietro.mnemosyne.fragments.word.CreateWordFragment;
import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;

import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.dictionary.sql.DictionaryOfWordContract;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.tools.ViewTools;

/**
 * Dictionary fragment. Manages the layout of a dictionay, add and remove words, ...
 */
public class DictionaryFragment extends ListFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DICTIONARY_NAME = "DICTIONARY_NAME";
    private static final String PARENT_CATALOGUE_NAME = "PARENT_CATALOGUE_NAME";

    private String mDictionaryName;
    private String mCatalogueName;
    private DictionaryAdapter mAdapter;

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
        setHasOptionsMenu(true);
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
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_dictionary);
        ViewTools.setSubtitle(getActivity(), mDictionaryName);

    }

    @Override
    public void onStop() {
        super.onStop();

        //close the cursor before leaving
        if (mAdapter.getCursor() != null)
            mAdapter.getCursor().close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        WordDefinitionObj obj = WordDefinitionObj.LoadFromCursor(cursor);
        WordFragment fragment = WordFragment.newInstance(obj);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //go to create wordFragment
        CreateWordFragment fragment = CreateWordFragment.newInstance(mDictionaryName, mCatalogueName);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_content_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.remove_item:
                removeWord(info.position);
                return true;
            default:
                return true;
        }
    }

    private void removeWord(int position){
        mAdapter.getCursor().moveToPosition(position);
        long [] id = {GeneralTools.getLongElement(mAdapter.getCursor(), DictionaryContractBase.DictionaryBase._ID)};

        DictionarySQLManager manager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        int result = manager.remove(id);

        Logger.i("DictionaryFragment::removeWord", " Word removed "+ DictionaryOfWordContract.getWord((Cursor) mAdapter.getItem(position)) + ": res => " + result);

        //@todo remove also from memory manager !!!

        DictionarySQLManager sqlManager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        Cursor mRawCursor = sqlManager.getAllDictionaryObjectsCursor(mCatalogueName, mDictionaryName);
        mAdapter.changeCursor(mRawCursor);}
}
