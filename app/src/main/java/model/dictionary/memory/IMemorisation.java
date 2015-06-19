package model.dictionary.memory;

import java.util.Date;

import model.dictionary.dictionaryObject.DictionaryObject;
import model.dictionary.dictionaryObject.MemoryObject;

/**
 * Created by pietro on 30/03/15.
 *
 * Interface through we manage memory phases, ...
 */
public interface IMemorisation {

    /**
     * Return the computed next date to learn for object identified by objectid
     * @param object dictionary object
     * @return date for the next alert
     */
    Date computeNextLearningDate(DictionaryObject object);

    /**
     * Postpone the learning date of an object (for exemple when the user did not learn the object at the rappel date)
     * @param object dictionary object
     * @return date of postpone
     */
    Date postponeLearningDate(DictionaryObject object);

    /**
     * Check if the memorisation phase jumps to the next one or not (ENCODING => STORING => UPKEEPING)
     * @param object object to update
     */
    void updateMemorisationPhase(MemoryObject object);
}
