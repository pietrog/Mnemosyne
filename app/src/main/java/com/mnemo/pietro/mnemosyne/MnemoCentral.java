package com.mnemo.pietro.mnemosyne;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mnemo.pietro.mnemosyne.fragments.CatalogueListFragment;

import model.dictionary.catalogue.CatalogueList;
import model.dictionary.tools.Logger;


public class MnemoCentral extends ActionBarActivity implements CatalogueListFragment.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_central);

        if (savedInstanceState == null){
            //create the catalogue list fragment
            CatalogueListFragment fgt = CatalogueListFragment.newInstance("BASECATALOGUE");
            getFragmentManager().beginTransaction().add(R.id.cat_list_fgt, fgt).addToBackStack("myfrag").commit();
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

    public void createCatalogue(View view) {
        Intent intent;

        intent = new Intent(this, CreateCatalogueActivity.class);
        startActivity(intent);
    }


    @Override
    public void onCatalogueSelected(String name) {
        String currentName = name;
        currentName += " : coucou";
    }
}
