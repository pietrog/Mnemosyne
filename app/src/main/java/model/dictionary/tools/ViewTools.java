package model.dictionary.tools;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pietro on 09/02/15.
 * Class providing some useful UI functions
 */
public class ViewTools {

    /**
     * Just get the string from editable text
     * @param view view to cast in editable text
     * @return text if exist
     */
    public static String getStringFromEditableText(View view){
        EditText editableObject = (EditText) view;
        return editableObject == null ? null : editableObject.getText().toString();
    }

    public static void setStringOfTextView(TextView view, String text){
        view.setText(text);
    }

    public static void hideKeyboard(View view, Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * ACTION BAR / TOOLBAR
     */
    public static void setTitle(Activity activity, String title){
        android.support.v7.app.ActionBar toolbar = ((ActionBarActivity)activity).getSupportActionBar();

        if (toolbar != null)
        toolbar.setTitle(title);
    }
    public static void setTitle(Activity activity, int title){
        android.support.v7.app.ActionBar toolbar = ((ActionBarActivity)activity).getSupportActionBar();

        if (toolbar != null)
            toolbar.setTitle(title);
    }
    public static void setSubtitle(Activity activity, String title){
        android.support.v7.app.ActionBar toolbar = ((ActionBarActivity)activity).getSupportActionBar();

        if (toolbar != null)
            toolbar.setSubtitle(title);
    }
    public static void setSubtitle(Activity activity, int title){
        android.support.v7.app.ActionBar toolbar = ((ActionBarActivity)activity).getSupportActionBar();

        if (toolbar != null)
            toolbar.setSubtitle(title);
    }



}
