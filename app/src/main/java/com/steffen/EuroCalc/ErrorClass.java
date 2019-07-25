package com.steffen.EuroCalc;

import android.content.Context;
import android.widget.Toast;

public class ErrorClass {
    private Context context;

    public void setContext (Context context) {
        this.context = context;
    }

    public void errorToast(String message, boolean long_text) {
        if (!long_text)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        if (long_text)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
