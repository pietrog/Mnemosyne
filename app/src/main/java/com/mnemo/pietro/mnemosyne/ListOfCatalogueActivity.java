package com.mnemo.pietro.mnemosyne;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import model.dictionary.catalogue.CatalogueList;


public class ListOfCatalogueActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_catalogue);
        ListView lv = (ListView) findViewById(R.id.catListView);
        CatalogueList cl = CatalogueList.LoadCatalogueListFromJSONFile(this);
        ArrayAdapter<String> adaptater = new ArrayAdapter<String>(this, R.layout.catalogue_list_item, cl.getArrayOfString());
        lv.setAdapter(adaptater);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_catalogue, menu);
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

}
