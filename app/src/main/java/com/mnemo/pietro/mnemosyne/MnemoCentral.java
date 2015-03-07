package com.mnemo.pietro.mnemosyne;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mnemo.pietro.mnemosyne.fragments.CatalogueFragment;
import com.mnemo.pietro.mnemosyne.fragments.CatalogueListFragment;
import com.mnemo.pietro.mnemosyne.fragments.CreateDictionaryFragment;

import model.dictionary.catalogue.CatalogueList;
import model.dictionary.tools.Logger;


public class MnemoCentral
        extends ActionBarActivity
        implements CatalogueListFragment.OnCatalogueListFragmentInteractionListener, CatalogueFragment.OnCatalogueFragmentInteractionListener{

    private static final String FGT_BASE_TAG = "BASECATALOGUE";
    private static final String FGT_CURRENT_CATALOGUE_TAG = "CURRENTCATALOGUE";

    private FragmentManager fgtMng;

    /*******************/
    /// ACTIVITY LIFECYLE
    /*******************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_central);

        if (savedInstanceState == null){
            fgtMng = getFragmentManager();
            //create the catalogue list fragment
            CatalogueListFragment fgt = CatalogueListFragment.newInstance(FGT_BASE_TAG);
            fgtMng.beginTransaction().add(R.id.cat_list_fgt, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mnemo_central, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("MnemoCentral::onResume","");
        //getFragmentManager().beginTransaction()

    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i("MnemoCentral::onPause", "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i("MnemoCentral::onStop", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("MnemoCentral::onDestroy", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*******************/
    /// ONCLICK METHODS
    /*******************/

    public void createCatalogue(View view) {
        Intent intent;

        intent = new Intent(this, CreateCatalogueActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (fgtMng.getBackStackEntryCount() > 0){
            fgtMng.popBackStack();
        }
        else
            super.onBackPressed();
    }






    /**
     * OnCatalogueListFragmentInteractionListener implementation
     */
    @Override
     public void onCatalogueSelected(String name) {
        //hide the new catalogue button
        Button addCatalogueButton = (Button) findViewById(R.id.addCatalogueButton);
        addCatalogueButton.setVisibility(View.INVISIBLE);

        CatalogueFragment cfgt = CatalogueFragment.newInstance(name);
        fgtMng.beginTransaction().replace(R.id.cat_list_fgt, cfgt).addToBackStack(FGT_CURRENT_CATALOGUE_TAG).commit();
    }

    @Override
    public void catalogueListFragmentVisible() {
        Button addCatalogueButton = (Button) findViewById(R.id.addCatalogueButton);
        addCatalogueButton.setVisibility(View.VISIBLE);
    }



    /**
     * OnCatalogueFragmentInteractionListener implementation
     */

    @Override
    public void onDictionarySelected(String name) {
        Logger.i("MnemoCentral::onDictionarySelected", " dictionary selected");
    }


    @Override
    public void catalogueFragmentVisible() {
        Button addCatalogueButton = (Button) findViewById(R.id.addCatalogueButton);
        addCatalogueButton.setVisibility(View.VISIBLE);
    }
}
