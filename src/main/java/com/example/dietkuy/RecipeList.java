package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RecipeList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView totalKalori, totalKaloriDB, textViewDesc;

    Spinner spinner;
    TextView textViewM, textViewK, textViewB;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    LinearLayout linearLayout;

    int data;

    String id, makanan, kalori, berat;

    Button sarapan, makansiang, makanmalam;

    CustomDialogList customDialogList;

    public static final String EXTRA_ID = "com.example.dietkuy.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("DocID");
        makanan = intent.getStringExtra("NamaMakanan");
        kalori = intent.getStringExtra("JumlahKalori");
        berat = intent.getStringExtra("BeratMakanan");

        textViewM = findViewById(R.id.makanan);
        textViewK = findViewById(R.id.Kalori);
        textViewB = findViewById(R.id.berat);

        textViewM.setText(makanan);
        textViewK.setText(kalori);
        textViewB.setText(berat);
        textViewDesc = findViewById(R.id.descMakanan);

        linearLayout = findViewById(R.id.buttoon);
        totalKaloriDB = findViewById(R.id.totalKaloriDB);
        totalKalori = findViewById(R.id.totalKalori);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        sarapan = findViewById(R.id.sarapan);
        makansiang = findViewById(R.id.makansiang);
        makanmalam = findViewById(R.id.makanmalam);

        customDialogList = new CustomDialogList(RecipeList.this);

        final CustomDIalogFood customDIalogFood = new CustomDIalogFood(RecipeList.this);

        showData();

        sarapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                String Makanan = textViewM.getText().toString().trim();
                final String Kalori = totalKalori.getText().toString().trim();

                Map<String,Object> food = new HashMap<>();
                food.put("Makanan", Makanan);
                food.put("Kalori", Kalori);
                food.put("Jenis", "sarapan");

                fStore.collection("users").document(userID).collection("sarapan").document().set(food, SetOptions.merge());

                data = Integer.parseInt(totalKaloriDB.getText().toString().trim()) + Integer.parseInt(totalKalori.getText().toString().trim());

                Map<String,Object> user = new HashMap<>();
                user.put("Konsumsi kalori", String.valueOf(data));

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customDIalogFood.dissmissDialog();
                        startActivity(new Intent(getApplicationContext(), TambahResep.class));
                    }
                });
            }
        });

        makansiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                String Makanan = textViewM.getText().toString().trim();
                final String Kalori = totalKalori.getText().toString().trim();

                Map<String,Object> food = new HashMap<>();
                food.put("Makanan", Makanan);
                food.put("Kalori", Kalori);
                food.put("Jenis", "makansiang");

                fStore.collection("users").document(userID).collection("makansiang").document().set(food, SetOptions.merge());

                data = Integer.parseInt(totalKaloriDB.getText().toString().trim()) + Integer.parseInt(totalKalori.getText().toString().trim());

                Map<String,Object> user = new HashMap<>();
                user.put("Konsumsi kalori", String.valueOf(data));

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customDIalogFood.dissmissDialog();
                        startActivity(new Intent(getApplicationContext(), TambahResep.class));
                    }
                });
            }
        });

        makanmalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                String Makanan = textViewM.getText().toString().trim();
                final String Kalori = totalKalori.getText().toString().trim();

                Map<String,Object> food = new HashMap<>();
                food.put("Makanan", Makanan);
                food.put("Kalori", Kalori);
                food.put("Jenis", "makanmalam");

                fStore.collection("users").document(userID).collection("makanmalam").document().set(food, SetOptions.merge());

                data = Integer.parseInt(totalKaloriDB.getText().toString().trim()) + Integer.parseInt(totalKalori.getText().toString().trim());

                Map<String,Object> user = new HashMap<>();
                user.put("Konsumsi kalori", String.valueOf(data));

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customDIalogFood.dissmissDialog();
                        startActivity(new Intent(getApplicationContext(), TambahResep.class));
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int total = Integer.parseInt((String) parent.getSelectedItem()) * Integer.parseInt(textViewK.getText().toString().trim());
        totalKalori.setText(String.valueOf(total));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                editResep();
                return true;
            case R.id.item2:
                deleteData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editResep() {
        Intent intent = new Intent(this, EditResep.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    private void deleteData() {
        customDialogList.startLoadingAnimation();

        fStore.collection("recipes").document(userID).collection("resep makanan").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customDialogList.dissmissDialog();
                        startActivity(new Intent(getApplicationContext(), TambahResep.class));
                    }
                });
    }

    private void showData() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalKaloriDB.setText(documentSnapshot.getString("Konsumsi kalori"));
            }
        });

        DocumentReference reference = fStore.collection("recipes").document(userID).collection("resep makanan").document(id);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewDesc.setText(documentSnapshot.getString("Deskripsi"));

                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
