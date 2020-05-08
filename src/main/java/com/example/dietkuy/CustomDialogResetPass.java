package com.example.dietkuy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class CustomDialogResetPass extends AppCompatDialogFragment {

    private TextInputLayout inputPassword;
    private CustomDialogResetPassListener resetPassListener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_reset_pass, null);

        inputPassword = view.findViewById(R.id.input_password);

        builder.setView(view)
                .setTitle("Ganti Password")
                .setMessage("Password minimal 6 karakter!!")
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ganti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = inputPassword.getEditText().getText().toString().trim();
                        resetPassListener.applyTexts(password);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            resetPassListener = (CustomDialogResetPassListener) context;
        } catch (Exception e) {
            throw  new ClassCastException(context.toString() + "Harus implement CustomDialogResetPassListener");
        }
    }

    public interface CustomDialogResetPassListener {
        void applyTexts(String password);
    }
}
