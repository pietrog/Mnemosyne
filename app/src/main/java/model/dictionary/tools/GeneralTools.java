package model.dictionary.tools;

import android.content.Context;
import android.util.Log;

import com.mnemo.pietro.mnemosyne.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import model.dictionary.Global;


/**
 * Created by pietro on 09/02/15.
 */
public class GeneralTools {

    /**
     * Return a JSON object built from a json file
     * @param dir directory where the json file is
     * @param fileName name of the source json file
     * @return json object resulting of the fileName json file
     */
    public static JSONObject getJSONObjectFromFile(String dir, String fileName){
        File file = new File(dir, fileName);
        if (!file.exists())
            return null;

        BufferedReader in;
        String line;
        StringBuilder str_build = new StringBuilder();
        JSONObject res = null;

        try{
            in = new BufferedReader(new FileReader(file));
            while((line = in.readLine()) != null)
                str_build.append(line);

            res = new JSONObject(str_build.toString());
        }
        catch (FileNotFoundException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "File not found ", e);
        }
        catch(IOException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "IOException thrown ", e);
        }
        catch(JSONException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "JSON exception thrown ", e);
        }

        return res;
    }

    /**
     * Write the json object into a file named filename
     * @param toWrite json object to write
     * @param filename name of the output file
     * @param context context for the path
     * @return SUCCESS ot FAILURE
     */
    public static int writeFile (JSONObject toWrite, String filename, Context context){

        try {
            FileOutputStream stream = context.openFileOutput(filename, context.MODE_PRIVATE);

            stream.write(toWrite.toString().getBytes());
            stream.flush();
            stream.close();
            Logger.v("GeneralTools::writeFile","File " + filename + " written");
        }
        catch (FileNotFoundException e){
            Logger.d("GeneralTools::writeFile", "File not found during writing", e);
            return Global.FAILURE;
        }
        catch (IOException e){
            Logger.d("GeneralTools::writeFile", "IOException thrown", e);
            return Global.FAILURE;
        }

        return Global.SUCCESS;
    }
}
