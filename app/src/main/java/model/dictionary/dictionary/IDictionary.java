package model.dictionary.dictionary;

import java.util.Date;

/**
 * Created by pietro on 20/03/15.
 * Interface for a Dictionary object
 * A dictionary is a set of objects of the same familly
 */
public interface IDictionary<T> {

    /**
     * Add objectToAdd in this dictionary.
     * Every kind of T object choses its uniqueness constraint
     * @param objectToAdd object to add to the dictionary
     * @return {Global.SUCCESS} if successful, ...
     */
    int addObject(T objectToAdd);

    /**
     * Remove objectToRemove from the dictionary. Cannot revert this action
     * @param objectToRemove object to remove
     * @return {Global.SUCCESS} if successful
     */
    int removeObject(T objectToRemove);

    /**
     * Edit object identified by id, king of merge
     * @param id id of the existing object to edit
     * @param objectWithModifications object to merge with the existing one identified by id
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    int editObject(long id, T objectWithModifications);

    /**
     * Return the last date user checked this object
     * @param id id of the object to check
     * @return the last time the user learnt this object
     */
    Date getLastTimeChecked(long id);

    /**
     * Return the next date user needs to learn this object
     * @param id id of the object
     * @return date, next date for learning this object
     */
    Date getNextTimeTolearn(long id);


    /**
     * !!!!WARNING!!!! Remove everything from the dictionary. CANNOT BE REVERTED
     * @return {Global.SUCCESS} if successful, {Global.FAILURE} otherwise
     */
    int clearAll();

    /**
     * Return the number of objects in the dictionary
     * @return number of objects
     */
    int getCountOfObjects();



}
