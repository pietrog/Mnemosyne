package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;


/**
 * Created by pietro on 26/07/15.
 */
public class MnemoCatalogue extends AppCompatActivity {

    public static final String ID = "mnemocatalogue.id";
    public static final String NAME = "mnemocatalogue.name";

    private long mID;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mnemo_catalogue);

        if (savedInstanceState == null){
            mID = getIntent().getLongExtra(ID, -1);
            mName = getIntent().getStringExtra(NAME);

            //create the catalogue list fragment
            DictionaryFragment fgt = DictionaryFragment.newInstance(mID, mName);
            getSupportFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }

    }
}
