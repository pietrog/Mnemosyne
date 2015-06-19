package com.mnemo.pietro.mnemosyne.fragments.word;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mnemo.pietro.mnemosyne.MnemoMemoryManager;
import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.dictionaryObject.DictionaryObject;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.dictionary.sql.DictionarySQLManager;
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
    private static final String DICTIONARYOBJECTID = "DICTOBJID";


    private String mWord;
    private String mDefinition;
    private long mID;
    private long mDictObjID;
    private View mRootView;

    public static WordFragment newInstance(WordDefinitionObj obj) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, obj.getWord());
        args.putString(DEFINITION, obj.getDefinition());
        args.putLong(WORDID, obj.getID());
        args.putLong(DICTIONARYOBJECTID, obj.getDictionaryObjectID());
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
            mDictObjID = getArguments().getLong(DICTIONARYOBJECTID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.word_fragment, container, false);
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.word_name), mWord);
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.word_definition), mDefinition);

        //onclicklistener attached
        ImageButton valid = (ImageButton)mRootView.findViewById(R.id.learnt);
        valid.setOnClickListener(this);
        ImageButton show = (ImageButton)mRootView.findViewById(R.id.showStats);
        show.setOnClickListener(this);

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
        switch (v.getId()){
            case R.id.learnt:
                onLearntWord();
            break;
            case R.id.showStats:
                showStatistics();
                break;
        }
    }

    private void onLearntWord(){
        MnemoMemoryManager.startActionUpdateWord(getActivity().getApplicationContext(), mDictObjID);
        getFragmentManager().popBackStack();
    }

    private void showStatistics(){
        MemoryObject object = DictionarySQLManager.getInstance().getMemoryObjectFromID(mID);
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.lastDate), GeneralTools.getSQLDate(object.getLastLearnt()));
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.nextDate), GeneralTools.getSQLDate(object.getNextLearnt()));

        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.date_add), GeneralTools.getSQLDate(object.getDateAdded()));
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.days_between), object.getDaysBetween() + "");
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.mem_phase), object.getMemoryPhaseName());
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.beg_mem), GeneralTools.getSQLDate(object.getBeginningOfMP()));


        ImageButton show = (ImageButton)mRootView.findViewById(R.id.showStats);
        View stats = mRootView.findViewById(R.id.layStats);
        stats.setVisibility(View.VISIBLE);
        show.setVisibility(View.INVISIBLE);
    }
}
