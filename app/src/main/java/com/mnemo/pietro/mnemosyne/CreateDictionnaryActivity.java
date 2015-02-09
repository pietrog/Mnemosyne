package com.mnemo.pietro.mnemosyne;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import model.dictionary.Dictionary;
import model.dictionary.DictionaryHandle;


public class CreateDictionnaryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dictionnary);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_dictionnary, menu);
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

    public void save_dict(View view){
        EditText dictName = (EditText) findViewById(R.id.name_dict);
        EditText dictDesc = (EditText) findViewById(R.id.desc_dict);
        String name = dictName.getText().toString();
        String desc = dictName.getText().toString();

        Dictionary dict = new Dictionary(name, desc);
        DictionaryHandle dictHdl ;
    }
}
