package com.youroff.listus.validators;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.regex.Pattern;

public class NumberValidator implements TextWatcher {
    private final TextView textView;
    private final String pattern = "\\d*(\\.\\d{0,2})?";
    public NumberValidator(TextView textView) {
        this.textView = textView;
    }

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        if (!text.matches(pattern))
            textView.setError("doesn't look like a number");
        if (text.length() == 0)
            textView.setError("must be filled in");
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}
