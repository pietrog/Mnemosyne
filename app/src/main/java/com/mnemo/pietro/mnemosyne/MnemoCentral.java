package com.mnemo.pietro.mnemosyne;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueFragment;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueListFragment;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CreateCatalogueFragment;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.CreateDictionaryFragment;
import com.mnemo.pietro.mnemosyne.fragments.word.CreateWordFragment;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;

import model.dictionary.tools.Logger;


public class MnemoCentral
        extends ActionBarActivity
        implements CatalogueListFragment.OnCatalogueListFragmentInteractionListener,
        CatalogueFragment.OnCatalogueFragmentInteractionListener,
        DictionaryFragment.OnDictionaryFragmentInteractionListener,
        CreateWordFragment.OnWordFragmentInteractionListener,
        View.OnClickListener{

    public static final String FGT_CATALOGUE_LIST_TAG = "CATALOGUELIST";
    public static final String FGT_CATALOGUE_TAG = "CATALOGUE";
    public static final String FGT_DICTIONARY_TAG = "DICTIONARY";


    private Toolbar mToolbar;
    private String mCurrentCatalogueName;
    private String mCurrentDictionaryName;
    private Button mToolbar_add_button;

    /*******************/
    /// ACTIVITY LIFECYLE
    /*******************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_central);

        mToolbar = (Toolbar) findViewById(R.id.global_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar_add_button = (Button)mToolbar.findViewById(R.id.add_button);
        }

        if (savedInstanceState == null){
            //create the catalogue list fragment
            CatalogueListFragment fgt = CatalogueListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.cat_list_fgt, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
        else{

        }

    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();

        if (tag == null) {
            Logger.d("MnemoCentral::onClick", " No tag found");
            return;
        }

        switch (tag.toString()){
            case FGT_CATALOGUE_LIST_TAG:
                onClickCatalogueListFgt(v);
                break;
            case FGT_CATALOGUE_TAG:
                onCLickCatalogueFgt(v);
                break;
            case FGT_DICTIONARY_TAG:
                onClickDictionaryFgt(v);
                break;
        }
    }

    /*******************/
    /// ONCLICK METHODS
    /*******************/

    private void onClickCatalogueListFgt(View v){
        //launch fragment createCatalogue
        CreateCatalogueFragment fragment = CreateCatalogueFragment.newInstance();
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
    }

    private void onCLickCatalogueFgt(View view){
        CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(mCurrentCatalogueName);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
    }

    private void onClickDictionaryFgt(View view){
        CreateWordFragment fragment = CreateWordFragment.newInstance(mCurrentDictionaryName, mCurrentCatalogueName);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        else
            super.onBackPressed();
    }



    /**
     * OnCatalogueListFragmentInteractionListener implementation
     */

    @Override
    public void catalogueListFragmentVisible() {
        mToolbar.setSubtitle(R.string.hint_catalogue_list);
        mToolbar_add_button.setText(R.string.action_cat_create);
        mToolbar_add_button.setOnClickListener(this);
        mToolbar_add_button.setTag(FGT_CATALOGUE_LIST_TAG);
    }



    /**
     * OnCatalogueFragmentInteractionListener implementation
     */


    @Override
    public void catalogueFragmentVisible(String catalogueName) {
        mToolbar.setSubtitle(R.string.hint_catalogue);
        mToolbar_add_button.setText(R.string.action_dict_create);
        mToolbar_add_button.setOnClickListener(this);
        mToolbar_add_button.setTag(FGT_CATALOGUE_TAG);
        mCurrentCatalogueName = catalogueName;
    }

    /**
     * OnDictionaryFragmentInteractionListener implementation
     */
    @Override
    public void dictionaryFragmentVisible(String dictionaryName) {
        mToolbar.setSubtitle(R.string.hint_dictionary);
        mToolbar_add_button.setText(R.string.action_word_create);
        mToolbar_add_button.setOnClickListener(this);
        mToolbar_add_button.setTag(FGT_DICTIONARY_TAG);
        mCurrentDictionaryName = dictionaryName;
    }

    /**
     *
     * @param dictionaryName gg
     * @param catalogueName ggg
     */
    @Override
    public void createWordFragmentVisible(String dictionaryName, String catalogueName) {

    }
}
