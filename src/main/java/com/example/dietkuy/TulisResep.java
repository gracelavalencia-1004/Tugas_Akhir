package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TulisResep extends AppCompatActivity {

    EditText nama_ET, berat_ET, kalori_ET, desc_ET;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tulis_resep);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nama_ET = findViewById(R.id.namaMakanan);
        berat_ET = findViewById(R.id.beratMakanan);
        kalori_ET = findViewById(R.id.kaloriMakanan);
        desc_ET = findViewById(R.id.descMakanan);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        spinKitView = findViewById(R.id.Loading);
    }

    private void saveData() {
        spinKitView.setVisibility(View.VISIBLE);

        DocumentReference db = fStore.collection("recipes").document(userID).collection("resep makanan").document();

        String nama = nama_ET.getText().toString().trim();
        String berat = berat_ET.getText().toString().trim();
        String kalori = kalori_ET.getText().toString().trim();
        String desc = desc_ET.getText().toString().trim();
        String id = db.getId();

        if (nama.isEmpty() || berat.isEmpty() || kalori.isEmpty() || desc.isEmpty()) {
            spinKitView.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Data belum lengkap!!", Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String,Object> recipes = new HashMap<>();
            recipes.put("Makanan", nama);
            recipes.put("Berat", berat);
            recipes.put("Kalori", kalori);
            recipes.put("Deskripsi", desc);
            recipes.put("ID", id);

            db.set(recipes).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    spinKitView.setVisibility(View.INVISIBLE);

                    Toast.makeText(TulisResep.this, "Resep telah ditambahkan!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), TambahResep.class));
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            spinKitView.setVisibility(View.INVISIBLE);

                            Toast.makeText(TulisResep.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), TambahResep.class));
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_ok) {
            saveData();
        }
        return super.onOptionsItemSelected(item);
    }
}
