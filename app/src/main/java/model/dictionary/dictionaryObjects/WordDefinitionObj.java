package model.dictionary.dictionaryObjects;

/**
 * Created by pietro on 02/02/15.
 */
public class WordDefinitionObj extends DictionaryObject{

    private String m_sWord; //
    private String m_sDefinition; //



    WordDefinitionObj(String word, String definition)
    {
        super( word+ "(" + definition.substring(0, 5).toString() + "...)");
        m_sWord = word;
        m_sDefinition = definition;
    }

    @Override
    public DictionaryObjectType getType() {
        return DictionaryObjectType.WordDefinition;
    }
}