package model.dictionary;


/**
 * Created by pietro on 09/02/15.
 *
 */
public class Global {

    public static final String catalogueList_JSON_filename = "JSON_catalogue_list";

    public static final String FORMAT_SQL_DATE = "dd:MMM:yyyy";


    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final int NOT_AVAILABLE = 3;
    public static final int ALREADY_EXISTS = 4;
    public static final int NOT_FOUND = 5;
    public static final int OUT_OF_BOUNDS = 6;
    public static final int BAD_PARAMETER = 7;
    public static final int NOTHING_DONE = 15;


    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String COMMASEP = " ,";
    public static final String DEFAULT_EMPTY_STRING = " DEFAULT '' ";

    public static final long DAYINMILLIS = 86400000;
    public static final long HOURINMILLI = 3600000;


    /**
     * DATABASE RELATED SECTION
     */
    //First phase
    public static final String PHASE_NAME_P1 = "ENCODING";
    public static final int DURATION_PHASE_P1 = 5;
    public static final int PERIOD_INCREMENT_P1 = 0;
    public static final int FIRST_PERIOD_P1 = 1;
    //Second phase
    public static final String PHASE_NAME_P2 = "STORING";
    public static final int DURATION_PHASE_P2 = 95;
    public static final int PERIOD_INCREMENT_P2 = 1;
    public static final int FIRST_PERIOD_P2 = 5;
    //Third phase
    public static final String PHASE_NAME_P3 = "UPKEEPING";
    public static final int DURATION_PHASE_P3 = 0;
    public static final int PERIOD_INCREMENT_P3 = 0;
    public static final int FIRST_PERIOD_P3 = 62;

    public static final String LAST_PHASE_NAME = PHASE_NAME_P3;

    public static String getLogFromResult(int result){
        switch (result){
            case Global.SUCCESS:
                return " SUCCESS ";
            case Global.NOT_FOUND:
                return " NOT FOUND ";
            case Global.ALREADY_EXISTS:
                return " ALREADY EXISTS ";
            case Global.FAILURE:
                return " FAILURE ";
            case Global.OUT_OF_BOUNDS:
                return " OUT OF BOUNDS ";
            case Global.NOT_AVAILABLE:
                return " NOT AVAILABLE ";
            default:
                return " CODE(" + result+") unknown ";

        }
    }


    public static class Couple<T,C>{
        public T val1;
        public C val2;

        public Couple(T t, C c){
            val1 = t;
            val2 = c;
        }
    }

}
