package com.mnemo.pietro.mnemosyne.fragments.word;


import android.os.Bundle;

/**
 * Created by pietro on 03/08/15.
 */
public class WordFragmentRev extends WordFragment {

    public static WordFragmentRev newInstance(String word, String definition, long id, long dictID) {
        WordFragmentRev fragment = new WordFragmentRev();
        Bundle args = new Bundle();
        args.putString(WORD, word);
        args.putString(DEFINITION, definition);
        args.putLong(WORDID, id);
        args.putLong(DICTIONARYOBJECTID, dictID);
        fragment.setArguments(args);
        return fragment;
    }

    public WordFragmentRev(){}

    @Override
    protected void onLearntWord(){
        super.onLearntWord();
        getActivity().finish();
    }



}
