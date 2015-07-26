package model.dictionary.tools;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import model.dictionary.Global;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;


/**
 * Created by pietro on 14/05/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GeneralToolsTest extends ApplicationTestCase<Application>{

    @Before
    public void setUp() throws Exception {
        MnemoCalendar.reset();
    }

    @After
    public void tearDown() throws Exception {
        MnemoCalendar.reset();
    }

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

    @Test
    public void getNumberOfDaysBetweenTwoDatesWhenEqualDates(){
        Date d1 = GeneralTools.getFormattedDate(MnemoCalendar.getInstance().getTime());
        Date d2 = GeneralTools.getFormattedDate(MnemoCalendar.getInstance().getTime());
        Assert.assertThat(d1, equalTo(d2));
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), equalTo((long) 0));
    }
    @Test
    public void getNumberOfDaysBetweenTwoDatesWhenNotEqualDates(){
        Date d1 = GeneralTools.getFormattedDate(MnemoCalendar.getInstance().getTime());
        Date d2 = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(d1, not(d2));
    }
    @Test
    public void getNumberOfDaysBetweenTwoDatesWhenOneDayBetween(){
        //formatted dates
        Date d1 = GeneralTools.getFormattedDate(MnemoCalendar.getInstance().getTime());
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 1);
        Date d2 = GeneralTools.getFormattedDate(MnemoCalendar.getInstance().getTime());
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), equalTo(((long)1)));

        //dates with milliseconds
        MnemoCalendar.reset();
        d1 = MnemoCalendar.getInstance().getTime();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 1);
        d2 = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), equalTo(((long) 1)));
    }
    @Test
    public void getNumberOfDaysBetweenTwoDatesWhenFewDaysBetween(){
        Date d1 = MnemoCalendar.getInstance().getTime();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 3);
        Date d2 = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), equalTo(((long)3)));
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d2.getTime(), d1.getTime()), equalTo(((long)3)));

        MnemoCalendar.reset();
        d1 = MnemoCalendar.getInstance().getTime();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 9);
        d2 = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), equalTo(((long)9)));
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d2.getTime(), d1.getTime()), equalTo(((long)9)));

        MnemoCalendar.reset();
        d1 = MnemoCalendar.getInstance().getTime();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 35);
        d2 = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d1.getTime(), d2.getTime()), is(((long) 35)));
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d2.getTime(), d1.getTime()), is(((long)35)));
        Assert.assertThat(GeneralTools.getNumberOfDaysBetweenTwoDates(d2.getTime(), d1.getTime()), not(((long)34)));
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