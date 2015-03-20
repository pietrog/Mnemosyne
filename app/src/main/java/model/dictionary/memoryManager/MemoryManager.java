package model.dictionary.memoryManager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContract;
import model.dictionary.memoryManager.sql.MemoryMNanagerDBHelper;
import model.dictionary.memoryManager.sql.MemoryManagerContract;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;

/**
 * @author Pierre Gaulard
 * Class used to manage the memory cycle for all the dictionaries objects
 *
 */
public class MemoryManager {

    private MemoryManagerSQL mMemMgrSQL;
    private static String TABLE_NAME = MemoryManagerContract.MemoryManager.TABLE_NAME;

    public MemoryManager (Context context){
        MemoryMNanagerDBHelper dbhelper = new MemoryMNanagerDBHelper(context);
        mMemMgrSQL = new MemoryManagerSQL(dbhelper);
    }


    /**
     * Add a new word to manage.
     * @param _id dictionary object ID
     * @return rowID if successfull
     */
    public long addWordManager(long _id, Date date){
        return mMemMgrSQL.addWordManager(_id, date);
    }

    public long addWordManagerForToday(long _id){
        Calendar now = Calendar.getInstance();
        return addWordManager(_id, now.getTime());
    }

    public void clear(){
        Calendar now = Calendar.getInstance();
        mMemMgrSQL.clearAtDate(now.getTime());
    }


    /**
     * Retrieve the list of all dictionary objects saved in column COLUMN_NAME_IDLIST for a given date
     * @return list of integer (object ids)
     */
    public Vector<Integer> getTodayListOfObjectIDs(){
        return mMemMgrSQL.getTodayListOfObjectIDs();
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


        public long addWordManager(long _IDWord, Date date){

            if (mDB == null) {
                Logger.w("MemoryManagerSQL::getTodayListOfObjectIDs", " mDB is null ! ");
                return Global.FAILURE;
            }

            //check if there is already a list at this date (CREATE or UPDATE the line)
            Vector<Integer> list = getListOfObjectIDs(date);

            //insert
            if (list == null){
                ContentValues value = new ContentValues();
                String strdate = GeneralTools.getTodayDateFormatted(date);
                Logger.i("MemoryManagerSQL::addWordManager"," Insert wordManager: id "+ _IDWord + " date " + strdate);
                value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE, strdate);
                value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, _IDWord);
                return mDB.insert(TABLE_NAME, null, value);
            }
            //update
            else{
                ContentValues value = new ContentValues();
                list.add((int)_IDWord);
                value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, GeneralTools.getStringFrom(list));

                return mDB.update(TABLE_NAME, value, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + GeneralTools.getTodayDateFormatted(date) + "'", null);
            }
        }

        public Vector<Integer> getTodayListOfObjectIDs(){
            Calendar now = Calendar.getInstance();
            return getListOfObjectIDs(now.getTime());
        }


        /**
         *
         * @param date
         * @return null if mDB is null or if no line exists. A vector of the ids otherwise
         */
        public Vector<Integer> getListOfObjectIDs(Date date){
            Vector<Integer> res = new Vector<>();

            if (mDB == null) {
                Logger.w("MemoryManagerSQL::getTodayListOfObjectIDs", " mDB is null ! ");
                return null;
            }

            String strDate = GeneralTools.getTodayDateFormatted(date);
            String SQL_TODAYLIST = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME + " WHERE "
                    + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + strDate + "'";

            Logger.i("MemoryManagerSQL::getListOfObjectIDs", " sql " + SQL_TODAYLIST);
            Cursor cursor = mDB.rawQuery(SQL_TODAYLIST, null);

            //should have only one result
            if (cursor.getCount() != 1){
                //@todo add a more visible warning, because this situation should never occur
                Logger.w("MemoryManagerSQL::getTodayListOfObjectIDs"," Today list contains multiple entries... bad");

                if (cursor.getCount() == 0)
                    return null;
            }

            showCursor(cursor);
            if (!cursor.moveToFirst())
                Logger.i("MemoryManagerSQL::getListOfObjectIDs", " cursor move to first failed");

            String listBrut = MemoryManagerContract.getIDList(cursor);
            String[] listSplited = listBrut.split(",");
            for (String curr : listSplited)
                res.add(Integer.parseInt(curr));

            return  res;
        }

        public int clearAtDate(Date date){

            mDB.delete(TABLE_NAME, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + GeneralTools.getTodayDateFormatted(date) + "'", null);
            return Global.SUCCESS;
        }

        private void showCursor(Cursor cursor){
            if (!cursor.moveToFirst()){
                Logger.i("MemoryManagerSQL::showCursor", " empty cursor");
                return;
            }

            while(!cursor.isAfterLast()){
                String log = "ID " + MemoryManagerContract.getID(cursor) + " Date - " + MemoryManagerContract.getDate(cursor) + " ; IDList - " + MemoryManagerContract.getIDList(cursor);
                Logger.i("MemoryManagerSQL::showCursor", log);
                cursor.moveToNext();
            }
        }
    }

}
