package com.mnemo.pietro.mnemosyne.fragments.dictionary;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DictionaryObjectWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DictionaryObjectWordFragment extends Fragment {
    private static final String WORD = "WORD";
    private static final String DEFINITION = "DEFINITION";

    private String mWord;
    private String mDefinition;
    private View mRootView;

    private OnDictionaryObjectWordFragmentInteractionListener mListener;


    public static DictionaryObjectWordFragment newInstance(WordDefinitionObj obj) {
        DictionaryObjectWordFragment fragment = new DictionaryObjectWordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, obj.getWord());
        args.putString(DEFINITION, obj.getDefinition());
        fragment.setArguments(args);
        return fragment;
    }

    public DictionaryObjectWordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWord = getArguments().getString(WORD);
            mDefinition = getArguments().getString(DEFINITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.dictionary_object_word_fragment, container, false);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.word_name), mWord);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.word_definition), mDefinition);
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (OnDictionaryObjectWordFragmentInteractionListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnDictionaryObjectWordFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.dictionaryObjectWordFragmentVisible(mWord);
    }

    public interface OnDictionaryObjectWordFragmentInteractionListener {
        public void dictionaryObjectWordFragmentVisible(String word);
    }


}
