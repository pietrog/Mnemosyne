package model.dictionary.tools;

import android.database.Cursor;

import java.util.Calendar;

import model.dictionary.catalogue.sql.CatalogueSQLManager;
import model.dictionary.dictionary.sql.DictionarySQLManager;
import model.dictionary.dictionaryObject.WordDefinitionObj;
import model.dictionary.memoryManager.sql.MemoryManagerSQLManager;

/**
 * Created by pietro on 24/07/15.
 *
 */
public class TestEnvironment {


    //used for unit testing, delta max in order to not handle milliseconds in the program
    public static final long DELTAMAXINMILLI = 1800000;
    /**
     * Create a set of datas in the app
     */
    public static void initDBSetOfTest(){
        Cursor cursor = CatalogueSQLManager.getInstance().getAll();
        if (cursor != null && cursor.getCount() > 0)
            return;

        //catalogues
        long idCatDictionaries = CatalogueSQLManager.getInstance().add("Dictionaires langue etrangeres", "Apprendre des langues etrangeres");
        //long idCatHistory = CatalogueSQLManager.getInstance().add("Histoire", "Apprendre l'histoire");

        //dictionaries
        long idDictUK = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatDictionaries, "Anglais", "Dictionaire d'anglais");
        long idDictIT = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatDictionaries, "Italien", "Dictionaire d'italien");
        //long idDictHistFR = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatHistory, "Histoire de france", "Des elements de l'histoire de france");
        //long idDictHistRUSSIE = DictionarySQLManager.getInstance().addDictionaryInCatalogue(idCatHistory, "Histoire de russie", "Que s'est il passe en Russie ?");

        //words
        long wordUK1 = DictionarySQLManager.getInstance().addNewWord(idDictUK, "Throw", "(to) throw, Propel something with force through the air");
        //long wordUK2 = DictionarySQLManager.getInstance().addNewWord(idDictUK, "Add", "(to) add, join something to something else as to increase the size");
        long wordUK3 = DictionarySQLManager.getInstance().addNewWord(idDictUK, "Car", "a road vehicle, typically with four wheels");
        long wordUK4 = DictionarySQLManager.getInstance().addNewWord(idDictUK, "Wheel", "A circular object that revolves on an axis.");

        long wordIT1 = DictionarySQLManager.getInstance().addNewWord(idDictIT, "Piacere", "Risultare gradito a qualcosa");
        long wordIT2 = DictionarySQLManager.getInstance().addNewWord(idDictIT, "Andare", "Muoversi, camminando o con un mezzo di locomozione, e dirigersi verso un luogo o una persona");
        long wordIT3 = DictionarySQLManager.getInstance().addNewWord(idDictIT, "Comprare", "Entrare in possesso di qlco. attraverso il pagamento del prezzo fissato");

        //modify some dates
        Calendar calM1 = MnemoCalendar.getInstance();
        calM1.add(Calendar.DAY_OF_YEAR, -1);
        Calendar calM2 = MnemoCalendar.getInstance();
        calM2.add(Calendar.DAY_OF_YEAR, -2);
        Calendar calM4 = MnemoCalendar.getInstance();
        calM4.add(Calendar.DAY_OF_YEAR, -4);
        Calendar calM5 = MnemoCalendar.getInstance();
        calM5.add(Calendar.DAY_OF_YEAR, -5);

        Calendar cal = MnemoCalendar.getInstance();

        WordDefinitionObj obj = DictionarySQLManager.getInstance().getWordFromID(wordUK1);
        obj.setBegMP(calM2.getTime());
        obj.setDateAdded(calM2.getTime());
        obj.setNext(cal.getTimeInMillis());
        obj.setmLastLearnt(calM1.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);


        obj = DictionarySQLManager.getInstance().getWordFromID(wordUK3);
        obj.setBegMP(calM5.getTime());
        obj.setDateAdded(calM5.getTime());
        obj.setNext(cal.getTimeInMillis());
        obj.setmLastLearnt(calM1.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);

        obj = DictionarySQLManager.getInstance().getWordFromID(wordIT2);
        obj.setBegMP(calM4.getTime());
        obj.setDateAdded(calM4.getTime());
        obj.setNext(cal.getTimeInMillis());
        obj.setmLastLearnt(calM1.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);


        //in the past
        Calendar calM7 = MnemoCalendar.getInstance();
        calM7.add(Calendar.DAY_OF_YEAR, -7);
        Calendar calM9 = MnemoCalendar.getInstance();
        calM9.add(Calendar.DAY_OF_YEAR, -9);
        Calendar calM6 = MnemoCalendar.getInstance();
        calM6.add(Calendar.DAY_OF_YEAR, -6);
        Calendar calM8 = MnemoCalendar.getInstance();
        calM8.add(Calendar.DAY_OF_YEAR, -8);
        Calendar calM3 = MnemoCalendar.getInstance();
        calM3.add(Calendar.DAY_OF_YEAR, -3);

        //WordDefinitionObj obj2 = DictionarySQLManager.getInstance().getWordFromID(wordUK2);
        obj = DictionarySQLManager.getInstance().getWordFromID(wordIT3);
        obj.setBegMP(calM9.getTime());
        obj.setDateAdded(calM9.getTime());
        obj.setNext(calM5.getTimeInMillis());
        obj.setmLastLearnt(calM6.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);

        obj = DictionarySQLManager.getInstance().getWordFromID(wordUK4);
        obj.setBegMP(calM7.getTime());
        obj.setDateAdded(calM7.getTime());
        obj.setNext(calM5.getTimeInMillis());
        obj.setmLastLearnt(calM6.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);

        obj = DictionarySQLManager.getInstance().getWordFromID(wordIT1);
        obj.setBegMP(calM4.getTime());
        obj.setDateAdded(calM4.getTime());
        obj.setNext(calM1.getTimeInMillis());
        obj.setmLastLearnt(calM2.getTime());
        MemoryManagerSQLManager.getInstance().updateMemoryObjectInDB(obj);
    }
}
