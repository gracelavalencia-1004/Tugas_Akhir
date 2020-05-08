package com.example.dietkuy;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class CustomDialogList {

    private Activity activity;
    private AlertDialog alertDialog;

    CustomDialogList (Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_list, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dissmissDialog() {
        alertDialog.dismiss();
    }
}
