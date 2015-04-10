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


    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String COMMASEP = " ,";
    public static final String DEFAULT_EMPTY_STRING = " DEFAULT '' ";


    /**
     * DATABASE RELATED SECTION
     */
    // this is the first phase, phase name should be a global constant variable because we use it for dictionary object creation
    public static final String FIRST_PHASE_NAME = "ENCODING";


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
                return "OUT OF BOUNDS";
            case Global.NOT_AVAILABLE:
                return "NOT_AVAILABLE";
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
