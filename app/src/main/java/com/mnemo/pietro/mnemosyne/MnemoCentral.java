package com.mnemo.pietro.mnemosyne;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;


import com.mnemo.pietro.mnemosyne.fragments.library.LibraryFragment;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.DictionaryObject;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.library.sql.LibrarySQLManager;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.MnemoDBHelper;

//EN COURS
//@TODO: refaire l'ecran de visionnage d'un mot

//A FAIRE
//@TODO: implementer la modification des entites
//@TODO: implementer un rappel basé sur l'alarme
//@TODO: impémenter le report de jour pour les mots n'ayant pas été revisés le jour ou ils auraient du
//@TODO: finir l'ecran d'affichage des mot a réviser, avec couleurs pour noter l'importance(jours de retard)
//@TODO: introduire les foreign keys pour relier les tables et avoir un vrai schema
//@TODO: manager l'initialisation/destruction de certaines ressources(dbhelpers, ...) dans MnemoCentral, rationaliser l'utilissation des dbhelpers
//@TODO: separer la table dictionary de la table word, qui doit etendre dictionary

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

        // init the system, database helpers, ...
        InitSystem();

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

    /**
     * Init singletons, ...
     * @return
     */
    private int InitSystem(){
        CatalogueSQLManager.getInstance(getApplicationContext());
        LibrarySQLManager.getInstance(getApplicationContext());
        DictionarySQLManager.getInstance(getApplicationContext());
        MemoryManagerSQLManager.getInstance(getApplicationContext());
        DictionaryObject.initMemoryPhaseMap();
        return Global.SUCCESS;
    }

    /**
     * Shutdown the system, called when activity is destroyed
     * @return
     */
    private int ShutdownSystem(){

        return Global.SUCCESS;
    }
}
