package com.mnemo.pietro.mnemosyne.fragments.catalogue;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.MnemoCentral;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.CatalogueListAdapter;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.tools.ViewTools;

/**
 * Fragment containing the list of all catalogues.
 * You can click on a catalogue to open the catalogue fragment and obtain the list of dictionaries
 *
 */
public class CatalogueListFragment extends ListFragment {

    private CatalogueListAdapter mAdaptater;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param catalogueName name of the catalogue
     * @return A new instance of fragment CatalogueListFragment.
     */
    public static CatalogueListFragment newInstance(/*String catalogueName*/) {
        return new CatalogueListFragment();
    }

    public CatalogueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // for toolbar interaction
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdaptater = new CatalogueListAdapter(getActivity());
        registerForContextMenu(getListView());
        setListAdapter(mAdaptater);
    }

    @Override

    public void onListItemClick(ListView l, View v, int position, long id) {
        CatalogueFragment cfgt = CatalogueFragment.newInstance(((Catalogue)mAdaptater.getItem(position)).getName());
        getFragmentManager().beginTransaction().replace(R.id.cat_list_fgt, cfgt).addToBackStack(MnemoCentral.FGT_CATALOGUE_TAG).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdaptater.notifyDataSetChanged();
        ViewTools.setTitle(getActivity(), R.string.hint_catalogue_list);
        ViewTools.setSubtitle(getActivity(), "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CreateCatalogueFragment fragment = CreateCatalogueFragment.newInstance();
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
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
        switch(item.getItemId()){

            case R.id.remove_item:
                removeCatalogue(info.position);
                return true;
        }
        return true;
    }

    private void removeCatalogue(int position){
        String name = ((Catalogue)mAdaptater.getItem(position)).getName();
        CatalogueList cl = CatalogueListSingleton.getInstance(getActivity().getApplicationContext());
        cl.removeCatalogue(name);
        mAdaptater.notifyDataSetChanged();
    }
}
