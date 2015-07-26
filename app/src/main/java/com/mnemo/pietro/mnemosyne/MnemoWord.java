package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;

/**
 * Created by pietro on 26/07/15.
 */
public class MnemoWord extends AppCompatActivity{

    private String mWord;
    private String mDefinition;
    private long mID;
    private long mDictObjID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mnemo_catalogue);

        if (savedInstanceState == null){
            mWord = getIntent().getStringExtra(WordFragment.WORD);
            mDefinition = getIntent().getStringExtra(WordFragment.DEFINITION);
            mID = getIntent().getLongExtra(WordFragment.WORDID, -1);
            mDictObjID = getIntent().getLongExtra(WordFragment.DICTIONARYOBJECTID, -1);

            WordFragment fragment = WordFragment.newInstance(mWord, mDefinition, mID, mDictObjID);
            getSupportFragmentManager().beginTransaction().add(R.id.main_subscreen, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }

    }
}
