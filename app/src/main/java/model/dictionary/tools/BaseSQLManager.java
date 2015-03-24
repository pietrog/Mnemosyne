package model.dictionary.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import model.dictionary.dictionary.sql.DictionaryOfWordContract;

/**
 * Created by pietro on 23/03/15.
 * Base abstract class for a SQL manager.
 * SQL manager deals with its own database and model. Each persistent model item should have a class that extends this one
 */
public abstract class BaseSQLManager {

    private SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mDBWrite;
    private SQLiteDatabase mDBRead;

    protected BaseSQLManager(SQLiteOpenHelper dbHelper){
        mDBHelper = dbHelper;
    }

    protected SQLiteDatabase getSQLDBWrite(){
        if (mDBWrite == null || !mDBWrite.isOpen())
            mDBWrite = mDBHelper.getWritableDatabase();
        return mDBWrite;
    }

    protected SQLiteDatabase getSQLDBRead(){
        if (mDBRead == null || !mDBRead.isOpen())
            mDBRead = mDBHelper.getReadableDatabase();
        return mDBRead;
    }


    protected final long add(ContentValues value, String tableName){
        long res ;
        try {
            res = getSQLDBWrite().insertOrThrow(tableName, null, value);
        } catch (SQLException e) {
            Logger.w("BaseSQLManager::add", " insertion failed in " + tableName + " value ( " + value.toString() + "). Already exists");
            return -1;
        }
        return res;
    }

    /**
     * Remove items identified by ids. See with extending class for removed item
     * @param listIDs sql id of the dictionary
     * @return row affected
     */
    protected final int remove(long[] listIDs, String tableName){
        String sqlclause = BaseColumns._ID + " in (" ;
        for (long id: listIDs )
            sqlclause += id + ",";
        sqlclause = sqlclause.substring(0, sqlclause.length()-1);
        sqlclause += ")";
        return getSQLDBWrite().delete(tableName, sqlclause, null);
    }

    /**
     * Get a cursor on set of results fetched with sqlClause
     * @param sqlClause sql request
     * @return cursor
     */
    protected final Cursor rawQuery(String sqlClause){
        return getSQLDBRead().rawQuery(sqlClause, null);
    }

}
