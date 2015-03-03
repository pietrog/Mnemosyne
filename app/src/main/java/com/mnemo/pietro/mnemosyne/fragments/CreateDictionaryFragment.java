package com.mnemo.pietro.mnemosyne.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.Global;
import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.tools.Logger;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateDictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDictionaryFragment extends Fragment implements View.OnClickListener{

    private static final String CATALOGUE_NAME = "DICT_NAME";


    //catalogue containing this dictionary
    private String mCatalogueName;
    private View rootView = null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogue_name Parameter 1.
     * @return A new instance of fragment CreateDictionaryFragment.
     */
    public static CreateDictionaryFragment newInstance(String catalogue_name) {
        CreateDictionaryFragment fragment = new CreateDictionaryFragment();
        Bundle args = new Bundle();
        args.putString(CATALOGUE_NAME, catalogue_name);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateDictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCatalogueName = getArguments().getString(CATALOGUE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_dictionary, container, false);
        Button saveDict = (Button)rootView.findViewById(R.id.btn_save);
        saveDict.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        saveDictionary(v);
    }

    public void saveDictionary(View view){

        String dictName = ViewTools.getStringFromEditableText(rootView.findViewById(R.id.dict_name));
        String dictDesc = ViewTools.getStringFromEditableText(rootView.findViewById(R.id.dict_desc));

        Catalogue cat = CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogueName);
        if (cat == null){
            Logger.i("CreateDictionaryFragment::saveDictionary"," catalogue " + cat+ " not found");
            return;
        }

        int res = cat.addDictionary(dictName, dictDesc, true);
        Logger.d("CreateDictionaryFragment::saveDictionary", Global.getLogFromResult(res));

    }


}
