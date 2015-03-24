package com.mnemo.pietro.mnemosyne.fragments.word;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mnemo.pietro.mnemosyne.MnemoMemoryManager;
import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionarySQLManager;

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

        //add word in dictionary via DictionarySQLManager
        DictionarySQLManager manager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        long id = manager.add(mCatalogueName, mDictionaryName, word, definition);


        if (id == Global.FAILURE)
            Toast.makeText(getActivity().getApplicationContext(), word + " already exists.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity().getApplicationContext(), word + " added to " + mDictionaryName, Toast.LENGTH_SHORT).show();

        //add word in memory manager via MemoryManager
        MemoryManager mgr = MemoryManagerSingleton.getInstance(getActivity().getApplicationContext());
        long memId = mgr.addWordManagerForToday(id);


        //hide the keyboard and exit the create fragment
        ViewTools.hideKeyboard(mRootview, getActivity());
        getFragmentManager().popBackStack();

        MnemoMemoryManager.startActionRiseTodayList(getActivity().getApplicationContext(), "");
    }

}
