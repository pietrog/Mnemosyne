package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueFragment;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.dictionary.Dictionary;

/**
 * Created by pietro on 03/03/15.
 *
 */
public class CatalogueAdapter extends BaseAdapter {

    private Catalogue mCatalogue = null;
    private LayoutInflater mInflater = null;
    private CatalogueFragment mFrag;

    public CatalogueAdapter(Catalogue catalogue, Context context, CatalogueFragment frag){
        mCatalogue = catalogue;
        mInflater = LayoutInflater.from(context);
        mFrag = frag;
    }

    @Override
    public int getCount() {
        return mCatalogue.getCount();
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

        ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.catalogue_listview_layout, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.removeDict = (Button) convertView.findViewById(R.id.removeButton);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();

        bindView(holder, position);

        return convertView;
    }

    private void bindView(ViewHolder holder, int position){
        Dictionary dict = mCatalogue.getElement(position);
        holder.name.setText(dict.getName());
        holder.description.setText(dict.getDescription());
        holder.removeDict.setOnClickListener(mFrag);
        holder.removeDict.setTag(dict.getName());
    }

    private class ViewHolder{
        public TextView name;
        public TextView description;
        public Button removeDict;
    }
}
