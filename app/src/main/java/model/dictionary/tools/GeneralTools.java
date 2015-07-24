package model.dictionary.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import model.dictionary.Global;


/**
 * Created by pietro on 09/02/15.
 *
 * Contains some general usefull fonctions
 */
public class GeneralTools {


    /**
     * Return a JSON object built from a json file
     * @param dir directory where the json file is
     * @param fileName name of the source json file
     * @return json object resulting of the fileName json file
     */
    public static JSONObject getJSONObjectFromFile(String dir, String fileName){
        File file = new File(dir, fileName);
        if (!file.exists())
            return null;

        BufferedReader in;
        String line;
        StringBuilder str_build = new StringBuilder();
        JSONObject res = null;

        try{
            in = new BufferedReader(new FileReader(file));
            while((line = in.readLine()) != null)
                str_build.append(line);

            res = new JSONObject(str_build.toString());
        }
        catch (FileNotFoundException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "File not found ", e);
        }
        catch(IOException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "IOException thrown ", e);
        }
        catch(JSONException e){
            Logger.d("GeneralTools::getJSONObjectFromFile", "JSON exception thrown ", e);
        }

        return res;
    }

    /**
     * Write the json object into a file named filename
     * @param toWrite json object to write
     * @param filename name of the output file
     * @param context context for the path
     * @return SUCCESS ot FAILURE
     */
    public static int writeFile (JSONObject toWrite, String filename, Context context){

        try {
            FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);

            stream.write(toWrite.toString().getBytes());
            stream.flush();
            stream.close();
            Logger.v("GeneralTools::writeFile","File " + filename + " written");
        }
        catch (FileNotFoundException e){
            Logger.d("GeneralTools::writeFile", "File not found during writing", e);
            return Global.FAILURE;
        }
        catch (IOException e){
            Logger.d("GeneralTools::writeFile", "IOException thrown", e);
            return Global.FAILURE;
        }

        return Global.SUCCESS;
    }

    /**
     * Delete a file
     * @param filename file name to delete
     * @return {Global.SUCCESS} is successful, {Global.FAILURE} otherwise
     */
    /*public static int deleteFile(String filename){
        File file = new File(filename);
        return file.delete() ? Global.SUCCESS : Global.FAILURE;
    }*/

    /**
     * Return a string containing a formatted date (template is {Global.FORMAT_SQL_DATE})
     * @param date date to stringify
     * @return string date
     */
    public static String getSQLDate(Date date){
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(Global.FORMAT_SQL_DATE, Locale.FRANCE);
        return sdf.format(date);
    }

    /**
     * Convert string sqldate to date
     * @param sqlDate string to convert into date
     * @return date object, null if fails
     */
    public static Date getDateFromSQLDate(String sqlDate){

        if (sqlDate.isEmpty())
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(Global.FORMAT_SQL_DATE, Locale.FRANCE);

        Date date;
        try {
            date = sdf.parse(sqlDate);
        } catch (ParseException e) {
            Logger.e("GeneralTools::getDateFromSQLDate"," error occured while parsing date : " + sqlDate);
            return null;
        }

        return date;
    }

    public static Date getFormattedDate(Date date){
        return getDateFromSQLDate(getSQLDate(date));
    }

    /**
     * Return a string list from vector vect
     * @param vect vector to put in string list
     * @return string
     */
    public static String getStringFrom(Vector<?> vect){
        String res = "";
        for(Object curr : vect)
            res += curr.toString() + ",";

        return res.substring(0, res.length() - 1);
    }

    public static Vector<Integer> getVectorFromString(String str){
        Vector<Integer> res = new Vector<>();
        String[] tab = str.split(",");
        for(String current: tab){
            res.add(Integer.parseInt(current));
        }
        return res;
    }


    /**
     * SQL
     */
    public static String getStringElement(Cursor cursor, String column){
        Logger.i("GeneralTools::getStringElement", " get string from " + column + " ; cursor index : " + cursor.getColumnIndex(column)) ;
        if (cursor.isNull(cursor.getColumnIndex(column)))
            return "";
        return cursor.getString(cursor.getColumnIndex(column));
    }

    /**
     * Return long value exctracted from column of cursor
     * @param cursor cursor containing the value
     * @param column column targeted
     * @return long if exists, -1 otherwise
     */
    public static long getLongElement(Cursor cursor, String column){
        if (cursor.isNull(cursor.getColumnIndex(column)))
            return -1;
        return cursor.getLong(cursor.getColumnIndex(column));
    }

    public static Date getNowDate(){
        return MnemoCalendar.getInstance().getTime();
    }


    /**
     * Return a color corresponding to a delay. If there is no delay, color is white. More important is the delay, more red is the returned color
     * @param delay number of days
     * @return color for background
     */
    public static int getColorFromDelay(int delay){
        switch (delay){
            case 0:
                return Color.WHITE;
            case 1:
                return Color.parseColor("#FFD7D7");
            case 2:
                return Color.parseColor("#FFBBBB");
            case 3:
                return Color.parseColor("#FFA6A6");
            case 4:
                return Color.parseColor("#FF8383");
            case 5:
                return Color.parseColor("#FF6E6E");
            default:
                return Color.parseColor("#FF0000");
        }
    }
}
