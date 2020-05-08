package com.example.dietkuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.HashMap;
import java.util.Map;

public class FoodList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView totalKalori, totalKaloriDB;

    Spinner spinner;
    TextView textViewM, textViewK, textViewB;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    LinearLayout linearLayout;

    int data;

    Button sarapan, makansiang, makanmalam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String makanan = intent.getStringExtra("NamaMakanan");
        final String kalori = intent.getStringExtra("JumlahKalori");
        final String berat = intent.getStringExtra("BeratMakanan");

        textViewM = findViewById(R.id.makanan);
        textViewK = findViewById(R.id.Kalori);
        textViewB = findViewById(R.id.berat);

        textViewM.setText(makanan);
        textViewK.setText(kalori);
        textViewB.setText(berat);

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

        final CustomDIalogFood customDIalogFood = new CustomDIalogFood(FoodList.this);

        showData();

        createNotificationChannel1();
        createNotificationChannel2();
        createNotificationChannel3();

        sarapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                setAlarmSarapan();

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
                        startActivity(new Intent(getApplicationContext(), Cari.class));
                    }
                });
            }
        });

        makansiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                setAlarmMakanSiang();

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
                        startActivity(new Intent(getApplicationContext(), Cari.class));
                    }
                });
            }
        });

        makanmalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDIalogFood.startLoadingDialog();

                setAlarmMakanMalam();

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
                        startActivity(new Intent(getApplicationContext(), Cari.class));
                    }
                });
            }
        });
    }

    private void setAlarmSarapan() {
        Toast.makeText(this, "Set alarm sarapan", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FoodList.this, AlarmManager2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(FoodList.this, 0, intent, 0);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long after10sec = 1000 * 5;

        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,
                currentTime + after10sec,
                pendingIntent);
    }

    private void createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Diet kuy!!";
            String description = "Anda belum menambahkan data pada sarapan hari ini, sudahkah anda sarapan?";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDietKuy2", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarmMakanSiang() {
        Toast.makeText(this, "Set alarm makan siang", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FoodList.this, AlarmManager3.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(FoodList.this, 0, intent, 0);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long after10sec = 1000 * 5;

        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,
                currentTime + after10sec,
                pendingIntent);
    }

    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Diet kuy!!";
            String description = "Anda belum menambahkan data pada makan siang hari ini, sudahkah anda makan siang?";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDietKuy3", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarmMakanMalam() {
        Toast.makeText(this, "Set alarm makan malam", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FoodList.this, AlarmManager4.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(FoodList.this, 0, intent, 0);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long after10sec = 1000 * 5;

        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,
                currentTime + after10sec,
                pendingIntent);
    }

    private void createNotificationChannel3() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Diet kuy!!";
            String description = "Anda belum menambahkan data pada makan malam hari ini, sudahkah anda makan malam?";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDietKuy4", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int total = Integer.parseInt((String) parent.getSelectedItem()) * Integer.parseInt(textViewK.getText().toString().trim());
        totalKalori.setText(String.valueOf(total));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showData() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalKaloriDB.setText(documentSnapshot.getString("Konsumsi kalori"));

                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
