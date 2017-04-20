package com.youroff.listus.validators;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.List;

public class TextUniqueValidator implements TextWatcher {
    private final TextView textView;
    private List<String> titles;

    public TextUniqueValidator(TextView textView, List<String> titles) {
        this.textView = textView;
        this.titles = titles;
    }

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString().toLowerCase().trim();
        if (titles.indexOf(text) != -1)
            textView.setError("already taken");
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}
