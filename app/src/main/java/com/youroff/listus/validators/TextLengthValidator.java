package com.youroff.listus.validators;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextLengthValidator implements TextWatcher {
    private final TextView textView;
    private final Integer min;
    private final Integer max;

    public TextLengthValidator(TextView textView, Integer min, Integer max) {
        this.textView = textView;
        this.min = min;
        this.max = max;
    }

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        if (min != null && text.length() < min)
            addError("can't be less than " + min + " characters");
        if (max != null && text.length() > max)
            addError("can't be more than " + max + " characters");
    }

    private void addError(String e) {
        if (textView.getError() == null) {
            textView.setError(e);
        } else {
            textView.setError(textView.getError() + ", " + e);
        }
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}
