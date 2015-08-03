package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;
import com.mnemo.pietro.mnemosyne.fragments.word.WordFragmentRev;

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

        setContentView(R.layout.activity_mnemo_dictionary);

        if (savedInstanceState == null){
            mWord = getIntent().getStringExtra(WordFragment.WORD);
            mDefinition = getIntent().getStringExtra(WordFragment.DEFINITION);
            mID = getIntent().getLongExtra(WordFragment.WORDID, -1);
            mDictObjID = getIntent().getLongExtra(WordFragment.DICTIONARYOBJECTID, -1);

            WordFragment fragment = WordFragmentRev.newInstance(mWord, mDefinition, mID, mDictObjID);
            getSupportFragmentManager().beginTransaction().add(R.id.main_subscreen, fragment).addToBackStack(null).commit();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
}
