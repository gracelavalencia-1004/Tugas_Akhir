package com.example.dietkuy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AboutMe extends AppCompatActivity {

    public static final String TAG = "TAG";
    RulerValuePicker tinggiBadan;
    RulerValuePicker beratBadan;

    TextView textView_tb;
    TextView textView_bb;
    TextView tv_kg;
    TextView tv_cm;

    Button next;
    String userID;
    RadioGroup radioGroup;
    TextView jeniskelamin;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    RadioButton radioButton;

    public static final String EXTRA_BMI = "com.example.dietkuy.EXTRA_BMI";
    public static final String EXTRA_BERAT = "com.example.dietkuy.EXTRA_BERAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        radioGroup = findViewById(R.id.radio);
        jeniskelamin = findViewById(R.id.jeniskelamin);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                jeniskelamin.setText(radioButton.getText());
            }
        });

        textView_bb = findViewById(R.id.textView_w);
        tv_kg = findViewById(R.id.kg);
        beratBadan = findViewById(R.id.ruler_picker_w);
        beratBadan.selectValue(50);
        beratBadan.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                String kg = "kg";
                textView_bb.setText(String.valueOf(selectedValue));
                tv_kg.setText(kg);
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        textView_tb = findViewById(R.id.textView_h);
        tv_cm = findViewById(R.id.cm);
        tinggiBadan = findViewById(R.id.ruler_picker_h);
        tinggiBadan.selectValue(150);
        tinggiBadan.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                String cm = "cm";
                textView_tb.setText(String.valueOf(selectedValue));
                tv_cm.setText(cm);
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        next = findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tinggi = textView_tb.getText().toString().trim();
                String berat = textView_bb.getText().toString().trim();
                String gender = jeniskelamin.getText().toString().trim();
                String userID = fAuth.getCurrentUser().getUid();

                Map<String,Object> user = new HashMap<>();
                user.put("Tinggi badan", tinggi);
                user.put("Berat badan", berat);
                user.put("Jenis kelamin", gender);
                user.put("Kalori harian", "1500");
                user.put("Konsumsi kalori", "0");

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AboutMe.this, "Data telah ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
                openAboutMe2();
            }
        });
    }

    private void openAboutMe2() {
        float tinggi = Float.parseFloat(textView_tb.getText().toString()) / 100;
        int berat = Integer.parseInt(textView_bb.getText().toString());
        float bmi = berat / (tinggi * tinggi);

        Intent intent = new Intent(this, AboutMe2.class);
        intent.putExtra(EXTRA_BMI, bmi);
        intent.putExtra(EXTRA_BERAT, berat);
        startActivity(intent);
    }
}
