package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView buttonLogIn;
    Button buttonSignUp;
    SpinKitView spinKitView;

    TextInputLayout inputUsername;
    TextInputLayout inputPassword;
    TextInputLayout inputEmail;
    TextView tanggalLahir;
    TextView tL;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;
    String mbirthday, mtoday;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonLogIn = findViewById(R.id.button_login);
        buttonLogIn.setPaintFlags(buttonLogIn.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogIn();
            }
        });

        buttonSignUp = findViewById(R.id.button_signup);
        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        inputEmail = findViewById(R.id.input_email);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        spinKitView = findViewById(R.id.spinkit);

        tL = findViewById(R.id.tL);
        tanggalLahir = findViewById(R.id.tanggalLahir);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mtoday = simpleDateFormat.format(Calendar.getInstance().getTime());

        tL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                mbirthday = dayOfMonth + "/" + month + "/" + year;
                tanggalLahir.setText(mbirthday);
            }
        };

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validateEmail() | !validatePassword()) {
                    return;
                }
                else {
                    String email = inputEmail.getEditText().getText().toString().trim();
                    String password = inputPassword.getEditText().getText().toString().trim();
                    spinKitView.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String email = inputEmail.getEditText().getText().toString().trim();
                                String username = inputUsername.getEditText().getText().toString().trim();
                                String date = tanggalLahir.getText().toString().trim();

                                Toast.makeText(SignUp.this, "User created!", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("Nama", username);
                                user.put("Email", email);
                                user.put("Tanggal lahir", date);
                                user.put("Persentase air", "0");
                                user.put("Konsumsi air", "0");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user profile is created for" + userID);
                                    }
                                });
                                openAboutMe();
                            }
                            else {
                                Toast.makeText(SignUp.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                spinKitView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void openLogIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openAboutMe() {
        Intent intent = new Intent(this, AboutMe.class);
        startActivity(intent);
    }

    private boolean validateUsername() {
        String username = inputUsername.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            inputUsername.setError("Username belum diisi!");
            return false;
        }
        if (username.length() > 15) {
            inputUsername.setError("Username terdiri dari 6-15 karakter!");
            return false;
        }
        if (username.length() < 6) {
            inputUsername.setError("Username terdiri dari 6-15 karakter!");
            return false;
        }
        else {
            inputUsername.setError(null);
            return true;
        }
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
}
