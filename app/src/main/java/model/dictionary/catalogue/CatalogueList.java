package model.dictionary.catalogue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.JSONPersist;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 09/02/15.
 */

public class CatalogueList implements JSONPersist{

    // JSON KEYS
    public static final String JSON_NAME = "name";
    public static final String JSON_DESC = "description";
    public static final String JSON_LISTCATALOGUE = "all";

    private Vector<Catalogue> m_mListOfCatalogue = new Vector<>();

    private final Context mContext;

    private CatalogueList(Context context){
        mContext = context;
    }


    /**
     * Add a new catalogue, if it does not exist (name is unique)
     * @param catalogue
     * @return 0 if successful, -1 if name already exists
     */
    public int addCatalogue(Catalogue catalogue){

        if (m_mListOfCatalogue.contains(catalogue))
            return Global.ALREADY_EXISTS;
        m_mListOfCatalogue.add(catalogue);

        return Global.SUCCESS;
    }

    public int getCount(){

        return m_mListOfCatalogue.size();
    }

    public Catalogue getElement(int position){
        if (position > m_mListOfCatalogue.size()-1)
            return null;
        Catalogue res = Catalogue.LoadCatalogue(m_mListOfCatalogue.get(position).getName(), mContext);
        if (res == null)
            return null;

        return res;
    }

    /**
     * Load list of catalogue from the json file
     * @param context context of the application, for the directory path
     * @return the catalogue list if successful, null otherwise
     */
    public static CatalogueList LoadCatalogueListFromJSONFile(Context context){
        String path = context.getFilesDir().getPath();
        CatalogueList res = new CatalogueList(context);

        try {
            JSONObject obj = GeneralTools.getJSONObjectFromFile(path, Global.catalogueList_JSON_filename);
            JSONArray listOfCatalogue = null;
            //if file does not exist, we create it (first launch of the application)
            if (obj == null)
            {
                return res;
            }
            else{
                listOfCatalogue = obj.getJSONArray(JSON_LISTCATALOGUE);
            }


            for (int i = 0; i < listOfCatalogue.length(); i++){
                JSONObject current = listOfCatalogue.getJSONObject(i);
                String name = current.getString(JSON_NAME);
                Catalogue toAdd = Catalogue.LoadCatalogue(name, context);
                res.addCatalogue(toAdd);
            }
        }
        catch(JSONException e){
        }

        return res;
    }


    // IMPLEMENTATION of JSONPersist

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = null;
        try {
            json = new JSONObject();
            JSONArray _list = new JSONArray();
            for (Catalogue el : m_mListOfCatalogue) {
                JSONObject currObj = new JSONObject();
                currObj.put(JSON_NAME, el.getName());
                currObj.put(JSON_DESC, el.getDescription());
                _list.put(currObj);
            }
            json.put(JSON_LISTCATALOGUE, _list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public int writeToJSONFile() {
        JSONObject json = toJSONObject();
        if (json == null)
            return Global.FAILURE;

        return GeneralTools.writeFile(json, Global.catalogueList_JSON_filename, mContext);
    }

    public List<String> getArrayOfString(){

        List<String> res = new ArrayList<>();

        for(Catalogue key: m_mListOfCatalogue){
            res.add(key.getName() + " => " + key.getDescription());
        }
        return res;
    }

}
