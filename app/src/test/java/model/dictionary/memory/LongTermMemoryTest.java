package model.dictionary.memory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;

import model.dictionary.Global;
import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.dictionaryObject.MemoryObject;
import model.dictionary.memoryManager.MemoryManager;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;
import model.dictionary.tools.GeneralTools;
import model.dictionary.tools.MnemoCalendar;
import model.dictionary.tools.MnemoDBHelper;

import static model.dictionary.tools.GeneralTools.getNumberOfMillisBetweenTwoDates;
import static model.dictionary.tools.TestEnvironment.DELTAMAXINMILLI;
import static org.hamcrest.CoreMatchers.is;


/**
 * Created by pietro on 02/07/15.
 *
 * Test the LongTermMemory class.
 *
 * Test are implemented for the first version of LongTermMemory: we DO NOT take in account the possibility of a delay
 * Next phase will always be on BeginningOfPhase + maxDate(Phase Duration , next learn)
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LongTermMemoryTest {

    private LongTermMemory singleton;

    private long dict1ID;

    @Before
    public void setUp() throws Exception {
        MnemoCalendar.reset();
        DictionarySQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        CatalogueSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        MemoryManagerSQLManager.getInstance(Robolectric.getShadowApplication().getApplicationContext());
        MemoryManager.initMemoryPhaseMap();

        singleton = LongTermMemory.getInstance(Robolectric.getShadowApplication().getApplicationContext());

        //add catalogue and dictionary
        long cat1ID = CatalogueSQLManager.getInstance().add("Catalogue1", "Desc");

        dict1ID = DictionarySQLManager.getInstance().addDictionaryInCatalogue(cat1ID, "Dict 1", "def");
    }

    @After
    public void tearDown() throws Exception{
        MnemoDBHelper.release();
        MnemoCalendar.reset();
    }

    @Test
    public void testOnLearntWithNullObject() throws Exception {
        //Assert.assertEquals(singleton.onLearnt(null,1), Global.BAD_PARAMETER);
    }

    @Test
    public void testOnLearntFirstPhase() throws Exception {
        long idWord = DictionarySQLManager.getInstance().addNewWord(dict1ID, "Word", "Definition");
        MemoryObject object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        long idDictObj = object.getDictionaryObjectID();

        //init futures dates
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 1);
        long dP1 = MnemoCalendar.getInstance().getTimeInMillis();//set now day 1 (after today), then 2, 3, ...
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 2);
        long dP2 = MnemoCalendar.getInstance().getTimeInMillis();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 3);
        long dP3 = MnemoCalendar.getInstance().getTimeInMillis();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 4);
        long dP4 = MnemoCalendar.getInstance().getTimeInMillis();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 5);
        long dP5 = MnemoCalendar.getInstance().getTimeInMillis();
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 6);

        /*Date d1 = new Date(dP1);
        Date d2 = new Date(dP2);
        Date d3 = new Date(dP3);
        Date d4 = new Date(dP4);
        Date d5 = new Date(dP5);*/

        //day 0
        Assert.assertThat(getNumberOfMillisBetweenTwoDates(dP1, object.getNextLearn()) < DELTAMAXINMILLI, is(true));
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P1));

        //day 1 (after today)
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 1);
        Assert.assertThat(singleton.onLearnt(idDictObj), is(Global.SUCCESS));
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Assert.assertThat(GeneralTools.isTheSameDay(dP2, object.getNextLearn()), is(true));
        Assert.assertThat(GeneralTools.isTheSameDay(dP1, object.getLastLearnt().getTime()), is(true));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P1));

        //day 2
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 2);
        Assert.assertThat(singleton.onLearnt(idDictObj), is(Global.SUCCESS));
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Assert.assertThat(GeneralTools.isTheSameDay(dP3, object.getNextLearn()), is(true));
        Assert.assertThat(GeneralTools.isTheSameDay(dP2, object.getLastLearnt().getTime()), is(true));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P1));

        //day 3
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 3);
        Assert.assertThat(singleton.onLearnt(idDictObj), is(Global.SUCCESS));
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Assert.assertThat(GeneralTools.isTheSameDay(dP4, object.getNextLearn()), is(true));
        Assert.assertThat(GeneralTools.isTheSameDay(dP3, object.getLastLearnt().getTime()), is(true));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P1));

        //day 4
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 4);
        Assert.assertThat(singleton.onLearnt(idDictObj), is(Global.SUCCESS));
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Assert.assertThat(GeneralTools.isTheSameDay(dP5, object.getNextLearn()), is(true));
        Assert.assertThat(GeneralTools.isTheSameDay(dP4, object.getLastLearnt().getTime()), is(true));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P1));

        //day 5 (end of phase)
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 5);
        Assert.assertThat(singleton.onLearnt(idDictObj), is(Global.SUCCESS));
        object = DictionarySQLManager.getInstance().getWordFromID(idWord);
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P2));
        Assert.assertThat(GeneralTools.isTheSameDay(object.getBeginningOfMP().getTime(), dP5), is(true));
    }

    @Test
    public void testOnLearntSecondPhase() throws Exception {
        long idWord = DictionarySQLManager.getInstance().addNewWord(dict1ID, "Word2", "Definition");
        MemoryObject object = DictionarySQLManager.getInstance().getWordFromID(idWord);

        //Go to the phase 2
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 1);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 2);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 3);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 4);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 5);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));

        //Check the phase 2
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P2));
        Assert.assertThat(object.getDaysBetween(), is(5));
        //if we want to learn before the learn date, it is not taken in account
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 7);
        Assert.assertThat(singleton.onLearnt(object), is(Global.NOTHING_DONE));
        //the next learn date should be n days after the last learn session of the phase 1
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 10);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getDaysBetween(), is(6));
        //then n+1 days after the last learn session
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 16);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getDaysBetween(), is(7));
        //go to the end of phase
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 99);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getDaysBetween(), is(8));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, 107);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P3));
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P3));
    }

    @Test
    public void testOnLearntThirdPhase() throws Exception {
        long idWord = DictionarySQLManager.getInstance().addNewWord(dict1ID, "Word3", "Definition");
        MemoryObject object = DictionarySQLManager.getInstance().getWordFromID(idWord);

        //go to the second phase
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, Global.DURATION_PHASE_P1);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P2));
        //go to the third phase
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, Global.DURATION_PHASE_P1 + Global.DURATION_PHASE_P2);
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Assert.assertThat(object.getMemoryPhaseName(), is(Global.PHASE_NAME_P3));

        //check the two first learn sessions
        Assert.assertThat(GeneralTools.isTheSameDay(object.getBeginningOfMP(), MnemoCalendar.getInstance().getTime()), is(true));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, Global.DURATION_PHASE_P1 + Global.DURATION_PHASE_P2 + Global.FIRST_PERIOD_P3);
        long firstLearnDate = MnemoCalendar.getInstance().getTimeInMillis();
        Assert.assertThat(GeneralTools.isTheSameDay(object.getNextLearn(), firstLearnDate), is(true));

        //second learn date
        Assert.assertThat(singleton.onLearnt(object), is(Global.SUCCESS));
        Date dd = MnemoCalendar.getInstance().getTime();
        Assert.assertThat(GeneralTools.isTheSameDay(object.getLastLearnt(), MnemoCalendar.getInstance().getTime()), is(true));
        MnemoCalendar.init(Calendar.DAY_OF_YEAR, Global.DURATION_PHASE_P1 + Global.DURATION_PHASE_P2 + 2 * Global.FIRST_PERIOD_P3);
        long secondLearnDate = MnemoCalendar.getInstance().getTimeInMillis();
        Assert.assertThat(GeneralTools.isTheSameDay(object.getNextLearn(), secondLearnDate), is(true));
        Assert.assertThat(object.getDaysBetween(), is(Global.FIRST_PERIOD_P3));
    }
}