package model.dictionary;

import org.json.JSONObject;

/**
 * Created by pietro on 25/02/15.
 */
public interface JSONPersist {

    /**
     * Get the json object from the current catalogue
     * @return a json object if successful
     */
    JSONObject toJSONObject();

    /**
     * Write it
     * @return
     */
    int writeToJSONFile();

}
