package com.example.todolisttask2;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean validateTaskCreateInput(
            EditText tbxTaskName,
            EditText tbxDuration) {
        String nameValue = tbxTaskName.getText().toString().trim();
        String durationValue = tbxDuration.getText().toString().trim();
        if (TextUtils.isEmpty(nameValue)) {
            tbxTaskName.setError("Field cannot be empty");
            tbxTaskName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(durationValue)) {
            tbxDuration.setError("Field cannot be empty");
            tbxDuration.requestFocus();
            return false;
        }
        return true;
    }
}
