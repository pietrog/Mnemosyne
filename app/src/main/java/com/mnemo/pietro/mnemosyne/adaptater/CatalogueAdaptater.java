package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;

/**
 * Created by pietro on 12/02/15.
 */
public class CatalogueAdaptater extends BaseAdapter {

    private CatalogueList m_oCatalogues;
    private LayoutInflater m_oLayoutInflater;


    public CatalogueAdaptater(Context context, CatalogueList cl){
        m_oCatalogues = cl;
        m_oLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_oCatalogues.getCount();
    }

    @Override
    public Object getItem(int position) {
        return m_oCatalogues.getElement(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null){
            convertView = m_oLayoutInflater.inflate(R.layout.catalogue_listview_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.description = (TextView) convertView.findViewById(R.id.description);
        }
        else
            holder = (ViewHolder)convertView.getTag();

        Catalogue current = (Catalogue) getItem(position);

        if (current != null) {
            holder.name.setText(current.getName());
            holder.description.setText(current.getDescription());
        }

        return convertView;

    }


    private class ViewHolder{
        public TextView name;
        public TextView description;
    }
}


