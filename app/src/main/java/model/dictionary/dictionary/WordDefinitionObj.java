package model.dictionary.dictionary;

import android.content.ContentValues;
import android.database.Cursor;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionaryOfWordContract;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 02/02/15.
 *
 */
public class WordDefinitionObj extends DictionaryObject{

    private String m_sWord; // word
    private String m_sDefinition; // definition of word


    /**
     * Complete constructor for WordDefinitionObj
     * @param id sql id
     * @param catalogueName catalogue narme
     * @param dictionaryName dictionary name
     * @param word word name
     * @param definition definition of the word
     */
    public WordDefinitionObj(long id, String catalogueName, String dictionaryName, String word, String definition)
    {
        super(id, catalogueName, dictionaryName);
        m_sWord = word;
        m_sDefinition = definition;
    }



    public String getWord() {
        return m_sWord;
    }

    public String getDefinition() {
        return m_sDefinition;
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
        content.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_WORD, m_sWord);
        content.put(DictionaryOfWordContract.DictionaryOfWord.COLUMN_NAME_DEFINITION, m_sDefinition);
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
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_CATALOGUE_NAME),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_DICTIONARY_NAME),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_WORD),
                GeneralTools.getStringElement(cursor, DictionaryContractBase.DictionaryBase.COLUMN_NAME_DEFINITION));
    }


}