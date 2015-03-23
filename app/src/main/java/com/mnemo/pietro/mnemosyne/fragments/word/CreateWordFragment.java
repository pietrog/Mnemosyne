package com.mnemo.pietro.mnemosyne.fragments.word;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mnemo.pietro.mnemosyne.MnemoMemoryManager;
import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.dictionary.sql.DictionaryDBHelper;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.MemoryManagerSingleton;
import model.dictionary.tools.ViewTools;

/**
 *
 */
public class CreateWordFragment extends Fragment {
    private static final String DICTIONARY_NAME = "DICT_NAME";
    private static final String CATALOGUE_NAME = "CAT_NAME";

    private String mDictionaryName;
    private String mCatalogueName;

    private View mRootview;

    public static CreateWordFragment newInstance(String dict_name, String cat_name) {
        CreateWordFragment fragment = new CreateWordFragment();
        Bundle args = new Bundle();
        args.putString(DICTIONARY_NAME, dict_name);
        args.putString(CATALOGUE_NAME, cat_name);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateWordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mDictionaryName = getArguments().getString(DICTIONARY_NAME);
            mCatalogueName = getArguments().getString(CATALOGUE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootview = inflater.inflate(R.layout.word_create_fragment, container, false);
        return mRootview;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.validate:
                saveDictionaryObject();
                return true;
            default:
                return true;
        }
    }

    private void saveDictionaryObject(){
        String word = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.name));
        String definition = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.description));
        WordDefinitionObj wordDef = new WordDefinitionObj(1, word, definition);
        Dictionary dict = CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogueName).getDictionary(mDictionaryName);
        dict.setDBHelper(new DictionaryDBHelper(getActivity().getApplicationContext()));
        long rowID = dict.addDictionaryObject(wordDef);


        MemoryManager mgr = MemoryManagerSingleton.getInstance(getActivity().getApplicationContext());
        long id = mgr.addWordManagerForToday(rowID);


        ViewTools.hideKeyboard(mRootview, getActivity());
        getFragmentManager().popBackStack();

        MnemoMemoryManager.startActionRiseTodayList(getActivity().getApplicationContext(), "");
    }

}
