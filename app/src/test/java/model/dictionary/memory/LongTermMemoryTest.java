package model.dictionary.memory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.MnemoDBHelper;


/**
 * Created by pietro on 02/07/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LongTermMemoryTest {

    private LongTermMemory singleton;

    private long cat1ID, cat2ID;
    private long dict1ID, dict2ID, dict3ID, dict4ID, dict5ID;

    @Before
    public void setUp() throws Exception {
        DictionarySQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        CatalogueSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        MemoryManagerSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        MemoryManager.initMemoryPhaseMap();

        singleton = LongTermMemory.getInstance(Robolectric.getShadowApplication().getApplicationContext());

        //add catalogue and dictionary
        cat1ID = CatalogueSQLManager.getInstance().add("Catalogue1", "Desc");
        cat2ID = CatalogueSQLManager.getInstance().add("Catalogue2", "Desc");

        dict1ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat1ID, "Dict 1", "def");
        dict2ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat2ID, "Dict 2", "def");
        dict3ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat1ID, "Dict 3", "def");
        dict4ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat1ID, "Dict 4", "def");
        dict5ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat2ID, "Dict 5", "def");
    }

    @After
    public void tearDown() throws Exception{
        MnemoDBHelper.release();
    }

    @Test
    public void testOnLearntWithNullObject() throws Exception {
        //Assert.assertEquals(singleton.onLearnt(null,1), Global.BAD_PARAMETER);
    }

    @Test
    public void testOnLearntOneWordNormalEvolution() throws Exception {
        /*long idWord = DictionarySQLManager.getInstance().addNewWord(dict1ID, "Word", "Definition");
        MemoryObject object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        singleton.onLearnt(object, 0);
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Assert.assertEquals("Next learning session should be tomorrow", GeneralTools.getDateFromSQLDate(GeneralTools.getSQLDate(cal.getTime())), object.getNextLearnt());
        */
    }
}