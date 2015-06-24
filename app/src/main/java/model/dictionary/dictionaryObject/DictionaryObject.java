package model.dictionary.dictionaryObject;
import android.content.ContentValues;
import android.database.Cursor;
import model.dictionary.dictionaryObject.sql.DictionaryObjectContract;
import model.dictionary.tools.GeneralTools;


/**
 * Created by pietro on 30/01/15.
 * DictionaryObject is the base object describing a dictionary object
 * It will be derived, for exemple, be MemoryObject class, managing memory processing
 * But DictionaryObject can also be more complex, store links, pictures or audio description
 */
public class DictionaryObject {

    //object content
    private long mID; // DictionaryObject ID
    private long mDictionaryID; // parent Dictionary ID
    private long mMemoryMonitoringID;


    /**
     * Constructor used for Memory Management
     * @param id object id
     */
    protected DictionaryObject(long id, long dictionaryId, long memMonitoringId){
        mID = id;
        mDictionaryID = dictionaryId;
        mMemoryMonitoringID = memMonitoringId;
    }

    /**
     * Constructor from cursor
     * @param cursor
     */
    protected DictionaryObject(Cursor cursor){
        mID = GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.CSID);
        mDictionaryID = GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.DICTIONARYID);
        mMemoryMonitoringID = GeneralTools.getLongElement(cursor, DictionaryObjectContract.DictionaryObject.MEMORY_MONITORING_ID);
    }


    public static DictionaryObject LoadFromCursor(Cursor cursor){
        return new DictionaryObject(cursor);
    }

    /**
     * Return type of the current object
     * @return enum Type of the dictionary object
     */
    public DictionaryObjectType getType() {
        return null;
    };

    /**
     * Get the object in ContentValues container, for sql. Method to override in all subclasses
     * @return ContentValues of this object
     */
    public ContentValues dictionaryObjectToContentValues(){
        return new ContentValues();
    }

    /**
     * ToString method
     * @return string representation of the object
     */
    public String toString(){
        String res = "DictionaryObject : ID "+ mID ;
        res += "Memonry object id : " + mMemoryMonitoringID;
        return res;
    }


    /**
     * GETTER
     */
    public long getDictionaryObjectID() {
        return mID;
    }

    public long getMemoryMonitoringID() {
        return mMemoryMonitoringID;
    }

    public long getDictionaryID() {
        return mDictionaryID;
    }

}