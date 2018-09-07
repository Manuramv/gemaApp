package gemalto.com.gemaltouser.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Manuramv on 9/7/2018.
 */

public class CustomWatcher implements TextWatcher {

    private boolean mWasEdited = false;
    private EditText et1,et2;
    private Spinner sp;

    public CustomWatcher(EditText et1, EditText et2, Spinner sp) {
        this.et1 = et1;
        this.et2 = et2;
        this.sp = sp;
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
        } else {
            sp.setEnabled(true);
            sp.setSelection(0);
        }
    }
}