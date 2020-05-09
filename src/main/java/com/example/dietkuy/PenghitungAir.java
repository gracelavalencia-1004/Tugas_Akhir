package com.example.dietkuy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.muddzdev.styleabletoast.StyleableToast;
import com.victor.loading.rotate.RotateLoading;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.HashMap;
import java.util.Map;

import me.itangqi.waveloadingview.WaveLoadingView;

public class PenghitungAir extends AppCompatActivity {

    WaveLoadingView waveLoadingView;
    IndicatorSeekBar indicatorSeekBar;
    Button button;
    TextView keterangan, konsumsi_TV;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    LinearLayout linearLayout, loading;
    RotateLoading rotateLoading;

    int konsumsiAir = 0;
    int persentaseAir;
    String persentaseTv;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penghitung_air);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        waveLoadingView = findViewById(R.id.waveLoadingView);
        indicatorSeekBar = findViewById(R.id.indicatorSeekBar);
        linearLayout = findViewById(R.id.linearLayout);
        keterangan = findViewById(R.id.keterangan);

        loading = findViewById(R.id.Loading);
        rotateLoading = findViewById(R.id.rotateLoading);
        rotateLoading.start();

        final String string1 = "Minumlah air minimal 2000mL per hari!!";
        final String string2 = "Anda sudah memenuhi batas minimal konsumsi air :)";

        final CustomDialogWater customDialog = new CustomDialogWater(PenghitungAir.this);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        konsumsi_TV = findViewById(R.id.konsumsi_TV);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                persentaseTv = documentSnapshot.getString("Persentase air");
                konsumsi_TV.setText(documentSnapshot.getString("Konsumsi air"));

                waveLoadingView.setProgressValue(Integer.parseInt(persentaseTv));
                waveLoadingView.setCenterTitle(persentaseTv + "%");
                konsumsiAir = Integer.parseInt(konsumsi_TV.getText().toString().trim());

                if (persentaseTv.equals("100")) {
                    keterangan.setText(string2);
                }
                else {
                    keterangan.setText(string1);
                }

                button.setVisibility(View.VISIBLE);
                indicatorSeekBar.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                waveLoadingView.setVisibility(View.VISIBLE);
                keterangan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);
            }
        });

        button = findViewById(R.id.addwater);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.startLoadingAnimation();

                int air = indicatorSeekBar.getProgress() / 20;
                int value = waveLoadingView.getProgressValue();

                konsumsiAir = konsumsiAir + indicatorSeekBar.getProgress();

                final int persentase = air + value;
                if (persentase <= 100) {
                    persentaseAir = persentase;
                }
                else {
                    persentaseAir = 100;
                }

                Map<String,Object> user = new HashMap<>();
                user.put("Persentase air", String.valueOf(persentaseAir));
                user.put("Konsumsi air", String.valueOf(konsumsiAir));

                fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customDialog.dissmissDialog();

                        new StyleableToast.Builder(PenghitungAir.this)
                                .text(indicatorSeekBar.getProgress() + "mL air telah ditambahkan")
                                .iconStart(R.drawable.ic_glass)
                                .backgroundColor(Color.WHITE)
                                .show();

                        if (persentase < 100) {
                            keterangan.setText(string1);
                            konsumsi_TV.setText(String.valueOf(konsumsiAir));
                        }
                        else {
                            keterangan.setText(string2);
                            konsumsi_TV.setText(String.valueOf(konsumsiAir));
                        }

                        if (waveLoadingView.getProgressValue() <= 100) {
                            waveLoadingView.setProgressValue(persentaseAir);
                            waveLoadingView.setCenterTitle(persentaseAir + "%");
                        }
                        else {
                            waveLoadingView.setProgressValue(100);
                            waveLoadingView.setCenterTitle(100 + "%");
                        }
                    }
                });

                setAlarm();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.penghitung_air);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.catatan_harian:
                        startActivity(new Intent(getApplicationContext(), CatatanHarian.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.penghitung_air:
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

        createNotificationChannel();
    }

    private void setAlarm() {
        Intent intent = new Intent(PenghitungAir.this, AlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PenghitungAir.this, 0, intent, 0);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long after10sec = 1000 * 3600 * 6;

        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,
                currentTime + after10sec,
                pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Penghitung Air";
            String description = "Sudahkah anda minum air hari ini?";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDietKuy", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void resetDatapenghitungAir() {
        final CustomDialogList customDialogList = new CustomDialogList(PenghitungAir.this);
        customDialogList.startLoadingAnimation();

        Map<String,Object> user = new HashMap<>();
        user.put("Persentase air", "0");
        user.put("Konsumsi air", "0");

        fStore.collection("users").document(userID).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                customDialogList.dissmissDialog();
                waveLoadingView.setProgressValue(Integer.parseInt("0"));
                waveLoadingView.setCenterTitle("0%");
                keterangan.setText("Minumlah air minimal 2000mL per hari!!");
                konsumsiAir = Integer.parseInt("0");
                konsumsi_TV.setText("0");
            }
        });

    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Mereset data");
        alert.setMessage("Apakah anda yakin? Semua data yang telah anda masukkan di penghitung air hari ini akan dihapus!!");
        alert.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetDatapenghitungAir();
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_reset) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
