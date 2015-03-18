package model.dictionary.memoryManager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.memoryManager.sql.MemoryMNanagerDBHelper;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.tools.Logger;

/**
 * Created by pietro on 18/03/15.
 *
 */
public class MemoryManager {

    public MemoryManager (){

    }


    public int addWordManager(){

        return Global.SUCCESS;
    }



    private class MemoryManagerSQL{
        private MemoryMNanagerDBHelper mDBHelper = null;
        private SQLiteDatabase mDB;

        public MemoryManagerSQL(MemoryMNanagerDBHelper dbHelper){
            setDBHelper(dbHelper);
        }

        public void setDBHelper(MemoryMNanagerDBHelper dbHelper){
            if (dbHelper != null && mDBHelper == null){
                mDBHelper = dbHelper;
                mDB = mDBHelper.getWritableDatabase();
            }
        }

        public Vector<Integer> getTodayListOfObjectIDs(){
            Vector<Integer> res = new Vector<>();

            if (mDB == null) {
                Logger.w("MemoryManagerSQL::getTodayListOfObjectIDs", " mDB is null ! ");
                return res;
            }

            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MMM:yyyy");
            String strDate = sdf.format(now.getTime());


            String SQL_TODAYLIST = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME + " WHERE "
                    + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + strDate + "'";
            Cursor cursor = mDB.rawQuery(SQL_TODAYLIST, null);
            if (cursor.getCount() != 1){
                Logger.w("MemoryManagerSQL::getTodayListOfObjectIDs"," Today list contains multiple entries... bad");
                return res;
            }

            cursor.moveToFirst();
            String listBrut = MemoryManagerContract.getIDList(cursor);
            String[] listSplited = listBrut.split(";");
            for (String curr : listSplited)
                res.add(Integer.parseInt(curr));

            return  res;
        }

        public int newObjectToManage(long _id){


            return Global.SUCCESS;
        }
    }

}
