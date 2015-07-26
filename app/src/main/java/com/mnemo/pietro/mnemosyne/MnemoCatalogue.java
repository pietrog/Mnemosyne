package com.mnemo.pietro.mnemosyne;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueFragment;


/**
 * Created by pietro on 26/07/15.
 */
public class MnemoCatalogue extends AppCompatActivity {

    public static final String CATALOGUEID = "mnemocatalogue.catalogueid";
    public static final String CATALOGUENAME = "mnemocatalogue.cataloguename";

    private long mCatalogueID;
    private String mCatalogueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mnemo_catalogue);

        if (savedInstanceState == null){
            mCatalogueID = getIntent().getLongExtra(CATALOGUEID, -1);
            mCatalogueName = getIntent().getStringExtra(CATALOGUENAME);

            //create the catalogue list fragment
            CatalogueFragment fgt = CatalogueFragment.newInstance(mCatalogueID, mCatalogueName);
            getSupportFragmentManager().beginTransaction().add(R.id.main_subscreen, fgt).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }

    }




    /*public static class PagerAdapter extends FragmentStatePagerAdapter {

        private final SparseArray<Long> mIDOfCatalogues = new SparseArray<>();
        private final SparseArray<String> mCatalogueTitles = new SparseArray<>();

        public PagerAdapter(FragmentManager fm, Context context){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mIDOfCatalogues.get(position) == null){

            }
            return mIDOfCatalogues.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mTitles.get(position);
        }
    }*/
}
