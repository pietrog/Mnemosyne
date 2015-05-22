package model.dictionary.dictionary.sql;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.tools.MnemoDBHelper;

import static org.junit.Assert.*;

/**
 * Created by pietro on 22/05/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DictionarySQLManagerTest {

    private DictionarySQLManager singleton;

    private long midCatalogue1, midCatalogue2;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        CatalogueSQLManager.getInstance(context);
        singleton = DictionarySQLManager.getInstance(context);

        //init catalogues
        midCatalogue1 = CatalogueSQLManager.getInstance().add("Catalogue1", "Desc1");
        midCatalogue2 = CatalogueSQLManager.getInstance().add("Catalogue2", "Desc2");
    }

    @After
    public void tearDown() throws Exception {
        MnemoDBHelper.release();
    }

    @Test
    public void testGetAllDictionaryObjectsCursor() throws Exception {

    }

    @Test
    public void testGetDictionaryObjectsFromLearningList() throws Exception {

    }

    @Test
    public void testAddNewWord() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {
        //empty list
        long[] list = {};
        Assert.assertEquals("Should return 0", 0, singleton.remove(list));
        //remove ids that does not exist
        long[] list2 = {4, 8};
        Assert.assertEquals("Should return 0", 0, singleton.remove(list2));
        //remove existing id
        long id1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Test", "test");
        long id2 = singleton.addDictionaryInCatalogue(midCatalogue1, "Test2", "test");
        long id3 = singleton.addDictionaryInCatalogue(midCatalogue1, "Test3", "test");
        long id4 = singleton.addDictionaryInCatalogue(midCatalogue1, "Test4", "test");
        long[] list3 = {id1};
        long[] list4 = {id2, id3, id4};
        int res = singleton.remove(list3);
         res = singleton.remove(list3);
         res = singleton.remove(list4);

        Assert.assertEquals("Should return 1", 1, res);
        Assert.assertEquals("Should return 0", 0, singleton.remove(list3));
        Assert.assertEquals("Should return 3", 3, singleton.remove(list4));
        Assert.assertEquals("Should return 0", 0, singleton.remove(list4));
    }

    @Test
    public void testGetFullObjectFromID() throws Exception {

    }

    @Test
    public void testAddDictionaryInCatalogue() throws Exception {
        //fails because of empty name
        long id1 = singleton.addDictionaryInCatalogue(midCatalogue1, "", "Desc");
        Assert.assertEquals("Should fail because of empty name", Global.BAD_PARAMETER, id1);

        //fails because of bad parameters
        id1 = singleton.addDictionaryInCatalogue(midCatalogue1, null, "");
        Assert.assertEquals("Should return bad parameters, because name is null", Global.BAD_PARAMETER, id1);
        id1 = singleton.addDictionaryInCatalogue(midCatalogue1, "NMAE", null);
        Assert.assertEquals("Should return bad parameters, because description is null", Global.BAD_PARAMETER, id1);

        //should success
        id1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict1", "");
        long id2 = singleton.addDictionaryInCatalogue(midCatalogue2, "Dict2", "jdj");
        Assert.assertTrue("ID should be greater than 0", id1 > 0);
        Assert.assertTrue("ID should be greater than 0", id2 > 0);
        Assert.assertNotEquals("Ids should be different", id1, id2);
    }
}