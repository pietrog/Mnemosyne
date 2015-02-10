package model.dictionary.catalogue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import model.dictionary.Global;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 06/02/15.
 */
public class Catalogue {

    private String m_sName;
    private String m_sDescription;
    private HashMap<String, String> m_mDictMap = new HashMap<>(0);

    /**
     * Creation of a new Catalogue
     * @param name Name of the catalogue
     * @param desc Description of the catalogue
     */
    private Catalogue(String name, String desc){
        m_sName = name;
        m_sDescription = desc;
    }

    public int storeCatalogue(){

        JSONObject catalogue ;
        String listFilename = Global.catalogueList_JSON_filename;
        File f = new File(listFilename);
        if (!f.exists()){
            try{
                catalogue = new JSONObject();
                catalogue.put("name", m_sName);
                catalogue.put("description", m_sDescription);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        else{

        }

        FileOutputStream fos ;

        return 0;
    }

    public Catalogue LoadExistingCatalogue() {

        Catalogue res = null;
        try {
            JSONObject jsonObj = GeneralTools.getJSONObjectFromFile("", Global.catalogueList_JSON_filename);
            String name = jsonObj.getString("name");
            String desc = jsonObj.getString("description");
            res = new Catalogue(name, desc);
        }
        catch (JSONException e){
        }

        return res;
    }

}