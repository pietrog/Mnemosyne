package com.mnemo.pietro.mnemosyne;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;


import com.mnemo.pietro.mnemosyne.fragments.library.LibraryFragment;

//@TODO: refaire l'ecran de visionnage d'un mot
//@TODO: implementer un rappel basé sur l'alarme
//@TODO: impémenter le report de jour pour les mots n'ayant pas été revisés le jour ou ils auraient du
//@TODO: finir l'ecran d'affichage des mot a réviser, avec couleurs pour noter l'importance(jours de retard)
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
            LibraryFragment fgt = LibraryFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
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
        menu.getItem(1).setVisible(false);
        return true;
    }

}
