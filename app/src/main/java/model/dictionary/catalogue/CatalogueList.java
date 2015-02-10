package model.dictionary.catalogue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.tools.GeneralTools;

/**
 * Created by pietro on 09/02/15.
 */
public class CatalogueList {

    private Hashtable<String, String> m_mListOfCatalogue = new Hashtable<>();

    private CatalogueList(){}


    /**
     * Add a new catalogue, if it does not exist (name is unique)
     * @param name name of the catalogue (name is unique
     * @param desc description of the catalogue
     * @return 0 if successful, -1 if name already exists
     */
    public int addCatalogue(String name, String desc){
        if (m_mListOfCatalogue.containsKey(name))
            return -1;
        m_mListOfCatalogue.put(name, desc);
        return 0;
    }

    /**
     * Load list of catalogue from the json file
     * @param context context of the application, for the directory path
     * @return the catalogue list if successful, null otherwise
     */
    public static CatalogueList LoadCatalogueListFromJSONFile(Context context){
        String path = context.getFilesDir().getPath();
        CatalogueList res = new CatalogueList();

        try {
            JSONObject obj = GeneralTools.getJSONObjectFromFile(path, Global.catalogueList_JSON_filename);
            JSONArray listOfCatalogue = null;
            //if file does not exist, we create it (first launch of the application)
            if (obj == null)
            {
                return res;
            }
            else{
                listOfCatalogue = obj.getJSONArray("all");
            }




            for (int i = 0; i < listOfCatalogue.length(); i++){
                JSONObject current = listOfCatalogue.getJSONObject(i);
                String name = current.getString("name");
                String description = current.getString("description");
                res.addCatalogue(name, description);
            }
        }
        catch(JSONException e){
        }


        return res;
    }


    /**
     * Store the catalogue list in a JSON file
     * @param context context of the app
     * @param list The catalogue list to persist
     * @return 0 if successful
     */
    public static int StoreCatalogueListToJSONFile(Context context, CatalogueList list){

        try {

            String path = context.getFilesDir().getPath();
            JSONObject toWrite = new JSONObject();
            JSONArray _list = new JSONArray();
            for (String currName : list.m_mListOfCatalogue.keySet()) {
                JSONObject currObj = new JSONObject();
                currObj.put("name", currName);
                currObj.put("description", list.m_mListOfCatalogue.get(currName));
                _list.put(currObj);
            }
            toWrite.put("all", _list);

            FileOutputStream stream = context.openFileOutput(Global.catalogueList_JSON_filename, Context.MODE_PRIVATE);

            stream.write(toWrite.toString().getBytes());
            stream.flush();
            stream.close();

        }
        catch (JSONException e){

        }
        catch (FileNotFoundException e){

        }
        catch (IOException e){

        }

        return 0;
    }

    public List<String> getArrayOfString(){

        List<String> res = new ArrayList<>();

        for(String key: m_mListOfCatalogue.keySet()){
            res.add(key + " => " + m_mListOfCatalogue.get(key));
        }
        return res;
    }

    public static boolean removeCatalogue(Context context){
        String path = context.getFilesDir().toString();
        File file = new File(path, Global.catalogueList_JSON_filename);
        boolean isDeleted = file.delete();
        return isDeleted;
    }

}
