package com.mnemo.pietro.mnemosyne.fragments.word;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mnemo.pietro.mnemosyne.MnemoMemoryManager;
import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment implements View.OnClickListener{
    private static final String WORD = "WORD";
    private static final String DEFINITION = "DEFINITION";
    private static final String WORDID = "ID";


    private String mWord;
    private String mDefinition;
    private long mID;


    public static WordFragment newInstance(WordDefinitionObj obj) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, obj.getWord());
        args.putString(DEFINITION, obj.getDefinition());
        args.putLong(WORDID, obj.getID());
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

        //onclicklistener attached
        ImageButton valid = (ImageButton)mRootView.findViewById(R.id.learnt);
        valid.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTools.setTitle(getActivity(), R.string.hint_word);
        ViewTools.setSubtitle(getActivity(), mWord);
    }

    @Override
    public void onClick(View v) {
        onLearntWord();
    }

    private void onLearntWord(){
        MnemoMemoryManager.startActionUpdateWord(getActivity().getApplicationContext(), mID);
        getFragmentManager().popBackStack();
    }
}
