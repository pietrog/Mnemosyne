package model.dictionary.catalogue.sql;

import android.content.Context;
import android.database.Cursor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import model.dictionary.Global;
import model.dictionary.dictionary.sql.DictionaryContractBase;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoDBHelper;


/**
 * Created by pietro on 22/05/15.
 *
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CatalogueSQLManagerTest {

    private CatalogueSQLManager singleton;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        singleton = CatalogueSQLManager.getInstance(context);
    }

    @After
    public void tearDown() throws Exception {
        MnemoDBHelper.release();
    }

    @Test
    public void testGetAllWithOneCatalogue() throws Exception {
        //TODO Implement this part !! do not forget db is not reset between junit tests
        //TODO i deactivated it because i had some catalogues existing before
        //get all on empty db should return empty cursor
        /*Cursor cursor = singleton.getAll();

        String str = "Catalogues ==> ";
        cursor.moveToFirst();
        do{
            str += GeneralTools.getStringElement(cursor, CatalogueContract.Catalogue.CATALOGUE_NAME);
            str += " -- ";
        }while (cursor.moveToNext());
        System.out.println(str);

        Assert.assertEquals("Cursor should be empty", 0, cursor.getCount());
        cursor.close();

        long idCat1;
        //insert one catalogue
        idCat1 = singleton.add("cat1", "");
        cursor = singleton.getAll();
        Assert.assertEquals("Cursor should contain one element", 1, cursor.getCount());
        cursor.moveToFirst();
        Assert.assertEquals("First element should be equal to idCat1", idCat1, GeneralTools.getLongElement(cursor, CatalogueContract.Catalogue._ID));
        cursor.close();*/
    }

    @Test
    public void testGetAllWithThreeCatalogues() throws Exception {
        long idCat1, idCat2, idCat3;
        //insert three catalogues
        idCat1 = singleton.add("cat1", "");
        idCat2 = singleton.add("cat2", "");
        idCat3 = singleton.add("cat3", "");
        Cursor cursor = singleton.getAll();
        Assert.assertEquals("Cursor should contain three elements", 3, cursor.getCount());
        cursor.moveToFirst();
        do {
            long curr = GeneralTools.getLongElement(cursor, CatalogueContract.Catalogue._ID);
            Assert.assertTrue("Cursor should contain id " + curr, curr == idCat1 || curr == idCat2 || curr == idCat3);
        }while(cursor.moveToNext());
        cursor.close();
    }

    @Test
    public void testGetAllDictionaryOfCatalogue() throws Exception {
        //catalogue does not exist
        Cursor cursor = singleton.getAllDictionaryOfCatalogue(3);
        Assert.assertEquals("Cursor should be empty", 0, cursor.getCount());
        cursor.close();
        //bad ids
        cursor = singleton.getAllDictionaryOfCatalogue(-2);
        Assert.assertEquals("Cursor should be empty", 0, cursor.getCount());
        cursor.close();
        cursor = singleton.getAllDictionaryOfCatalogue(999);
        Assert.assertEquals("Cursor should be empty", 0, cursor.getCount());
        cursor.close();

        //add a catalogue and two dictionaries
        long catID = singleton.add("Cat", "");
        long catID2 = singleton.add("Cat2", "");
        long dictID1 = DictionarySQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext()).addDictionaryInCatalogue(catID, "Dic1", "");
        DictionarySQLManager.getInstance().addDictionaryInCatalogue(catID2, "Dic2", "");
        long dictID2 = DictionarySQLManager.getInstance().addDictionaryInCatalogue(catID, "Dic3", "");
        DictionarySQLManager.getInstance().addDictionaryInCatalogue(catID2, "Dic4", "");
        DictionarySQLManager.getInstance().addDictionaryInCatalogue(catID2, "Dic5", "");
        cursor = singleton.getAllDictionaryOfCatalogue(catID2);
        Assert.assertEquals("Should contain three elements", 3, cursor.getCount());
        cursor = singleton.getAllDictionaryOfCatalogue(catID);
        Assert.assertEquals("Should contain two elements", 2, cursor.getCount());
        cursor.moveToFirst();
        do {
            long curr = GeneralTools.getLongElement(cursor, DictionaryContractBase.DictionaryBase.CID);
            Assert.assertTrue("Should contain dictionary id " + curr, curr == dictID1 || curr == dictID2);
        }while(cursor.moveToNext());
        cursor.close();
    }

    @Test
    public void testAdd() throws Exception {
        //add a new catalogue
        long idCat = singleton.add("CatalogueTest", "DescriptionTest");
        Assert.assertTrue("Should success", idCat > 0);

        //add the same catalogue
        idCat = singleton.add("CatalogueTest", "DescriptionTest");
        Assert.assertEquals("Should fail because of the name", Global.FAILURE, idCat);

        //add another catalogue
        idCat = singleton.add("CatalogueTest2", "DescriptionTest2");
        Assert.assertTrue("Should success", idCat > 0);

        //empty catalogue name
        idCat = singleton.add("", "DescriptionTest");
        Assert.assertEquals("Insert should fail because of the empty name", Global.BAD_PARAMETER, idCat);
    }

    @Test
    public void testRemove() throws Exception {
        //list null
        Assert.assertEquals("Should fail because of null parameter", Global.BAD_PARAMETER, singleton.remove(null));

        //empty list
        long [] list = {};
        Assert.assertEquals("Should return 0 because list is empty", 0, singleton.remove(list));

        //add one remove one
        long [] list2 = {singleton.add("CatalogueTest", "DescriptionTest")};
        Assert.assertEquals("Should return one element removed", 1, singleton.remove(list2));

        //multiple remove
        long [] list3= {singleton.add("CatalogueTest", ""), singleton.add("CatalogueTest2", ""), singleton.add("CatalogueTest3", "")};
        Assert.assertEquals("Should return three elements removed", 3, singleton.remove(list3));
        //do it again should return 0
        Assert.assertEquals("Should return three elements removed", 0, singleton.remove(list3));
    }
}