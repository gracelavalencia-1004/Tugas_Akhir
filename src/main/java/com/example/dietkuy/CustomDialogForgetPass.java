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
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class CustomDialogForgetPass extends DialogFragment {

    private TextInputLayout Email;
    private CustomDialogForgetPassListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_forget_pass, null);

        builder.setView(view)
                .setTitle("Reset Password")
                .setMessage("Masukkan email anda untuk mendapat email dan mengubah password anda")
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = Email.getEditText().getText().toString().trim();
                        listener.applyTexts(email);
                    }
                });

        Email = view.findViewById(R.id.input_email);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CustomDialogForgetPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Harus menambah CustomDialogForgetPassListener");
        }
    }

    public interface CustomDialogForgetPassListener {
        void applyTexts(String email);
    }
}
