package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.CreateDictionaryFragment;
import com.mnemo.pietro.mnemosyne.fragments.word.CreateWordFragment;

/**
 * Created by pietro on 27/07/15.
 */
public class MnemoCreation extends AppCompatActivity {

    public static final String DICTIONARYID = "dictionaryID";

    private long mDictionaryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemo_dictionary);

        mDictionaryID = getIntent().getLongExtra(DICTIONARYID, -1);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabDictionary);
        fab.setImageResource(R.drawable.ic_check_white_48dp);

        if (mDictionaryID == -1) {
            CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(-1);
            fab.setOnClickListener(fragment);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.main_subscreen, fragment).commit();
        }
        else{
            CreateWordFragment fragment = CreateWordFragment.newInstance(mDictionaryID);
            fab.setOnClickListener(fragment);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.main_subscreen, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            this.finish();
    }
}
