package com.mnemo.pietro.mnemosyne.fragments.word;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.catalogue.CatalogueListSingleton;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.dictionary.sql.DictionaryDBHelper;
import model.dictionary.tools.ViewTools;

/**
 *
 */
public class CreateWordFragment extends Fragment implements View.OnClickListener{
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
        if (getArguments() != null) {
            mDictionaryName = getArguments().getString(DICTIONARY_NAME);
            mCatalogueName = getArguments().getString(CATALOGUE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootview = inflater.inflate(R.layout.word_create_fragment, container, false);
        Button save = (Button) mRootview.findViewById(R.id.saveButton);
        save.setOnClickListener(this);
        return mRootview;
    }

    @Override
    public void onClick(View v) {
        String word = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.word_name));
        String definition = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.word_definition));
        WordDefinitionObj wordDef = new WordDefinitionObj(word, definition);
        Dictionary dict = CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogueName).getDictionary(mDictionaryName);
        dict.setDBHelper(new DictionaryDBHelper(getActivity().getApplicationContext()));
        dict.addDictionaryObject(wordDef);

        ViewTools.hideKeyboard(v, getActivity());
        getFragmentManager().popBackStack();
    }

}
