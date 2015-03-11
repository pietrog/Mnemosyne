package com.mnemo.pietro.mnemosyne.fragments.catalogue;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCatalogueFragment extends Fragment implements View.OnClickListener{

    //private static final String GLOBAL_TOOLBAR = "GLOBARTOOLBAR";


    private View rootview;
    private Toolbar mToolbar;

    public CreateCatalogueFragment(){}


    public static CreateCatalogueFragment newInstance(){
        CreateCatalogueFragment fragment = new CreateCatalogueFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.dictionary_create_fragment, container, false);
        mToolbar = (Toolbar)container.getRootView().getRootView().findViewById(R.id.global_toolbar);
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setSubtitle(R.string.action_cat_create);
        Button add = (Button) mToolbar.findViewById(R.id.add_button);
        add.setText(R.string.action_save);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String catalogue_name = ViewTools.getStringFromEditableText(rootview.findViewById(R.id.dict_name));
        String catalogue_desc = ViewTools.getStringFromEditableText(rootview.findViewById(R.id.dict_desc));
        CatalogueList whole = CatalogueListSingleton.getInstance(getActivity().getApplicationContext());
        Catalogue newCat = Catalogue.createCatalogue(catalogue_name, catalogue_desc, getActivity().getApplicationContext());
        whole.addCatalogue(newCat);
        whole.writeToJSONFile();

        ViewTools.hideKeyboard(v, getActivity());
        getFragmentManager().popBackStack();
    }
}
