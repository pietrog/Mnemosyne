package com.mnemo.pietro.mnemosyne.fragments.library;

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
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueFragment;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CreateCatalogueFragment;
import com.mnemo.pietro.mnemosyne.fragments.library.tools.LibraryAdapter;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.library.sql.LibraryContract;
import model.dictionary.library.sql.LibrarySQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;
import model.dictionary.tools.ViewTools;

/**
 * Fragment containing the list of all catalogues.
 * You can click on a catalogue to open the catalogue fragment and obtain the list of dictionaries
 *
 */
public class LibraryFragment extends ListFragment {

    private LibraryAdapter mAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param catalogueName name of the catalogue
     * @return A new instance of fragment LibraryFragment.
     */
    public static LibraryFragment newInstance(/*String catalogueName*/) {
        return new LibraryFragment();
    }

    public LibraryFragment() {
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

        //bind the adapter
        LibrarySQLManager manager = LibrarySQLManager.getInstance(getActivity().getApplicationContext());
        mAdapter = new LibraryAdapter(getActivity().getApplicationContext(), R.layout.library_view, manager.getAll(), 0);
        setListAdapter(mAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Catalogue catalogue = Catalogue.LoadFromSQL(cursor);
        CatalogueFragment cfgt = CatalogueFragment.newInstance(catalogue.getID(), catalogue.getName(), catalogue.getDescription());
        getFragmentManager().beginTransaction().replace(R.id.main_subscreen, cfgt).addToBackStack(MnemoCentral.FGT_CATALOGUE_TAG).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        ViewTools.setTitle(getActivity(), R.string.hint_catalogue_list);
        ViewTools.setSubtitle(getActivity(), "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CreateCatalogueFragment fragment = CreateCatalogueFragment.newInstance();
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
        switch(item.getItemId()){

            case R.id.remove_item:
                removeCatalogue(info.position);
                return true;
        }
        return true;
    }

    private void removeCatalogue(int position){
        mAdapter.getCursor().moveToPosition(position);
        long[] listIDs = {GeneralTools.getLongElement(mAdapter.getCursor(), LibraryContract.Library._ID)};

        LibrarySQLManager.getInstance(getActivity().getApplicationContext()).remove(listIDs);
        Logger.i("LibraryFragment::removeCatalogue", " catalogue(s) " + listIDs[0] + " removed");

        mAdapter.notifyDataSetChanged();
        //@todo remove also from memory manager !!!
    }



}
