package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.MnemoWord;
import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.TodayListAdapter;
import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;

import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoCalendar;

/**
 * Created by pietro on 25/03/15.
 *
 * Fragment displaying the list of word to learn today
 * Extends DictionaryFragment
 */
public class TodayListFragment extends BaseDictionaryFragment{


    public static final String ALERTDATE = "ALERTDATE"; // the date corresponding to the raise alert
    private long mDate;

    public static TodayListFragment newInstance(long date){
        TodayListFragment fragment = new TodayListFragment();

        if (date > 0) {
            Bundle args = new Bundle();
            args.putLong(ALERTDATE, date);
            fragment.setArguments(args);
        }

        return fragment;
    }

    public TodayListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mDate = getArguments().getLong(ALERTDATE);
        else
            mDate = MnemoCalendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.raiseAlert).setVisible(false);
        menu.findItem(R.id.validate).setVisible(false);
        menu.findItem(R.id.add_item).setVisible(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshListView();
        setListAdapter(mAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), MnemoWord.class);
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        intent.putExtra(WordFragment.WORDID, GeneralTools.getLongElement(cursor, WordContract.Word.CID));
        intent.putExtra(WordFragment.DICTIONARYOBJECTID, GeneralTools.getLongElement(cursor, WordContract.Word.DICTIONARYOBJECTID));
        intent.putExtra(WordFragment.WORD, GeneralTools.getStringElement(cursor, WordContract.Word.WORD));
        intent.putExtra(WordFragment.DEFINITION, GeneralTools.getStringElement(cursor, WordContract.Word.DEFINITION));
        startActivity(intent);
    }



    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    @Override
    protected void removeWord(int position) {
        super.removeWord(position);
        mAdapter.changeCursor(MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate));
    }

    public void refreshListView(){
        if (mAdapter == null){
            mAdapter = new TodayListAdapter(mDate, getActivity().getApplicationContext(), R.layout.std_list_fragment, MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate), 0);
        }
        else
            mAdapter.changeCursor(MemoryManagerSQLManager.getInstance().getCursorOfObjectsToLearn(mDate));
    }
}
