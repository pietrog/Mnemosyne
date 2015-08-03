package com.mnemo.pietro.mnemosyne;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;


/**
 * Created by pietro on 26/07/15.
 */
public class MnemoDictionary extends AppCompatActivity {

    public static final String ID = "mnemocatalogue.id";
    public static final String NAME = "mnemocatalogue.name";

    private long mID;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_dictionary);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabDictionary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MnemoCreation.class);
                intent.putExtra(MnemoCreation.DICTIONARYID, mID);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null){
            mID = getIntent().getLongExtra(ID, -1);
            mName = getIntent().getStringExtra(NAME);

            //create the catalogue list fragment
            DictionaryFragment fgt = DictionaryFragment.newInstance(mID, mName);
            getSupportFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).commit();
        }
    }
}
