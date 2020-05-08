package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements CustomDialogForgetPass.CustomDialogForgetPassListener {

    TextView buttonSignUp;
    Button buttonLogIn;
    TextInputLayout inputEmail;
    TextInputLayout inputPassword;
    FirebaseAuth fAuth;
    SpinKitView spinKitView;
    TextView forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignUp = findViewById(R.id.button_signup);
        buttonSignUp.setPaintFlags(buttonSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        spinKitView = findViewById(R.id.spinkit);

        fAuth = FirebaseAuth.getInstance();
        buttonLogIn = findViewById(R.id.button_login);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getEditText().getText().toString().trim();
                String password = inputPassword.getEditText().getText().toString().trim();

                if (!validateEmail() | !validatePassword()) {
                    return;
                }
                else {
                    spinKitView.setVisibility(View.VISIBLE);

                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), CatatanHarian.class));
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                spinKitView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        forgetPass = findViewById(R.id.Forget_Password);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomDialogForgetPass();
            }
        });
    }

    private void openCustomDialogForgetPass() {
        CustomDialogForgetPass customDialogForgetPass = new CustomDialogForgetPass();
        customDialogForgetPass.show(getSupportFragmentManager(), "example dialog");
    }

    private void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    private boolean validateEmail() {
        String email = inputEmail.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError("Email belum diisi!");
            return false;
        }
        else {
            inputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = inputPassword.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            inputPassword.setError("Password belum diisi!");
            return false;
        }
        if (password.length() < 6) {
            inputPassword.setError("Password minimal terdiri dari 6 karakter!");
            return false;
        }
        else {
            inputPassword.setError(null);
            return true;
        }
    }

    @Override
    public void applyTexts(String email) {
        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Email untuk mengubah password telah terkirim", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser fUser = fAuth.getCurrentUser();
        if (fUser != null) {
            startActivity(new Intent(getApplicationContext(), CatatanHarian.class));
            finish();
        }
    }
}
