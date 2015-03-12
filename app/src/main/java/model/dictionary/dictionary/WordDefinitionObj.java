package model.dictionary.dictionary;

import android.content.ContentValues;

import model.dictionary.dictionary.sql.DictionaryContract;

/**
 * Created by pietro on 02/02/15.
 */
public class WordDefinitionObj extends DictionaryObject{

    private String m_sWord; //
    private String m_sDefinition; //



    public WordDefinitionObj(String word, String definition)
    {
        super( word+ "(" + definition + "...)");
        m_sWord = word;
        m_sDefinition = definition;
    }

    @Override
    public DictionaryObjectType getType() {
        return DictionaryObjectType.WordDefinition;
    }


    public ContentValues toContentValues(){
        ContentValues content = new ContentValues();
        content.put(DictionaryContract.Dictionary.COLUMN_NAME_WORD, m_sWord);
        content.put(DictionaryContract.Dictionary.COLUMN_NAME_DEFINITION, m_sDefinition);
        return content;
    }

    public String getWord() {
        return m_sWord;
    }

    public String getDefinition() {
        return m_sDefinition;
    }
}