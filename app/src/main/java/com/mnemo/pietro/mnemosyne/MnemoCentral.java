package com.mnemo.pietro.mnemosyne;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.TodayListFragment;
import com.mnemo.pietro.mnemosyne.fragments.library.LibraryFragment;

import model.dictionary.Global;
import model.dictionary.tools.MnemoDBHelper;
import model.dictionary.tools.TestEnvironment;

//EN COURS
//@TODO: modifier l'ecran d'affichage des mot a réviser, avec couleurs pour noter l'importance(jours de retard)

//A FAIRE
//@TODO: implementer la gestion des jours de retard dans l'apprentissage
//@TODO: implementer un rappel basé sur l'alarme
//@TODO: definir une regle de navigation dans l'appli (d'un dictionaire a l'autre, ...) coherente pour l'utilisateur
//@TODO: implementer la modification des entites
//@TODO: implementer la differenciation des cartes  (item date, image, definition, ...)


public class MnemoCentral
        extends AppCompatActivity{

    public static final String EXTRA_ALERT_DATE = "com.mnemo.pietro.ALERT_DATE";

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
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            setSupportActionBar(mToolbar);
        }

        if (savedInstanceState == null){
            if (getIntent() != null && getIntent().getLongExtra(EXTRA_ALERT_DATE, -1) >= 0){
                TodayListFragment fgt = TodayListFragment.newInstance(getIntent().getLongExtra(EXTRA_ALERT_DATE, -1));
                getFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
            else {
                //create the catalogue list fragment
                LibraryFragment fgt = LibraryFragment.newInstance();
                getFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        }

        // init the system, database helpers, ...
        InitSystem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShutdownSystem();
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
     * @return {Global.SUCCESS} if successful
     */
    private int InitSystem(){
        MnemoMemoryManager.initSystem(getApplicationContext());
        TestEnvironment.initDBSetOfTest();
        return Global.SUCCESS;
    }

    /**
     * Shutdown the system, called when activity is destroyed
     * @return {Global.SUCCESS} if successful
     */
    private int ShutdownSystem(){

        MnemoDBHelper.release();
        return Global.SUCCESS;
    }

}
