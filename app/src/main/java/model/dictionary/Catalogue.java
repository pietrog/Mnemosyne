package model.dictionary;

/**
 * Created by pietro on 06/02/15.
 */
public class Catalogue {

    private String m_sName;
    private String m_sDescription;


    /**
     * Creation of a new Catalogue
     * @param name Name of the catalogue
     * @param desc Description of the catalogue
     */
    public Catalogue(String name, String desc){
        m_sName = name;
        m_sDescription = desc;
    }

}