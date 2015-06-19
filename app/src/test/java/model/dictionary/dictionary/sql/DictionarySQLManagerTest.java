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
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.dictionaryObject.sql.WordContract;
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
    private long midDict1, midDict2;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.getShadowApplication().getApplicationContext();
        CatalogueSQLManager.getInstance(context);
        MemoryManagerSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        MemoryManager.initMemoryPhaseMap();
        singleton = DictionarySQLManager.getInstance(context);

        //init catalogues
        midCatalogue1 = CatalogueSQLManager.getInstance().add("Catalogue1", "Desc1");
        midCatalogue2 = CatalogueSQLManager.getInstance().add("Catalogue2", "Desc2");

        //init dictionary
        midDict1 = singleton.addDictionaryInCatalogue(midCatalogue1, "DictTest1", "def");
        singleton.addDictionaryInCatalogue(midCatalogue1, "DictTest3", "def");
        midDict2 = singleton.addDictionaryInCatalogue(midCatalogue2, "DictTest2", "def");
    }

    @After
    public void tearDown() throws Exception {
        MnemoDBHelper.release();
    }

    @Test
    public void testGetAllDictionaryObjectsCursor() throws Exception {
        //set up
        long dictID1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict1", "desc");
        long dictID2 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict2", "desc");

        long wordID1 = singleton.addNewWord(dictID1, "Word1", "Def1");

        //get one word
        Cursor cursor = singleton.getAllDictionaryObjectsCursor(dictID1);
        Assert.assertEquals("Should contain one word", 1, cursor.getCount());
        cursor.moveToFirst();
        Assert.assertEquals("Word object id should be equal to", wordID1, GeneralTools.getLongElement(cursor, WordContract.Word.CID));
        cursor.close();

        //get several words
        long wordID3 = singleton.addNewWord(dictID1, "Word3", "Def1");
        singleton.addNewWord(dictID2, "Word2", "Def1");
        long wordID4 = singleton.addNewWord(dictID1, "Word4", "Def1");


        cursor = singleton.getAllDictionaryObjectsCursor(dictID1);
        cursor.moveToFirst();
        Assert.assertEquals("Dictionary should contain 3 words", 3, cursor.getCount());
        do{
            Assert.assertTrue("Id should be in this list", GeneralTools.getLongElement(cursor, WordContract.Word.CID) == wordID1 || GeneralTools.getLongElement(cursor, WordContract.Word.CID) == wordID3 || GeneralTools.getLongElement(cursor, WordContract.Word.CID) == wordID4);
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
        long idWord1 = singleton.addNewWord(idDict, "Word1", "");
        long idWord2 = singleton.addNewWord(idDict, "Word2", "");
        long idWord3 = singleton.addNewWord(idDict, "Word3", "");
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

    /**
     * method AddNewWord
     */
    @Test
    public void testAddNewWord() throws Exception {
        //set up
        long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        long idWord = singleton.addNewWord(idDict, "WordTest", "Definition Test");
        Assert.assertTrue("A new word should be created", idWord > 0);
        long idWord2 = singleton.addNewWord(idDict, "WordTest2", "Definition Test");
        Assert.assertTrue("A new word should be created", idWord2 > 0 && idWord != idWord2);
        long idWord3 = singleton.addNewWord(idDict, "WordTest3", "Definition Test");
        Assert.assertTrue("A new word should be created", idWord3 > 0 && idWord2 != idWord3 && idWord3 != idWord);
    }
    @Test
    public void testAddNewWordWhenWordIsEmpty() throws Exception {
        //@TODO HIGH when we add monitoring object and dictionary object, but fail to add the word, the first ones stay in DB !!!
        long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        long idWord = singleton.addNewWord(idDict, "", "Definition Test");
        Assert.assertEquals("We could not add empty word", Global.BAD_PARAMETER, idWord);
    }
    @Test
    public void testAddNewWordWhenWordIsNull() throws Exception {
        long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        long idWord = singleton.addNewWord(idDict, null, "Definition Test");
        Assert.assertEquals("We could not add null word", Global.BAD_PARAMETER, idWord);
    }
    @Test
    public void testAddNewWordWhenAlreadyexists() throws Exception {
        //@TODO MODERATE we should make impossible to add two same words in the same dictionary
        /*long idDict = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict", "");
        singleton.addNewWord(idDict, "word1", "Definition Test");
        long idWord = singleton.addNewWord(idDict, "word1", "Definition Test");
        Assert.assertEquals("We could not add the same word in the same dictionary", -1, idWord);*/
    }


        /**
         * method Remove
         */
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

    /**
     * method createNewDictionaryObject
     */
    @Test
    public void testCreateNewDictionaryObjectWhenDictionaryIDIsWrong() throws Exception {
        long memID = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long id = singleton.createNewDictionaryObject(-1, memID);
        Assert.assertEquals("Should return bad parameter", Global.BAD_PARAMETER, id);
        id = singleton.createNewDictionaryObject(9, memID);
        Assert.assertEquals("Should return 0 because dictionary object id points on unknown row", -1, id);
    }
    @Test
    public void testCreateNewDictionaryObjectWhenMemoryIDIsWrong() throws Exception {
        long dict1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict1", "Test");
        long id = singleton.createNewDictionaryObject(dict1, -1);
        Assert.assertEquals("Creation should fail !", -1, id);
        id = singleton.createNewDictionaryObject(dict1, 1);
        Assert.assertEquals("Creation should fail because of an unknown memory object id", -1, id);
    }
    @Test
    public void testCreateNewDictionaryObjectWhenAllFine() throws Exception {
        long dict1 = singleton.addDictionaryInCatalogue(midCatalogue1, "Dict1", "Test");
        long memObjid1 = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long id = singleton.createNewDictionaryObject(dict1, memObjid1);
        Assert.assertTrue("Insert should success", id > 0);
        long memObjid2 = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long id2 = singleton.createNewDictionaryObject(dict1, memObjid2);
        Assert.assertTrue("Insert should success", id2 > 0);
    }

    /**
     * method getMemoryObjectFromDictionaryObjectID
     */
    @Test
    public void testGetMemoryObjectFromIDWhenDictionaryObjIDIsWrong() throws Exception {
        MemoryObject obj1 = singleton.getMemoryObjectFromDictionaryObjectID(-1);
        Assert.assertNull("Should return null pointer", obj1);
        obj1 = singleton.getMemoryObjectFromDictionaryObjectID(3);
        Assert.assertNull("Should return null pointer", obj1);
    }
    @Test
    public void testGetMemoryObjectFromID() throws Exception {
        MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long memObjID1 = MemoryManagerSQLManager.getInstance().createNewMemoryMonitoringObject();
        long dictObjID1 = singleton.createNewDictionaryObject(midDict1, memObjID1);
        MemoryObject obj1 = singleton.getMemoryObjectFromDictionaryObjectID(dictObjID1);
        Assert.assertNotNull("Should return memory object", obj1);
        Assert.assertEquals("Check object parameters", memObjID1, obj1.getMemoryObjectID());
        Assert.assertEquals("Check object parameters", dictObjID1, obj1.getDictionaryObjectID());
    }

    /**
     * method AddDictionaryInCatalogue
     */
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

    /**
     * method GetWordFromID
     */
    @Test
    public void testGetWordFromID() throws Exception {
        singleton.addNewWord(midDict1, "Word1", "Def");
        long wordid2 = singleton.addNewWord(midDict1, "Word2", "Def");
        singleton.addNewWord(midDict1, "Word3", "Def");
        long wordid4 = singleton.addNewWord(midDict2, "Word4", "Def");

        WordDefinitionObj obj1 = singleton.getWordFromID(wordid2);
        Assert.assertEquals("Should be equal", wordid2, obj1.getID());
        Assert.assertEquals("Should be equal", midDict1, obj1.getDictionaryID());
        Assert.assertEquals("Word should be the same", "Word2", obj1.getWord());

        WordDefinitionObj obj2= singleton.getWordFromID(wordid4);
        Assert.assertEquals("Should be equal", wordid4, obj2.getID());
        Assert.assertEquals("Should be equal", midDict2, obj2.getDictionaryID());
        Assert.assertEquals("Word should be the same", "Word4", obj2.getWord());
    }
    @Test
    public void testGetWordFromIDWhenWordIDIsWrong() throws Exception {
        Assert.assertEquals("Should return null object", null, singleton.getWordFromID(-1));
        Assert.assertEquals("Should return null object", null, singleton.getWordFromID(3));
        Assert.assertEquals("Should return null object", null, singleton.getWordFromID(1));
    }

}