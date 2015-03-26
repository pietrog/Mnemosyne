package model.dictionary.memoryManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.BaseSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;

/**
 * Created by pietro on 24/03/15.
 *
 */
public class MemoryManagerSQLManager extends BaseSQLManager{

    private static MemoryManagerSQLManager instance;

    public static synchronized MemoryManagerSQLManager getInstance(Context context){
        if (instance == null)
            instance = new MemoryManagerSQLManager(context);
        return instance;
    }

    public static synchronized MemoryManagerSQLManager getInstance(){
        return instance;
    }


    /**
     * IMPLEMENTATION
     */
    private MemoryManagerSQLManager (Context context){
        super(new MemoryManagerDBHelper(context));
    }


    /**
     * Add a new word, so basically add an alert for tomorrow
     * @param idWord word sql id
     * @return row id if successful, -1 otherwise
     */
    public long addNewWord(long idWord){
        Calendar cal =  Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK,1);
        Date date = cal.getTime();
        long rowID = addWord(idWord, date);
        //if we successfully inserted the word in memory manager, update the word object in database
        if (rowID != -1){
            DictionarySQLManager.getInstance().updateNextDateForWord(idWord, date);
        }
        return rowID;
    }


    /**
     * Add a word alert for a given date
     * @param id id of the word
     * @param date alert date
     * @return row id of the memory database row
     */
    public long addWord(long id, Date date){
        //check if there is already a list at this date (CREATE or UPDATE the line)
        Vector<Integer> list = getListOfObjectIDs(date);

        //insert
        if (list == null){
            ContentValues value = new ContentValues();
            String strdate = GeneralTools.getSQLDate(date);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE, strdate);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, id);
            return add(value, MemoryManagerContract.MemoryManager.TABLE_NAME);
        }
        //update
        else{
            ContentValues value = new ContentValues();
            //@todo remove this int cast because we want a long
            list.add((int)id);
            value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, GeneralTools.getStringFrom(list));
            return update(value, MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + GeneralTools.getSQLDate(date) + "'");
        }
    }

    /**
     * Get the list of id for today
     * @return list of long ids
     */
    public Vector<Integer> getTodayList(){
        return getListOfObjectIDs(Calendar.getInstance().getTime());
    }

    /**
     * Return a list of dictionary object for a given date
     * @param date date
     * @return vector of long
     */
    public Vector<Integer> getListOfObjectIDs(Date date){
        return getListOfObjectIDs(GeneralTools.getSQLDate(date));
    }

    public Vector<Integer> getListOfObjectIDs(String date){
        Vector<Integer> res = new Vector<>();

        String SQL_TODAYLIST = "SELECT * FROM " + MemoryManagerContract.MemoryManager.TABLE_NAME + " WHERE "
                + MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + date + "'";

        Logger.i("MemoryManagerSQL::getListOfObjectIDs", " sql " + SQL_TODAYLIST);
        Cursor cursor = rawQuery(SQL_TODAYLIST);

        //should have only one result
        if (cursor.getCount() == 0)
            return null;

        if (!cursor.moveToFirst())
            Logger.i("MemoryManagerSQL::getListOfObjectIDs", " cursor move to first failed");

        String listBrut = GeneralTools.getStringElement(cursor, MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST);
        String[] listSplited = listBrut.split(",");
        for (String curr : listSplited)
            res.add(Integer.parseInt(curr));

        return  res;
    }

    /**
     * Remove an object id from a day alert list
     * @param id dictionary object id
     * @param date date alert of the list
     * @return row impacted
     */
    public int removeIDsFromList(long id, Date date){
        return removeIDsFromList(id, GeneralTools.getSQLDate(date));
    }

    public int removeIDsFromList(long id, String date) {
        //get the list and remove given id
        Vector<Integer> list = getListOfObjectIDs(date);
        if (list == null)
            return -1;

        list.remove((int)id);
        //update the list
        ContentValues value = new ContentValues();
        value.put(MemoryManagerContract.MemoryManager.COLUMN_NAME_IDLIST, GeneralTools.getStringFrom(list));
        return update(value, MemoryManagerContract.MemoryManager.TABLE_NAME, MemoryManagerContract.MemoryManager.COLUMN_NAME_DATE + " = '" + date + "'");
    }

}
