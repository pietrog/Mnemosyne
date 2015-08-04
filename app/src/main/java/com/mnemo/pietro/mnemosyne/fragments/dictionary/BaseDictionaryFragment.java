package com.mnemo.pietro.mnemosyne.fragments.dictionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnemo.pietro.mnemosyne.R;
import com.mnemo.pietro.mnemosyne.tools.CursorRecycleViewAdapter;


/**
 * Created by pietro on 25/03/15.
 * Base Fragment class for Dictionary type fragment
 * Should be extended, shares some functionality useful for this type of fragment
 */
public abstract class BaseDictionaryFragment extends Fragment{

    protected CursorRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView mRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.release();
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        WordFragment fragment = WordFragment.newInstance(cursor);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_subscreen, fragment).commit();
    }*/

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_content_menu, menu);
    }*/

    protected void removeWord(int position){
        /*mAdapter.getCursor().moveToPosition(position);
        long [] id = {GeneralTools.getLongElement(mAdapter.getCursor(), WordContract.Word._ID)};

        DictionarySQLManager.getInstance().remove(id);*/
    }
}
