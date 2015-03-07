package com.mnemo.pietro.mnemosyne.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.DictionaryListAdapter;

import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.dictionary.sql.DictionaryContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CatalogueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CatalogueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogueFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final String CREATE_DICT_FGT_TAG = "CREATE_DICT_FGT_TAG";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATALOGUE_NAME = "catalogue_name";


    private String mCatalogue_name;
    private DictionaryListAdapter mDictionaryListAdapter;


    private OnCatalogueFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogueName Parameter 1.
     * @return A new instance of fragment CatalogueFragment.
     */
    public static CatalogueFragment newInstance(String catalogueName) {
        CatalogueFragment fragment = new CatalogueFragment();
        Bundle args = new Bundle();
        args.putString(CATALOGUE_NAME, catalogueName);
        fragment.setArguments(args);
        return fragment;
    }

    public CatalogueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCatalogue_name = getArguments().getString(CATALOGUE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        Button b = (Button)view.findViewById(R.id.idcreatedict);
        b.setOnClickListener(this);
        mDictionaryListAdapter = new DictionaryListAdapter(CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogue_name), getActivity().getApplicationContext());
        ListView lv = (ListView) view.findViewById(R.id.dictList);
        lv.setAdapter(mDictionaryListAdapter);
        lv.setOnItemClickListener(this);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onDictionarySelected(name);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCatalogueFragmentInteractionListener) activity;
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



    public interface OnCatalogueFragmentInteractionListener {

        public void onDictionarySelected(String name);
        public void catalogueFragmentVisible();
    }

    @Override
    public void onClick(View v) {
        CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(mCatalogue_name);
        getActivity().getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dictionaryName = ((Dictionary)mDictionaryListAdapter.getItem(position)).getName();
        DictionaryFragment fragment = DictionaryFragment.newInstance(mCatalogue_name, dictionaryName);
        getActivity().getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
    }
}
