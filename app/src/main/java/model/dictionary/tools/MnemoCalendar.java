package model.dictionary.tools;

import java.util.Calendar;

/**
 * Created by pietro on 01/07/15.
 *
 * Handle calendar dates
 */
public class MnemoCalendar{

    private static int     mDelay = 0;
    private static int     mTypeOfDelay = 0;

    public static void init(int type, int delay){
        mDelay = delay;
        mTypeOfDelay = type;
    }

    public  static void reset(){
        mDelay = 0;
        mTypeOfDelay = 0;
    }

    public static synchronized Calendar getInstance(){
        Calendar cal = Calendar.getInstance();
        switch (mTypeOfDelay){
            case Calendar.DAY_OF_WEEK:
                cal.add(Calendar.DAY_OF_WEEK, mDelay);
                break;
            case Calendar.MONTH:
                cal.add(Calendar.MONTH, mDelay);
                break;
            case Calendar.DAY_OF_YEAR:
                cal.add(Calendar.DAY_OF_YEAR, mDelay);
                break;
            case Calendar.YEAR:
                cal.add(Calendar.YEAR, mDelay);
                break;
            default:
                //do nothing
                Logger.w("MnemoCalendar::getInstance", " Type of delay does not match anything. Given calendar is set to local time");
                break;
        }
        return cal;
    }
}
