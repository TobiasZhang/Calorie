package cn.ft.calorie.listener;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by TT on 2017/1/17.
 */
public class OnPasswordEyeListenerImpl implements CompoundButton.OnCheckedChangeListener {
    private EditText editText;
    public OnPasswordEyeListenerImpl(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.length());
    }
}
