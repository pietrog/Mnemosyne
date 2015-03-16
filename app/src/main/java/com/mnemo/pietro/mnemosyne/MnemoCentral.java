package com.mnemo.pietro.mnemosyne;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;


import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueListFragment;



public class MnemoCentral
        extends ActionBarActivity {

    public static final String FGT_CATALOGUE_TAG = "CATALOGUE";
    public static final String FGT_DICTIONARY_TAG = "DICTIONARY";


    /*******************/
    /// ACTIVITY LIFECYLE
    /*******************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_central);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.global_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        if (savedInstanceState == null){
            //create the catalogue list fragment
            CatalogueListFragment fgt = CatalogueListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.cat_list_fgt, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }

    }


    /*******************/
    /// ONCLICK METHODS
    /*******************/

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
