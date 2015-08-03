package com.mnemo.pietro.mnemosyne.fragments.word;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.ViewTools;

/**
 *
 */
public class CreateWordFragment extends Fragment implements View.OnClickListener{
    private static final String DICTIONARY_ID = "DICTIONARY_ID";

    private long mDictionaryID;

    private View mRootview;

    public static CreateWordFragment newInstance(long dictonaryID) {
        CreateWordFragment fragment = new CreateWordFragment();
        Bundle args = new Bundle();
        args.putLong(DICTIONARY_ID, dictonaryID);
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
        if (getArguments() != null)
            mDictionaryID = getArguments().getLong(DICTIONARY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootview = inflater.inflate(R.layout.word_create_fragment, container, false);
        ViewTools.showKeyboard(mRootview.findViewById(R.id.name), getActivity());
        return mRootview;
    }

    @Override
    public void onClick(View v) {
        saveDictionaryObject();
    }

    private void saveDictionaryObject(){
        String word = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.name));
        String definition = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.description));

        //add word in dictionary via DictionarySQLManager
        //MnemoMemoryManager.startActionAddWord(getActivity().getApplicationContext(), mDictionaryID, word, definition);
        DictionarySQLManager.getInstance().addNewWord(mDictionaryID, word, definition);

        //hide the keyboard and exit the create fragment
        ViewTools.hideKeyboard(mRootview, getActivity());
        getFragmentManager().popBackStack();
        getActivity().finish();
    }

}
