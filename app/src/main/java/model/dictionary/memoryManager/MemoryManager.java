package model.dictionary.memoryManager;


/**
 * @author Pierre Gaulard
 * Class used to manage the memory cycle for all the dictionaries objects
 *
 */
public class MemoryManager {


    public MemoryManager (){    }



    public class MemoryPhase{
        public static final int BeginLearning = 1;
        public static final int Learning = 2;
        public static final int KeepInMemory = 3;
        public static final String sBeginLearning = "BeginLearning";
        public static final String sLearning = "BeginLearning";
        public static final String sKeepInMemory = "BeginLearning";


        public String toString(int memoryPhase){
            switch (memoryPhase){
                case BeginLearning:
                    return sBeginLearning;
                case Learning:
                    return sLearning;
                case KeepInMemory:
                    return sKeepInMemory;
                default:
                    return "ERROR";
            }
        }
        public int fromString(String memoryPhase){
            if (memoryPhase.compareTo(sBeginLearning) == 0)
                return BeginLearning;
            if (memoryPhase.compareTo(sLearning) == 0)
                return Learning;
            if (memoryPhase.compareTo(sKeepInMemory) == 0)
                return KeepInMemory;

            return -1;
        }
    }

}
