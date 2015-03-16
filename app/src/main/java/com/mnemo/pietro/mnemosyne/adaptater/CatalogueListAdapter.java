package com.mnemo.pietro.mnemosyne.adaptater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.catalogue.CatalogueListFragment;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;

/**
 * Created by pietro on 12/02/15.
 *
 *
 */
public class CatalogueListAdapter extends BaseAdapter {

    private CatalogueList m_oCatalogues;
    private LayoutInflater m_oLayoutInflater;
    private CatalogueListFragment frag;


    public CatalogueListAdapter(Context context, CatalogueListFragment frag){
        m_oCatalogues = CatalogueListSingleton.getInstance(context);
        m_oLayoutInflater = LayoutInflater.from(context);
        this.frag = frag;
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
            holder.removeBox = (ImageButton) convertView.findViewById(R.id.removeButton);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        bindView(holder, position);

        return convertView;

    }



    private void bindView(ViewHolder holder, int position){
        Catalogue current = (Catalogue) getItem(position);
        holder.name.setText(current.getName());
        holder.description.setText(current.getDescription());
        holder.removeBox.setOnClickListener(frag);
        holder.removeBox.setTag(current.getName());
    }


    private class ViewHolder{
        public TextView name;
        public TextView description;
        public ImageButton removeBox;
    }
}


