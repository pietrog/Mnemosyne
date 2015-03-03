package model.dictionary.catalogue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.JSONPersist;
import model.dictionary.dictionary.Dictionary;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.Logger;

/**
 * Created by pietro on 06/02/15.
 *
 * Describe a catalogue, a list of dictionaries. Can be an history book, or english book, with
 * classic dictionary, grammatical definitions, ...
 */
public class Catalogue implements JSONPersist{

    public static final String JSON_LISTDICT = "dictionaries";


    private String m_sName;
    private String m_sDescription;
    private Vector<Dictionary> m_mDictionaries = new Vector<Dictionary>();
    private boolean mHasBeenModifier = false;
    private final Context mContext;

    /**
     * Creation of a new Catalogue
     * @param name Name of the catalogue
     * @param desc Description of the catalogue
     */
    private Catalogue(String name, String desc, Context context){
        m_sName = name;
        m_sDescription = desc;
        mContext = context;
    }


    public String getName() {
        return m_sName;
    }
    public String getDescription(){
        return m_sDescription;
    }


    /**
     * Add a dictionary in the vector. We can add it without checking if it exists (when we load from JSON file)
     * @param name dictionary name
     * @param description dictionary description
     * @param checkExists if true we check if the dictionary already exists
     * @return SUCCESS or ALREADY_EXISTS
     */
    public int addDictionary(String name, String description, boolean checkExists){

        Dictionary dict = new Dictionary(name, description);

        if (checkExists && m_mDictionaries.contains(dict))
            return Global.ALREADY_EXISTS;

        m_mDictionaries.add(dict);
        mHasBeenModifier = true;

        //@TODO remove this
        writeToJSONFile();

        return Global.SUCCESS;
    }


    private static String getJSONNameFromCatalogueName(String name){
        String str = "catalogue_" + name;;
        return str.replaceAll("\\s+", "");
    }

    /**
     * Load a catalogue from the JSON file. If does not exist, return null
     * @param catalogueName name of the catalogue (unique, so it represents the id)
     * @param context activity context
     * @return catalogue loaded from the corresponding json file, null if file does not exist
     */
    public static Catalogue LoadCatalogue(String catalogueName, Context context) {
        String path = context.getFilesDir().getPath();
        Catalogue res = null;

        try {
            JSONObject jsonObj = GeneralTools.getJSONObjectFromFile(path, getJSONNameFromCatalogueName(catalogueName));

            if (jsonObj == null)
                return null;

            //Start building the catalogue
            String desc = jsonObj.getString(CatalogueList.JSON_DESC);

            res = new Catalogue(catalogueName, desc, context);

            //load the dictionaries from the json array
            JSONArray listOfDictionaries = jsonObj.getJSONArray(JSON_LISTDICT);

            for (int i = 0; i<listOfDictionaries.length(); i++){
                JSONObject current = listOfDictionaries.getJSONObject(i);
                res.addDictionary(current.getString(CatalogueList.JSON_NAME), current.getString(CatalogueList.JSON_DESC), false);
            }

        }
        catch (JSONException e){
            Logger.d("Catalogue::LoadCatalogue", "JSON exception thrown", e);
        }

        return res;
    }


    @Override
    public int writeToJSONFile(){

        JSONObject catalogue = toJSONObject();
        String jsonFilename = getJSONNameFromCatalogueName(m_sName);
        // write the json file
        GeneralTools.writeFile(catalogue, jsonFilename, mContext);

        return Global.SUCCESS;
    }

    @Override
    public JSONObject toJSONObject(){

        JSONObject catalogue = null;
        try {
            catalogue = new JSONObject();
            catalogue.put(CatalogueList.JSON_NAME, m_sName);
            catalogue.put(CatalogueList.JSON_DESC, m_sDescription);

            JSONArray listDict = new JSONArray();
            for(Dictionary current: m_mDictionaries){
                JSONObject json = new JSONObject();
                json.put(CatalogueList.JSON_NAME, current.getName());
                json.put(CatalogueList.JSON_DESC, current.getDescription());
                listDict.put(json);
            }
            catalogue.put(JSON_LISTDICT, listDict);
        } catch (JSONException e) {
            Logger.d("Catalogue::toJSONObject", "JSON exception thrown", e);
        }

        return catalogue;

    }

    /**
     * Create the catalogue
     * @param catalogueName name of the catalogue
     * @param description description of the catalogue
     * @param context context of the activity
     * @return the created catalogue
     */
    public static Catalogue createCatalogue(String catalogueName, String description, Context context) {
        Catalogue res = new Catalogue(catalogueName, description, context);
        res.writeToJSONFile();
        return res;
    }





}