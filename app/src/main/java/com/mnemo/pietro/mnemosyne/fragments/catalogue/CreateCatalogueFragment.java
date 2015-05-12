package com.mnemo.pietro.mnemosyne.fragments.catalogue;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCatalogueFragment extends Fragment {

    private View rootview;

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
        rootview = inflater.inflate(R.layout.catalogue_create_fragment, container, false);
        setHasOptionsMenu(true); // for toolbar communcation
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.action_cat_create);
        ViewTools.setSubtitle(getActivity(), "");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.validate:
                saveCatalogue();
                return true;
            default:
                return true;
        }
    }

    private void saveCatalogue(){
        String catalogue_name = ViewTools.getStringFromEditableText(rootview.findViewById(R.id.name));
        String catalogue_desc = ViewTools.getStringFromEditableText(rootview.findViewById(R.id.description));
        CatalogueSQLManager manager = CatalogueSQLManager.getInstance();
        long id = manager.add(catalogue_name, catalogue_desc);

        if (id == Global.FAILURE)
            Toast.makeText(getActivity().getApplicationContext(), "Catalogue " + catalogue_name + " already exists.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity().getApplicationContext(), "Catalogue " + catalogue_name + " added to library.", Toast.LENGTH_SHORT).show();

        //hide keyboard
        ViewTools.hideKeyboard(rootview, getActivity());
        //replace the fragment
        getFragmentManager().popBackStack();
    }
}
