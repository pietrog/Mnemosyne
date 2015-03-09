package com.mnemo.pietro.mnemosyne;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.tools.Logger;
import model.dictionary.tools.ViewTools;


public class CreateCatalogueActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_catalogue);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_catalogue, menu);
        return true;
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

    /**
     * Save the catalogue (persist it in memory)
     * @param view
     */
    public void save_catalogue(View view){
        String catalogue_name = ViewTools.getStringFromEditableText(findViewById(R.id.name_cat));
        String catalogue_desc = ViewTools.getStringFromEditableText(findViewById(R.id.desc_cat));
        CatalogueList whole = CatalogueListSingleton.getInstance(this);
        Catalogue newCat = Catalogue.createCatalogue(catalogue_name, catalogue_desc, this);
        whole.addCatalogue(newCat);
        whole.writeToJSONFile();

        finish();
    }


    public void comeBack(View view){
        Logger.i("CreateCatalogueActivity::comeBack"," finish activity");
        finish();
    }
}
