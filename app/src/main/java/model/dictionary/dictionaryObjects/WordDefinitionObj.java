package model.dictionary.dictionaryObjects;

/**
 * Created by pietro on 02/02/15.
 */
public class WordDefinitionObj extends DictionaryObject{

    private String m_sWord; //
    private String m_sDescription; //


    WordDefinitionObj(String word, String description)
    {
        m_sWord = m_sDescription;
        m_sDescription = description;
    }

}