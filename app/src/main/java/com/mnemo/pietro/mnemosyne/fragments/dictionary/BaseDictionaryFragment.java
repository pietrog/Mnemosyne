package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.fragments.dictionary.tools.DictionaryAdapter;
import com.mnemo.pietro.mnemosyne.fragments.word.WordFragment;

import model.dictionary.dictionary.WordDefinitionObj;
import model.dictionary.dictionary.sql.WordContract;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 25/03/15.
 * Base Fragment class for Dictionary type fragment
 * Should be extended, shares some functionality useful for this type of fragment
 */
public abstract class BaseDictionaryFragment extends ListFragment{

    protected DictionaryAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onStop() {
        super.onStop();

        //close the cursor before leaving
        mAdapter.getCursor().close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        WordDefinitionObj obj = WordDefinitionObj.LoadFromCursor(cursor);
        WordFragment fragment = WordFragment.newInstance(obj);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_content_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.remove_item:
                removeWord(info.position);
                return true;
            default:
                return true;
        }
    }


    protected void removeWord(int position){
        mAdapter.getCursor().moveToPosition(position);
        long [] id = {GeneralTools.getLongElement(mAdapter.getCursor(), WordContract.Word._ID)};

        DictionarySQLManager.getInstance().remove(id);
    }
}
