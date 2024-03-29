package com.mnemo.pietro.mnemosyne.fragments.dictionary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mnemo.pietro.mnemosyne.R;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.ViewTools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateDictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDictionaryFragment extends Fragment implements View.OnClickListener{

    private static final String CATALOGUE_ID = "CATALOGUE_ID";


    //catalogue containing this dictionary
    private long mCatalogueID;
    private View rootView = null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogueID long parent catalogue id
     * @return A new instance of fragment CreateDictionaryFragment.
     */
    public static CreateDictionaryFragment newInstance(long catalogueID) {
        CreateDictionaryFragment fragment = new CreateDictionaryFragment();
        Bundle args = new Bundle();
        args.putLong(CATALOGUE_ID, catalogueID);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateDictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mCatalogueID = getArguments().getLong(CATALOGUE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dictionary_create_fragment, container, false);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        saveDictionary();
    }

    public void saveDictionary(){

        String dictName = ViewTools.getStringFromEditableText(rootView.findViewById(R.id.name));
        String dictDesc = ViewTools.getStringFromEditableText(rootView.findViewById(R.id.description));

        DictionarySQLManager manager = DictionarySQLManager.getInstance(getActivity().getApplicationContext());
        long id;
        if (mCatalogueID >= 0)
            id = manager.addDictionaryInCatalogue(mCatalogueID, dictName, dictDesc);
        else
            id = manager.addDictionary(dictName, dictDesc);

        if (id == Global.FAILURE)
            Toast.makeText(getActivity().getApplicationContext(), "Dictionary " + dictName + " already exists.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity().getApplicationContext(), "Dictionary " + dictName + " added", Toast.LENGTH_SHORT).show();

        //hide keyboard
        ViewTools.hideKeyboard(rootView, getActivity());
        //pop the fragment
        getFragmentManager().popBackStack();
        getActivity().finish();
    }


}
