package model.dictionary.tools;

import android.app.Application;
import android.content.Context;
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

import model.dictionary.Global;

import static org.junit.Assert.*;

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

        JSONObject obj = new JSONObject();
        obj.put("Test1", "value1");
        obj.put("Test2", true);

        String filename = "test.json";
        Context context = Robolectric.getShadowApplication().getApplicationContext();

        //should return success
        File file = new File(context.getFilesDir().getPath()+filename);
        Assert.assertFalse("File should not exist", file.exists());
        int result = GeneralTools.writeFile(obj, filename, context);
        Assert.assertEquals("Should return Success", Global.SUCCESS, result);
        //should exist
        file = new File(context.getFilesDir().getPath()+filename);
        Assert.assertTrue("File should exist", file.exists());
    }

    @Test
    public void getJSONObjectFromFileTest(){
        Assert.assertNull("Should return null when file does not exists", GeneralTools.getJSONObjectFromFile(".", "test.json"));

    }
}