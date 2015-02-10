package model.dictionary.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by pietro on 09/02/15.
 */
public class GeneralTools {

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
        }
        catch(IOException e){
        }
        catch(JSONException e){
        }

        return res;
    }
}
