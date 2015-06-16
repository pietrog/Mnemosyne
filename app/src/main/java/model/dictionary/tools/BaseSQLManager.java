package model.dictionary.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import model.dictionary.Global;

/**
 * Created by pietro on 23/03/15.
 * Base abstract class for a SQL manager.
 * SQL manager deals with its own database and model. Each persistent model item should have a class that extends this one
 */
public abstract class BaseSQLManager {

    private SQLiteOpenHelper mDBHelper;
    private Context mContext;


    protected BaseSQLManager(Context context){
        mDBHelper = MnemoDBHelper.getInstance(context);
        mContext = context;
    }

    protected SQLiteDatabase getSQLDBWrite(){
        return MnemoDBHelper.getInstance(mContext).getWritableDatabase();
    }

    protected SQLiteDatabase getSQLDBRead(){
        return MnemoDBHelper.getInstance(mContext).getReadableDatabase();
    }

    /**
     * Add value in table tableName
     * @param value ContentValues value to add
     * @param tableName String table name
     * @return id created if successful, {Global.FAILURE} otherwise
     */
    protected final long add(ContentValues value, String tableName){
        long res ;
        try {
            res = getSQLDBWrite().insertOrThrow(tableName, null, value);
        } catch (SQLException e) {
            Logger.w("BaseSQLManager::add", " insertion failed in " + tableName + " value ( " + value.toString() + "). Already exists");
            return Global.FAILURE;
        }
        return res;
    }

    /**
     * Remove items identified by ids. See with extending class for removed item
     * @param listIDs sql id of the dictionary
     * @return row affected
     */
    protected final int remove(long[] listIDs, String tableName){
        if (listIDs == null)
            return Global.BAD_PARAMETER;
        if (listIDs.length == 0)
            return 0;
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
    protected final Cursor rawQuery(String sqlClause, String[] selectionArgs){
        return getSQLDBRead().rawQuery(sqlClause, selectionArgs);
    }

    protected final int update(ContentValues value, String tableName, String whereClause){
        return getSQLDBWrite().update(tableName, value, whereClause, null);
    }
}
