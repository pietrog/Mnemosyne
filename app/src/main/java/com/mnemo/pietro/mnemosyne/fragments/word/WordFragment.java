package com.mnemo.pietro.mnemosyne.fragments.word;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mnemo.pietro.mnemosyne.R;

import java.util.Date;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.memory.LongTermMemory;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment implements View.OnClickListener{

    public static final String WORD = "WRD";
    public static final String DEFINITION = "DEF";
    public static final String WORDID = "ID";
    public static final String DICTIONARYOBJECTID = "DCTOBJID";


    private String mWord;
    private String mDefinition;
    private long mID;
    private long mDictObjID;
    private View mRootView;

    public static WordFragment newInstance(Cursor cursor) {
        WordDefinitionObj obj = WordDefinitionObj.LoadFromCursor(cursor);
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, obj.getWord());
        args.putString(DEFINITION, obj.getDefinition());
        args.putLong(WORDID, obj.getID());
        args.putLong(DICTIONARYOBJECTID, obj.getDictionaryObjectID());
        fragment.setArguments(args);
        return fragment;
    }

    public static WordFragment newInstance(String word, String definition, long id, long dictID) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, word);
        args.putString(DEFINITION, definition);
        args.putLong(WORDID, id);
        args.putLong(DICTIONARYOBJECTID, dictID);
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
        //MnemoMemoryManager.startActionUpdateWord(getActivity().getApplicationContext(), mDictObjID);
        LongTermMemory.getInstance(getActivity().getApplicationContext()).onLearnt(mDictObjID);
        getFragmentManager().popBackStack();
    }

    private void showStatistics(){
        MemoryObject object = DictionarySQLManager.getInstance().getMemoryObjectFromDictionaryObjectID(mDictObjID);
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.lastDate), GeneralTools.getSQLDate(object.getLastLearnt()));
        ViewTools.setStringOfTextView(mRootView.findViewById(R.id.nextDate), GeneralTools.getSQLDate(new Date(object.getNextLearn())));

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
