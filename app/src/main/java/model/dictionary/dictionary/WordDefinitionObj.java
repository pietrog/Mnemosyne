package model.dictionary.dictionary;

import android.content.ContentValues;
import android.database.Cursor;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.WordContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 02/02/15.
 *
 */
public class WordDefinitionObj extends DictionaryObject{

    private long mID;
    private String m_sWord; // word
    private String m_sDefinition; // definition of word


    /**
     * Complete constructor for WordDefinitionObj
     * @param id sql Word object id
     * @param word word name
     * @param definition definition of the word
     */
    public WordDefinitionObj(long id, String word, String definition)
    {
        mID = id;
        m_sWord = word;
        m_sDefinition = definition;
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
    @Override
    public ContentValues toContentValues(){
        ContentValues content = super.toContentValues();
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
        return new WordDefinitionObj(GeneralTools.getLongElement(cursor, DictionaryContractBase.DictionaryBase._ID),
                GeneralTools.getStringElement(cursor, WordContract.Word.WORD),
                GeneralTools.getStringElement(cursor, WordContract.Word.DEFINITION));
    }


}