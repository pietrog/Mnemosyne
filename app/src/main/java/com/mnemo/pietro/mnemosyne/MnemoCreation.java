package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.dictionary.CreateDictionaryFragment;

/**
 * Created by pietro on 27/07/15.
 */
public class MnemoCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mnemo_catalogue);

        CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(-1);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.main_subscreen, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            this.finish();

    }
}
