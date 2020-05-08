package com.example.dietkuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatatanHarian extends AppCompatActivity {

    TextView sasaran, konsumsi, sisa;
    RecyclerView sarapan_List, makansiang_List, makanmalam_List;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    List<ProductModel2> model2List = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    CustomAdapter2 adapter;

    List<ProductModel2> modelList2 = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager2;
    CustomAdapter2 adapter2;

    List<ProductModel2> modelList22 = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager3;
    CustomAdapter2 adapter3;

    CustomDialogList customDialogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_harian);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sasaran = findViewById(R.id.sasaranK);
        konsumsi = findViewById(R.id.makanan);
        sisa = findViewById(R.id.sisaK);

        sarapan_List = findViewById(R.id.sarapan_List);
        sarapan_List.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        sarapan_List.setLayoutManager(layoutManager);

        makansiang_List = findViewById(R.id.makansiang_List);
        makansiang_List.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        makansiang_List.setLayoutManager(layoutManager2);

        makanmalam_List = findViewById(R.id.makanmalam_List);
        makanmalam_List.setHasFixedSize(true);
        layoutManager3 = new LinearLayoutManager(this);
        makanmalam_List.setLayoutManager(layoutManager3);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        showData();

        customDialogList = new CustomDialogList(CatatanHarian.this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.catatan_harian);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.catatan_harian:
                        return true;
                    case R.id.penghitung_air:
                        startActivity(new Intent(getApplicationContext(), PenghitungAir.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cari:
                        startActivity(new Intent(getApplicationContext(), Cari.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(), Profil.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void showData() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sasaran.setText(documentSnapshot.getString("Kalori harian"));
                konsumsi.setText(documentSnapshot.getString("Konsumsi kalori"));
                sisa.setText(String.valueOf(Integer.parseInt(documentSnapshot.getString("Kalori harian")) - Integer.parseInt(documentSnapshot.getString("Konsumsi kalori"))));
            }
        });

        fStore.collection("users").document(userID).collection("sarapan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult()) {
                    ProductModel2 model = new ProductModel2 (doc.getString("Makanan"), doc.getString("Kalori"));
                    model2List.add(model);
                }
                adapter = new CustomAdapter2(CatatanHarian.this, model2List);
                sarapan_List.setAdapter(adapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CatatanHarian.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        fStore.collection("users").document(userID).collection("makansiang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult()) {
                    ProductModel2 model2 = new ProductModel2 (doc.getString("Makanan"), doc.getString("Kalori"));
                    modelList2.add(model2);
                }
                adapter2 = new CustomAdapter2(CatatanHarian.this, modelList2);
                makansiang_List.setAdapter(adapter2);
            }
        });

        fStore.collection("users").document(userID).collection("makanmalam").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult()) {
                    ProductModel2 model3 = new ProductModel2 (doc.getString("Makanan"), doc.getString("Kalori"));
                    modelList22.add(model3);
                }
                adapter3 = new CustomAdapter2(CatatanHarian.this, modelList22);
                makanmalam_List.setAdapter(adapter3);
            }
        });
    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Mereset data");
        alert.setMessage("Apakah anda yakin? Semua data yang telah anda masukkan di catatan harian hari ini akan dihapus!!");
        alert.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetDatacatatanHarian();
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    public void resetDatacatatanHarian() {
        customDialogList.startLoadingAnimation();

        FirebaseFirestore.getInstance().collection("users")
                .document(userID)
                .collection("sarapan")
                .whereEqualTo("Jenis", "sarapan")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                WriteBatch batch = FirebaseFirestore.getInstance().batch();

                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot: snapshotList) {
                    batch.delete(snapshot.getReference());
                }

                batch.commit();
                model2List.clear();
            }
        });

        FirebaseFirestore.getInstance().collection("users")
                .document(userID)
                .collection("makansiang")
                .whereEqualTo("Jenis", "makansiang")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList) {
                            batch.delete(snapshot.getReference());
                        }

                        batch.commit();
                        modelList2.clear();
                    }
                });

        FirebaseFirestore.getInstance().collection("users")
                .document(userID)
                .collection("makanmalam")
                .whereEqualTo("Jenis", "makanmalam")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList) {
                            batch.delete(snapshot.getReference());
                        }

                        batch.commit();
                        modelList22.clear();
                    }
                });

        Map<String,Object> user = new HashMap<>();
        user.put("Konsumsi kalori", "0");

        fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), CatatanHarian.class));
                customDialogList.dissmissDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                showAlertDialog();
                return true;
            case R.id.item1:
                startActivity(new Intent(getApplicationContext(), PenghitungAir.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(getApplicationContext(), Cari.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav_2, menu);
        return true;
    }
}
