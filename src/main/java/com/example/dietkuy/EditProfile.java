package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText Username;
    TextView TanggalLahir, TL;
    String mbirthday, mtoday;
    DatePickerDialog.OnDateSetListener dateSetListener;

    RulerValuePicker BeratBadan;
    TextView BeratBadanTV;

    RulerValuePicker TinggiBadan;
    TextView TinggiBadanTV;

    RulerValuePicker BeratBadanSasaran;
    TextView BeratBadanSasaranTV;

    RadioGroup JenisKelamin;
    RadioButton radioButton;
    TextView JenisKelaminTV;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Username = findViewById(R.id.Username);
        TanggalLahir = findViewById(R.id.tanggalLahir);
        TL = findViewById(R.id.TL);

        TL.setPaintFlags(TL.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mtoday = simpleDateFormat.format(Calendar.getInstance().getTime());

        TL.setOnClickListener(new View.OnClickListener() {
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
                TanggalLahir.setText(mbirthday);
            }
        };

        BeratBadanTV = findViewById(R.id.textView_w);
        BeratBadan = findViewById(R.id.ruler_picker_w);
        BeratBadan.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                BeratBadanTV.setText(String.valueOf(selectedValue));
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        TinggiBadanTV = findViewById(R.id.textView_h);
        TinggiBadan = findViewById(R.id.ruler_picker_h);
        TinggiBadan.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                TinggiBadanTV.setText(String.valueOf(selectedValue));
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        BeratBadanSasaranTV = findViewById(R.id.textView_s);
        BeratBadanSasaran = findViewById(R.id.ruler_picker_s);
        BeratBadanSasaran.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                BeratBadanSasaranTV.setText(String.valueOf(selectedValue));
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        JenisKelaminTV = findViewById(R.id.jeniskelamin);
        JenisKelamin = findViewById(R.id.radio);
        JenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                JenisKelaminTV.setText(radioButton.getText());
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        showData();
    }

    public void showData() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Username.setText(documentSnapshot.getString("Nama"));
                TanggalLahir.setText(documentSnapshot.getString("Tanggal lahir"));
                JenisKelaminTV.setText(documentSnapshot.getString("Jenis kelamin"));
                TinggiBadanTV.setText(documentSnapshot.getString("Tinggi badan"));
                BeratBadanTV.setText(documentSnapshot.getString("Berat badan"));
                BeratBadanSasaranTV.setText(documentSnapshot.getString("Berat badan sasaran"));

                BeratBadan.selectValue(Integer.parseInt(BeratBadanTV.getText().toString().trim()));
                TinggiBadan.selectValue(Integer.parseInt(TinggiBadanTV.getText().toString().trim()));
                BeratBadanSasaran.selectValue(Integer.parseInt(BeratBadanSasaranTV.getText().toString().trim()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_ok) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit profil");
        alert.setMessage("Semua data yang sudah ada akan diubah");
        alert.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    private void saveData() {
        String username = Username.getText().toString().trim();
        String tanggallahir = TanggalLahir.getText().toString().trim();
        String beratbadan = BeratBadanTV.getText().toString().trim();
        String beratbadansasaran = BeratBadanSasaranTV.getText().toString().trim();
        String tinggibadan = TinggiBadanTV.getText().toString().trim();
        String jeniskelamin = JenisKelaminTV.getText().toString().trim();

        Map<String,Object> user = new HashMap<>();
        user.put("Nama", username);
        user.put("Tanggal lahir", tanggallahir);
        user.put("Berat badan", beratbadan);
        user.put("Berat badan sasaran", beratbadansasaran);
        user.put("Tinggi badan", tinggibadan);
        user.put("Jenis kelamin", jeniskelamin);

        fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), Profil.class));
            }
        });
    }
}
