package com.example.dietkuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AboutMe2 extends AppCompatActivity {

    TextView tv_bmi;
    TextView keterangan;
    TextView textView_bb;
    TextView tv_kg;

    Button next;

    String result;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    RulerValuePicker beratBadan;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me2);

        Intent intent = getIntent();
        String bmi = String.valueOf(intent.getFloatExtra(AboutMe.EXTRA_BMI, 0));
        String BMI = decimalFormat.format(Float.parseFloat(bmi));

        String berat = String.valueOf(intent.getIntExtra(AboutMe.EXTRA_BERAT, 0));

        tv_bmi = findViewById(R.id.bmi);
        tv_bmi.setText(BMI);

        float bmisederhana = Float.parseFloat(bmi);

        if (bmisederhana < 16) {
            result = "Berat badan sangat kurang!";
        }
        else if (bmisederhana <= 18.5) {
            result = "Berat badan kurang!";
        }
        else if (bmisederhana >= 18.5 && bmisederhana <= 24.9) {
            result = "Berat badan normal!";
        }
        else if (bmisederhana >= 25 && bmisederhana <= 29.9) {
            result = "Berat badan lebih!";
        }
        else {
            result = "Berat badan termasuk kategori obesitas!";
        }

        keterangan = findViewById(R.id.keteragan);
        keterangan.setText(result);

        textView_bb = findViewById(R.id.textView_w);
        tv_kg = findViewById(R.id.kg);
        beratBadan = findViewById(R.id.ruler_picker_w);
        beratBadan.setMinMaxValue(30, Integer.parseInt(berat));
        beratBadan.selectValue(Integer.parseInt(berat));
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

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        next = findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bbs = textView_bb.getText().toString().trim();
                String userID = fAuth.getCurrentUser().getUid();

                Map<String,Object> user = new HashMap<>();
                user.put("Berat badan sasaran", bbs);

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AboutMe2.this, "Data telah ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
                openCatatanharian();
            }
        });
    }

    private void openCatatanharian() {
        Intent intent = new Intent(this, CatatanHarian.class);
        startActivity(intent);
    }
}
