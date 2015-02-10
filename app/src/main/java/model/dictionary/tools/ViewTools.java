package model.dictionary.tools;

import android.view.View;
import android.widget.EditText;

/**
 * Created by pietro on 09/02/15.
 */
public class ViewTools {

    /**
     * Just get the string from editable text
     * @param view
     * @return
     */
    public static String getStringFromEditableText(View view){
        EditText editableObject = (EditText) view;
        return editableObject.getText().toString();
    }
}
