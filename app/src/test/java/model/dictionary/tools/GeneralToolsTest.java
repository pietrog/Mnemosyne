package model.dictionary.tools;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import model.dictionary.Global;


/**
 * Created by pietro on 14/05/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GeneralToolsTest extends ApplicationTestCase<Application>{

    public GeneralToolsTest(){
        super(Application.class);
    }
    @Test
    public void writeFileTest() throws JSONException {

        JSONObject obj = generateJSONObjectFile();

        String filename = "test.json";
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        String fullPath = context.getFilesDir().getPath()+"/"+filename;

        //should return success
        File file = new File(fullPath);
        Assert.assertFalse("File should not exist ("+fullPath+")", file.exists());
        int result = GeneralTools.writeFile(obj, filename, context);
        Assert.assertEquals("Should return Success", Global.SUCCESS, result);
        //should exist
        file = new File(fullPath);
        Assert.assertTrue("File should exist("+fullPath+")", file.exists());
    }

    @Test
    public void getJSONObjectFromFileTest() throws JSONException {

        JSONObject obj = generateJSONObjectFile();
        String filename = "test.json";
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        String fullPath = context.getFilesDir().getPath()+"/"+filename;

        Assert.assertNull("Should return null when file does not exists", GeneralTools.getJSONObjectFromFile(".", "test.json"));
        GeneralTools.writeFile(obj, filename, context);
        JSONObject objRetrived = GeneralTools.getJSONObjectFromFile(context.getFilesDir().getPath(), filename);
        Assert.assertNotNull("Should return JSONObject, not null", objRetrived);
        //for json object equality we just compare toString of both objects
        String expected = obj.toString();
        String actual = objRetrived.toString();
        Assert.assertTrue("JSONObject should be equal", actual.compareTo(expected) == 0);

    }




    /**
     *
     */
    private JSONObject generateJSONObjectFile() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("Test1", "value1");
        obj.put("Test2", true);

        return obj;
    }
}