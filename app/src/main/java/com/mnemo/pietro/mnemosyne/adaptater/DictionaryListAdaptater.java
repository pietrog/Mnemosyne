package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueListSingleton;

/**
 * Created by pietro on 03/03/15.
 */
public class DictionaryListAdaptater extends BaseAdapter {

    private Catalogue mCatalogue = null;
    private Context mContext = null;

    public DictionaryListAdaptater (Catalogue catalogue, Context context){
        mCatalogue = catalogue;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCatalogue;
    }

    @Override
    public Object getItem(int position) {
        return mCatalogue.getElement(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
