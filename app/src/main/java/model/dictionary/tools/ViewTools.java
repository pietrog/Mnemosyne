package model.dictionary.tools;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import model.dictionary.Global;

/**
 * Created by pietro on 09/02/15.
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

}
