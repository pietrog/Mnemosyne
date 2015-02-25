package model.dictionary.tools;

import android.graphics.Bitmap;
import android.util.Log;

import com.mnemo.pietro.mnemosyne.BuildConfig;

/**
 * Created by pietro on 25/02/15.
 */
public class Logger {

    public static void v(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;

        Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        if (!BuildConfig.DEBUG)
            return;

        Log.v(tag, msg, t);
    }

    public static void d(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;

        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (!BuildConfig.DEBUG)
            return;

        Log.d(tag, msg, t);
    }

    public static void i(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;

        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (!BuildConfig.DEBUG)
            return;

        Log.i(tag, msg, t);
    }

    public static void w(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;

        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (!BuildConfig.DEBUG)
            return;

        Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;

        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (!BuildConfig.DEBUG)
            return;

        Log.e(tag, msg, t);
    }
}
