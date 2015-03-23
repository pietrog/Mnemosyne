package model.dictionary.dictionary;

import android.content.ContentValues;

import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionaryOfWordContract;

/**
 * Created by pietro on 02/02/15.
 *
 */
public class WordDefinitionObj extends DictionaryObject{

    private String m_sWord; // word
    private String m_sDefinition; // definition of word


    /*public static WordDefinitionObj getWordDefinitionObjFromSQL( ){

    }*/

    /**
     * Constructor for WordDefinitionObj
     * @param word word
     * @param definition definition of word
     */
    public WordDefinitionObj(long id, String word, String definition)
    {
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
}