package com.mnemo.pietro.mnemosyne.fragments.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.library.tools.LibraryAdapter;

import model.dictionary.catalogue.sql.CatalogueSQLManager;

/**
 * Fragment containing the list of all catalogues.
 * You can click on a catalogue to open the catalogue fragment and obtain the list of dictionaries
 *
 */
public class LibraryFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //bind the adapter
        CatalogueSQLManager manager = CatalogueSQLManager.getInstance();
        mAdapter = new LibraryAdapter(manager.getAllDictionary(), getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), MnemoDictionary.class);
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        intent.putExtra(MnemoDictionary.ID, GeneralTools.getLongElement(cursor, CatalogueContract.Catalogue._ID));
        intent.putExtra(MnemoDictionary.NAME, GeneralTools.getStringElement(cursor, CatalogueContract.Catalogue.NAME));
        startActivity(intent);
    }*/

    /*private void removeCatalogue(int position){*/
        /*mAdapter.getCursor().moveToPosition(position);
        long[] listIDs = {GeneralTools.getLongElement(mAdapter.getCursor(), CatalogueContract.Catalogue._ID)};

        CatalogueSQLManager.getInstance().remove(listIDs);
        Logger.i("LibraryFragment::removeCatalogue", " catalogue(s) " + listIDs[0] + " removed");

        mAdapter.changeCursor(CatalogueSQLManager.getInstance().getAll());*/
    /*}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null && mAdapter.getCursor() != null)
            mAdapter.getCursor().close();
    }*/

}
