package com.mnemo.pietro.mnemosyne.fragments.word;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment {
    private static final String WORD = "WORD";
    private static final String DEFINITION = "DEFINITION";
    private static final String LASTDATE = "LAST";
    private static final String NEXTDATE = "NEXT";
    private static final long WORDID = "ID";


    private String mWord;
    private String mDefinition;
    private String mlastDate;
    private String mnextDate;
    private long mID;


    public static WordFragment newInstance(WordDefinitionObj obj) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, obj.getWord());
        args.putString(DEFINITION, obj.getDefinition());
        args.putString(LASTDATE, GeneralTools.getSQLDate(obj.getLastTimeLearnt()));
        args.putString(NEXTDATE, GeneralTools.getSQLDate(obj.getNextTimeToLearn()));
        args.putLong(WORD, obj.getID());
        fragment.setArguments(args);
        return fragment;
    }

    public WordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWord = getArguments().getString(WORD);
            mDefinition = getArguments().getString(DEFINITION);
            mlastDate = getArguments().getString(LASTDATE);
            mnextDate = getArguments().getString(NEXTDATE);
            mID = getArguments().getLong(WORDID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.word_fragment, container, false);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.word_name), mWord);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.word_definition), mDefinition);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.lastDate), mlastDate);
        ViewTools.setStringOfTextView((TextView)mRootView.findViewById(R.id.nextDate), mnextDate);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_word);
        ViewTools.setSubtitle(getActivity(), mWord);
    }

}
