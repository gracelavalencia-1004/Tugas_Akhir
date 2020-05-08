package com.example.dietkuy;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class CustomDialogWater {
    private Activity activity;
    private AlertDialog alertDialog;

    CustomDialogWater (Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_water, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dissmissDialog() {
        alertDialog.dismiss();
    }
}
