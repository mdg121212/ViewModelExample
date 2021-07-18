package com.mattg.viewmodelexample.utils;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mattg.viewmodelexample.databinding.DialogErrorBinding;

/**
 * Class to provide an application context available dialog to handle display of
 * error messages.
 */
public class ErrorDialog {
    private final Activity activity;
    private final AppError error;
    private DialogErrorBinding binding;

    public ErrorDialog(Activity activity, AppError error) {
        this.activity = activity;
        this.error = error;

    }

    //displays error dialog in application context
    public boolean showErrorDialog(String messageString) {
        Button btnOk;
        Button btnBack;
        TextView title;
        TextView message;

        if (this.error != null && this.activity != null) {
            Dialog errorDialog = new Dialog(this.activity);
            binding = DialogErrorBinding.inflate(this.activity.getLayoutInflater());
            errorDialog.setContentView(binding.getRoot());
            btnOk = binding.btnOkError;
            btnBack = binding.btnBackError;
            message = binding.tvMessage;
            title = binding.tvTitle;
            //set values then display
            if (messageString.isEmpty()) {
                message.setText("");
            } else {
                message.setText(messageString);
            }
            title.setText(this.error.getLabel());
            btnOk.setOnClickListener(view -> {
                Toast.makeText(activity.getBaseContext(), "Clicked Ok", Toast.LENGTH_SHORT).show();
                errorDialog.dismiss();
            });
            btnBack.setOnClickListener(view -> {
                Toast.makeText(activity.getApplicationContext(), "Clicked back", Toast.LENGTH_SHORT).show();
                errorDialog.dismiss();
            });
            errorDialog.show();
            return true;
        } else {
            return false;
        }
    }
}
