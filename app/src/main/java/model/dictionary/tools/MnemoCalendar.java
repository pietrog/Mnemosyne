package model.dictionary.tools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pietro on 01/07/15.
 *
 * Handle calendar dates
 */
public class MnemoCalendar {

    private static MnemoCalendar instance;

    public static synchronized MnemoCalendar getInstance(){
        if (instance == null){
            instance = new MnemoCalendar();
        }
        return instance;
    }


    private Calendar mCalendar;
    private Date mNowFormattedDate;
    private String mNowStrFormattedDate;

    private MnemoCalendar(){
        mCalendar = Calendar.getInstance();
        mNowStrFormattedDate = GeneralTools.getSQLDate(mCalendar.getTime());
        mNowFormattedDate = GeneralTools.getDateFromSQLDate(mNowStrFormattedDate);
    }

    protected MnemoCalendar(int delayInDays){
        mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_YEAR, delayInDays);
        mNowStrFormattedDate = GeneralTools.getSQLDate(mCalendar.getTime());
        mNowFormattedDate = GeneralTools.getDateFromSQLDate(mNowStrFormattedDate);
    }

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

}
