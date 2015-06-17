package model.dictionary.dictionary.sql;

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

import java.util.Vector;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionaryObject.DictionaryObject;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoDBHelper;

/**
 * Created by pietro on 22/05/15.
 *
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
        MemoryManagerSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
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
        //set up
        long dictID1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict1", "desc");
        //long dictID2 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict2", "desc");

        DictionaryObject.MemoryMonitoringObject memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj1 = MemoryManagerSQLManager.getInstance().createDictionaryObject(dictID1, memObj.getID());
        long idWord1 = singleton.addNewWord(idDictObj1, "WordTest", "Definition Test");

        //get one word
        Cursor cursor = singleton.getAllDictionaryObjectsCursor(dictID1);
        Assert.assertEquals("Should contain one word", 1, cursor.getCount());
        cursor.moveToFirst();
        Assert.assertEquals("Word id should be equal to", idWord1, GeneralTools.getLongElement(cursor, "_id"));
        cursor.close();

        //get several words
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj2 = MemoryManagerSQLManager.getInstance().createDictionaryObject(dictID1, memObj.getID());
        long idWord2 = singleton.addNewWord(idDictObj2, "WordTest", "Definition Test");
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj3 = MemoryManagerSQLManager.getInstance().createDictionaryObject(dictID1, memObj.getID());
        long idWord3 = singleton.addNewWord(idDictObj3, "WordTest", "Definition Test");

        cursor = singleton.getAllDictionaryObjectsCursor(dictID1);
        cursor.moveToFirst();
        Assert.assertEquals("Dictionary should contain 3 words", 3, cursor.getCount());
        do{
            Assert.assertTrue("Id should be in this list", GeneralTools.getLongElement(cursor, "_id") == idWord1 || GeneralTools.getLongElement(cursor, "_id") == idWord2 || GeneralTools.getLongElement(cursor, "_id") == idWord3);
        }while(cursor.moveToNext());
        cursor.close();

        //remove one word
        /*long[] list = {idWord2};
        cursor = singleton.getAllDictionaryObjectsCursor(dictID1);
        cursor.moveToFirst();
        Assert.assertEquals("Dictionary should contain 2 words", 2, cursor.getCount());
        do{
            Assert.assertTrue("Id should be in this list", GeneralTools.getLongElement(cursor, "_id") == idWord1  || GeneralTools.getLongElement(cursor, "_id") == idWord3);
        }while(cursor.moveToNext());
        cursor.close();*/

    }

    @Test
    public void testGetDictionaryObjectsFromLearningList() throws Exception {

        //list null or empty
        Cursor cursor = singleton.getDictionaryObjectsFromLearningList(null);
        Assert.assertNull("Null list should return null cursor", cursor);
        //empty list should return null cursor
        cursor = singleton.getDictionaryObjectsFromLearningList(null);
        Assert.assertNull("Empty list should return null cursor", cursor);
        //list with unknown ids
        Global.Couple c1 = new Global.Couple(2, 0);
        Global.Couple c2 = new Global.Couple(1, 0);
        Global.Couple c3 = new Global.Couple(4, 0);
        Vector<Global.Couple<Long, Integer>> list = new Vector<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
        cursor = singleton.getDictionaryObjectsFromLearningList(list);
        Assert.assertTrue("Should return an empty cursor because of unknown ids", cursor.getCount() == 0);

        //list of dictionary objects
        //set up
        long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        MemoryManager.initMemoryPhaseMap();
        DictionaryObject.MemoryMonitoringObject memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj1 = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        long idWord1 = singleton.addNewWord(idDictObj1, "Word1", "");
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj2 = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        long idWord2 = singleton.addNewWord(idDictObj2, "Word1", "");
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj3 = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        long idWord3 = singleton.addNewWord(idDictObj3, "Word1", "");
        c1 = new Global.Couple(idWord1, 0);
        c2 = new Global.Couple(idWord2, 0);
        c3 = new Global.Couple(idWord3, 0);
        list = new Vector<>();
        list.add(c2);
        list.add(c1);
        list.add(c3);
        cursor = singleton.getDictionaryObjectsFromLearningList(list);
        Assert.assertEquals("Should contain 3 objects", 3, cursor.getCount());
        cursor.moveToFirst();
        do{
            Assert.assertTrue("We should have a coherent result", GeneralTools.getLongElement(cursor, "_id") == idWord1 || GeneralTools.getLongElement(cursor, "_id") == idWord2 || GeneralTools.getLongElement(cursor, "_id") == idWord3);
        }while(cursor.moveToNext());

    }

    @Test
    public void testAddNewWord() throws Exception {
        //set up
        long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        MemoryManager.initMemoryPhaseMap();

        DictionaryObject.MemoryMonitoringObject memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long idDictObj = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        long idWord = singleton.addNewWord(idDictObj, "WordTest", "Definition Test");
        Assert.assertTrue("A new word should be created", idWord > 0);
        //add the same word should fail
        //@TODO MODERATE we should make impossible to add two same words in the same dictionary
        /*memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        idDictObj = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        idWord = singleton.addNewWord(idDictObj, "WordTest", "Definition Test bit different");
        Assert.assertEquals("We could not add the same word", Global.FAILURE, idWord);*/

        //cannot add an empty word
        //@TODO HIGH when we add monitoring object and dictionary object, but fail to add the word, the first ones stay in DB !!!
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        idDictObj = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        idWord = singleton.addNewWord(idDictObj, "", "Definition Test");
        Assert.assertEquals("We could not add empty word", Global.BAD_PARAMETER, idWord);

        //cannot add an null word
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        idDictObj = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        idWord = singleton.addNewWord(idDictObj, null, "Definition Test");
        Assert.assertEquals("We could not add null word", Global.BAD_PARAMETER, idWord);

        //add another one word
        memObj = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        idDictObj = MemoryManagerSQLManager.getInstance().createDictionaryObject(idDict, memObj.getID());
        idWord = singleton.addNewWord(idDictObj, "WORD 2", "Definition Test");
        Assert.assertTrue("Add the word should success", idWord > 0);
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
        Assert.assertEquals("Should return 1", 1, res);
        res = singleton.remove(list3);
        Assert.assertEquals("Should return 0", 0, res);
        res = singleton.remove(list4);
        Assert.assertEquals("Should return 1", 3, res);
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