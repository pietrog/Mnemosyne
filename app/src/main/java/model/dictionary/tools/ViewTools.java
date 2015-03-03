package model.dictionary.tools;

import android.view.View;
import android.widget.EditText;

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

}
