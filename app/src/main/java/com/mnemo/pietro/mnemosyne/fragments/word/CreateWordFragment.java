package com.mnemo.pietro.mnemosyne.fragments.word;

import android.app.Activity;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateWordFragment.OnWordFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateWordFragment extends Fragment implements View.OnClickListener{
    private static final String DICTIONARY_NAME = "DICT_NAME";
    private static final String CATALOGUE_NAME = "CAT_NAME";

    private String mDictionaryName;
    private String mCatalogueName;

    private View mRootview;

    private OnWordFragmentInteractionListener mListener;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnWordFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.createWordFragmentVisible(mDictionaryName, mCatalogueName);
    }

    @Override
    public void onClick(View v) {
        String word = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.word_name));
        String definition = ViewTools.getStringFromEditableText(mRootview.findViewById(R.id.word_definition));
        WordDefinitionObj wordDef = new WordDefinitionObj(word, definition);
        Dictionary dict = CatalogueListSingleton.getInstance(getActivity().getApplicationContext()).getCatalogue(mCatalogueName).getDictionary(mDictionaryName);
        dict.setDBHelper(new DictionaryDBHelper(getActivity().getApplicationContext()));
        dict.addDictionaryObject(wordDef);
        getFragmentManager().popBackStack();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnWordFragmentInteractionListener {
        public void createWordFragmentVisible(String dictionaryName, String catalogueName);
    }

}
