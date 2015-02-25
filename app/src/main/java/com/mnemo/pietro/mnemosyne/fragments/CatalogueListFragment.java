package com.mnemo.pietro.mnemosyne.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.CatalogueAdaptater;

import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CatalogueListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CatalogueListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogueListFragment extends Fragment implements AbsListView.OnItemClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATALOGUE_NAME = "catalogue_name";
    private String m_sCatalogueName;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;
    private CatalogueAdaptater mAdaptater;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_catalogue_list, container, false);
        mListView = (ListView) viewRoot.findViewById(R.id.catListView);
        mAdaptater = new CatalogueAdaptater(getActivity());
        mListView.setAdapter(mAdaptater);
        return viewRoot;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mListener != null){
            mListener.onCatalogueSelected(CatalogueListSingleton.getInstance(getActivity()).getElement(position).getName());
        }
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
