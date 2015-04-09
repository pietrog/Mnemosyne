package com.mnemo.pietro.mnemosyne.fragments.catalogue;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.MnemoCentral;
import com.mnemo.pietro.mnemosyne.MnemoMemoryManager;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.tools.CatalogueAdapter;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.CreateDictionaryFragment;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;

import model.dictionary.catalogue.sql.CatalogueContract;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.tools.ViewTools;


/**
 * Displays all the dictionaries related to given catalogue, identified by mCatalogueID
 */
public class CatalogueFragment extends ListFragment {

    //private static final String CREATE_DICT_FGT_TAG = "CREATE_DICT_FGT_TAG";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATALOGUE_NAME = "catalogue_name";
    private static final String CATALOGUE_ID = "catalogue_id";


    private String mCatalogueName;
    private long mCatalogueID;
    private CatalogueAdapter mAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogueName Parameter 1.
     * @return A new instance of fragment CatalogueFragment.
     */
    public static CatalogueFragment newInstance(long id, String catalogueName) {
        CatalogueFragment fragment = new CatalogueFragment();
        Bundle args = new Bundle();
        args.putString(CATALOGUE_NAME, catalogueName);
        args.putLong(CATALOGUE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public CatalogueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mCatalogueName = getArguments().getString(CATALOGUE_NAME);
            mCatalogueID = getArguments().getLong(CATALOGUE_ID);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //bind the adapter
        mAdapter = new CatalogueAdapter(getActivity().getApplicationContext(),R.layout.std_list_fragment, CatalogueSQLManager.getInstance().getAllDictionaryOfCatalogue(mCatalogueID), 0);
        setListAdapter(mAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_catalogue);
        ViewTools.setSubtitle(getActivity(), mCatalogueName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_item:
                CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(mCatalogueID);
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();
                break;
            case R.id.raiseAlert:
                MnemoMemoryManager.startActionRiseTodayList(getActivity().getApplicationContext());
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Dictionary dictionary = Dictionary.LoadFromCursor(cursor);
        DictionaryFragment fragment = DictionaryFragment.newInstance(dictionary.getID(), dictionary.getName());
        getActivity().getFragmentManager().beginTransaction().addToBackStack(MnemoCentral.FGT_DICTIONARY_TAG).replace(R.id.main_subscreen, fragment).commit();
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
                removeDictionary(info.position);
                return true;
            default:
                return true;
        }
    }

    private void removeDictionary(int position){
        mAdapter.getCursor().moveToPosition(position);
        long[] listIDs = {GeneralTools.getLongElement(mAdapter.getCursor(), CatalogueContract.Catalogue._ID)};

        CatalogueSQLManager.getInstance().remove(listIDs);
        Logger.i("CatalogueFragment::removeCatalogue", " dictionaries(s) " + listIDs[0] + " removed");

        //refresh the view
        mAdapter.changeCursor(CatalogueSQLManager.getInstance().getAllDictionaryOfCatalogue(mCatalogueID));
    }
}
