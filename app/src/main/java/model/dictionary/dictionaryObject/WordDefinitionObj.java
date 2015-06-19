package model.dictionary.dictionaryObject;

import android.content.ContentValues;
import android.database.Cursor;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionaryObject.sql.WordContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 02/02/15.
 *
 */
public class WordDefinitionObj extends MemoryObject{

    private long mID; // sql row id
    private String m_sWord; // word
    private String m_sDefinition; // definition of word


    protected WordDefinitionObj(Cursor cursor){
        super(cursor);
        mID = GeneralTools.getLongElement(cursor, WordContract.Word.CSID);
        m_sWord = GeneralTools.getStringElement(cursor, WordContract.Word.WORD);
        m_sDefinition = GeneralTools.getStringElement(cursor, WordContract.Word.DEFINITION);
    }


    public String getWord() {
        return m_sWord;
    }

    public String getDefinition() {
        return m_sDefinition;
    }

    public long getID() {
        return mID;
    }

    @Override
    public DictionaryObjectType getType() {
        return DictionaryObjectType.WordDefinition;
    }


    /**
     * Extends the toContentValue of DictionaryObject
     * @return ContentValues of this word object
     */
    public ContentValues toContentValues(){
        ContentValues content = new ContentValues();
        content.put(WordContract.Word.WORD, m_sWord);
        content.put(WordContract.Word.DEFINITION, m_sDefinition);
        return content;
    }


    /**
     * STATIC METHODS
     */

    /**
     * Load a new WordDefinitionObj from a cursor
     * @param cursor cursor from an adapter
     * @return WordDefinitionObj loaded from cursor
     */
    public static WordDefinitionObj LoadFromCursor(Cursor cursor){
        return new WordDefinitionObj(cursor);
    }


}