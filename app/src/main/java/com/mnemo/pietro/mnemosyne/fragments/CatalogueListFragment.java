package com.mnemo.pietro.mnemosyne.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.adaptater.CatalogueListAdapter;

import model.dictionary.catalogue.Catalogue;

/**
 * Fragment containing the list of all catalogues.
 * You can click on a catalogue to open the catalogue fragment and obtain the list of dictionaries
 *
 */
public class CatalogueListFragment extends ListFragment{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATALOGUE_NAME = "catalogue_name";
    private String m_sCatalogueName;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;
    private CatalogueListAdapter mAdaptater;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogueName name of the catalogue
     * @return A new instance of fragment CatalogueListFragment.
     */
    public static CatalogueListFragment newInstance(String catalogueName) {
        CatalogueListFragment fragment = new CatalogueListFragment();
        Bundle args = new Bundle();
        args.putString(CATALOGUE_NAME, catalogueName);
        fragment.setArguments(args);
        return fragment;
    }

    public CatalogueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            m_sCatalogueName = getArguments().getString(CATALOGUE_NAME);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdaptater = new CatalogueListAdapter(getActivity());
        setListAdapter(mAdaptater);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mListener != null)
            mListener.onCatalogueSelected(((Catalogue)mAdaptater.getItem(position)).getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public interface OnFragmentInteractionListener {
        public void onCatalogueSelected(String catalogueName);
    }

}
