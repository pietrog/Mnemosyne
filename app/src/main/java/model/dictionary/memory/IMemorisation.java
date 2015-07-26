package model.dictionary.memory;

import model.dictionary.dictionaryObject.MemoryObject;

/**
 * Created by pietro on 30/03/15.
 *
 * Interface through we manage memory phases, ...
 */
public interface IMemorisation {

    /**
     * Check if the memorisation phase jumps to the next one or not (ENCODING => STORING => UPKEEPING)
     * @param dictionaryObjectID id of sal dictionary object
     * @return {Global.SUCCESS} if successful incremented or changed the phase, {Global.NOTTHING_DONE} if nothing done(now date is before the next learn date), {Global.FAILURE} otherwise
     */
    int onLearnt(long dictionaryObjectID);
    int onLearnt(MemoryObject object);
}
