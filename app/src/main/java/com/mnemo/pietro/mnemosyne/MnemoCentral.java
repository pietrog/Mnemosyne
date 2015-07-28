package com.mnemo.pietro.mnemosyne;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.TodayListFragment;
import com.mnemo.pietro.mnemosyne.fragments.library.LibraryFragment;

import java.util.ArrayList;
import java.util.List;

import model.dictionary.Global;
import model.dictionary.tools.MnemoDBHelper;
import model.dictionary.tools.TestEnvironment;
import model.dictionary.tools.ViewTools;

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


    ViewPager mViewPager ;
    CentralPagerAdapter mPageAdapter;

    /*******************/
    /// ACTIVITY LIFECYLE
    /*******************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_central);

        if (savedInstanceState == null) {
            mPageAdapter = new CentralPagerAdapter(getSupportFragmentManager(), getApplicationContext());
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mPageAdapter);
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
        ViewTools.setTitle(this, R.string.app_name);
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


    public static class CentralPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mTitles = new ArrayList<>();

        public CentralPagerAdapter(FragmentManager fm, Context context){
            super(fm);

            mFragments.add(LibraryFragment.newInstance());
            mTitles.add(context.getString(R.string.hint_catalogue_list));

            mFragments.add(TodayListFragment.newInstance(-1));
            mTitles.add(context.getString(R.string.hint_todayslist));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mTitles.get(position);
        }
    }
}
