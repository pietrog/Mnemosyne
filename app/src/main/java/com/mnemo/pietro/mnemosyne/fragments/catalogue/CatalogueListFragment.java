package com.mnemo.pietro.mnemosyne.fragments.catalogue;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.MnemoCentral;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.CatalogueListAdapter;

import model.dictionary.catalogue.Catalogue;

/**
 * Fragment containing the list of all catalogues.
 * You can click on a catalogue to open the catalogue fragment and obtain the list of dictionaries
 *
 */
public class CatalogueListFragment extends ListFragment{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String CATALOGUE_NAME = "catalogue_name";
    //private String m_sCatalogueName;

    private OnCatalogueListFragmentInteractionListener mListener;

    private CatalogueListAdapter mAdaptater;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param catalogueName name of the catalogue
     * @return A new instance of fragment CatalogueListFragment.
     */
    public static CatalogueListFragment newInstance(/*String catalogueName*/) {
        CatalogueListFragment fragment = new CatalogueListFragment();
        Bundle args = new Bundle();
        //args.putString(CATALOGUE_NAME, catalogueName);
        fragment.setArguments(args);
        return fragment;
    }

    public CatalogueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null)
            //m_sCatalogueName = getArguments().getString(CATALOGUE_NAME);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdaptater = new CatalogueListAdapter(getActivity());
        setListAdapter(mAdaptater);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CatalogueFragment cfgt = CatalogueFragment.newInstance(((Catalogue)mAdaptater.getItem(position)).getName());
        getFragmentManager().beginTransaction().replace(R.id.cat_list_fgt, cfgt).addToBackStack(MnemoCentral.FGT_CATALOGUE_TAG).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCatalogueListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
        mAdaptater.notifyDataSetChanged();
        mListener.catalogueListFragmentVisible(); //visibility for add catalogue button
    }


    public interface OnCatalogueListFragmentInteractionListener {
        public void catalogueListFragmentVisible();
    }

}
