package model.dictionary.tools;

import java.util.Calendar;
import java.util.Date;

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


    private Date mNowFormattedDate;
    private String mNowStrFormattedDate;


    /**
     * Get today's formatted date
     * @return date formatted date
     */
    public Date getTime(){
        return mNowFormattedDate;
    }

    /**
     * Get today's formatted date as a string
     * @return string formatted date
     */
    public String getTimeAsString(){
        return mNowStrFormattedDate;
    }

    /**
     * Reset the calendar to the date defined in the cstor
     */
    private void initCal(Calendar cal){
        mNowStrFormattedDate = GeneralTools.getSQLDate(cal.getTime());
        mNowFormattedDate = GeneralTools.getDateFromSQLDate(mNowStrFormattedDate);
    }
}
