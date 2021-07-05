package com.mattg.viewmodelexample.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

/**
 * Instantiate this class and attach to any currency value edit texts
 */
public class CurrencyTextWatcher implements TextWatcher {

    private EditText thingWeAreWatching;
    private final WeakReference<EditText> editTextWeakReference;

    public CurrencyTextWatcher(EditText et){
        editTextWeakReference = new WeakReference<EditText>(et);

    }


    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        EditText editText = editTextWeakReference.get();
        thingWeAreWatching = editText;
        if (editText == null || editText.getText().toString().equals("")) {
            return;
        }
        editText.removeTextChangedListener(this);
        String userInput = "" + s.toString().replaceAll("[^\\d]", "");
        if (userInput.length() > 0) {
            float in = Float.parseFloat(userInput);
            float percent = in / 100;
            editText.setText("$" + df.format(percent));

            editText.setSelection(thingWeAreWatching.getText().length());

            editText.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
