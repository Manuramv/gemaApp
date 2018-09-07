package gemalto.com.gemaltouser.util;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

import gemalto.com.gemaltouser.activities.MainActivity;

/**
 * Created by Manuramv on 9/7/2018.
 */

public class CustomWatcher implements TextWatcher {

    private boolean mWasEdited = false;
    private EditText et1,et2;
    private Spinner sp;
    private String editedOptions;

    public CustomWatcher(EditText et1, EditText et2, Spinner sp, String editedText) {
        this.et1 = et1;
        this.et2 = et2;
        this.sp = sp;
        this.editedOptions = editedText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() > 0){
            et2.setText("");
            sp.setSelection(0);
            sp.setEnabled(false);
            if(editedOptions.equalsIgnoreCase("editingUserIdField")){
                GemAppConstants.isUserIDSelected = true;
                GemAppConstants.isMultiUserSelected = false;
                GemAppConstants.isGenderSelected = false;
            } else {
                GemAppConstants.isMultiUserSelected = true;
                GemAppConstants.isUserIDSelected = false;
                GemAppConstants.isGenderSelected = false;
            }
        } else {
            sp.setEnabled(true);
            sp.setSelection(0);
        }
    }
}