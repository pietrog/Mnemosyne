package com.mnemo.pietro.mnemosyne.fragments.catalogue;

import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.MnemoCentral;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.adaptater.CatalogueAdapter;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.CreateDictionaryFragment;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.DictionaryFragment;

import model.dictionary.catalogue.Catalogue;
import model.dictionary.catalogue.CatalogueList;
import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.tools.ViewTools;


public class CatalogueFragment extends Fragment implements AdapterView.OnItemClickListener{

    //private static final String CREATE_DICT_FGT_TAG = "CREATE_DICT_FGT_TAG";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATALOGUE_NAME = "catalogue_name";


    private String mCatalogue_name;
    private CatalogueAdapter mCatalogueAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogueName Parameter 1.
     * @return A new instance of fragment CatalogueFragment.
     */
    public static CatalogueFragment newInstance(String catalogueName) {
        CatalogueFragment fragment = new CatalogueFragment();
        Bundle args = new Bundle();
        args.putString(CATALOGUE_NAME, catalogueName);
        fragment.setArguments(args);
        return fragment;
    }

    public CatalogueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mCatalogue_name = getArguments().getString(CATALOGUE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.catalogue_fragment, container, false);
        mCatalogueAdapter = new CatalogueAdapter(CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogue_name), getActivity().getApplicationContext());
        ListView lv = (ListView) view.findViewById(R.id.dictList);
        lv.setAdapter(mCatalogueAdapter);
        lv.setOnItemClickListener(this);
        registerForContextMenu(lv);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_catalogue);
        ViewTools.setSubtitle(getActivity(), mCatalogue_name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CreateDictionaryFragment fragment = CreateDictionaryFragment.newInstance(mCatalogue_name);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cat_list_fgt, fragment).commit();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dictionaryName = ((Dictionary) mCatalogueAdapter.getItem(position)).getName();
        DictionaryFragment fragment = DictionaryFragment.newInstance(mCatalogue_name, dictionaryName);
        getActivity().getFragmentManager().beginTransaction().addToBackStack(MnemoCentral.FGT_DICTIONARY_TAG).replace(R.id.cat_list_fgt, fragment).commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_content_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.remove_item:
                removeDictionary(info.position);
                return true;
            default:
                return true;
        }
    }

    private void removeDictionary(int position){
        String dictionaryName = ((Dictionary)mCatalogueAdapter.getItem(position)).getName();
        CatalogueList singleton = CatalogueListSingleton.getInstance(getActivity().getApplicationContext());
        Catalogue toRemove = singleton.getCatalogue(mCatalogue_name);
        toRemove.removeDictionary(dictionaryName);
        mCatalogueAdapter.notifyDataSetChanged();
    }
}
