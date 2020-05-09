package com.example.dietkuy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;
import com.victor.loading.book.BookLoading;

public class Profil extends AppCompatActivity implements CustomDialogResetPass.CustomDialogResetPassListener {

    TextView nama, email, tanggal, gender, tinggi, berat, sasaran;
    LinearLayout Loading;
    BookLoading bookLoading;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userID;
    ScrollView scrollView;
    StorageReference storageReference;
    CircularImageView circularImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+ userID +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circularImageView);
            }
        });

        nama = findViewById(R.id.Nama);
        email = findViewById(R.id.Email);
        tanggal = findViewById(R.id.tanggalLahir);
        gender = findViewById(R.id.JenisKelamin);
        tinggi = findViewById(R.id.TinggiBadan);
        berat = findViewById(R.id.BeratBadan);
        sasaran = findViewById(R.id.BeratBadanSasaran);

        Loading = findViewById(R.id.Loading);
        scrollView = findViewById(R.id.scrollView);
        circularImageView = findViewById(R.id.circularImageView);

        bookLoading = findViewById(R.id.bookLoading);
        bookLoading.start();

        showData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.catatan_harian:
                        startActivity(new Intent(getApplicationContext(), CatatanHarian.class));
                        overridePendingTransition(0,0);
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
                nama.setText(documentSnapshot.getString("Nama"));
                email.setText(documentSnapshot.getString("Email"));
                tanggal.setText(documentSnapshot.getString("Tanggal lahir"));
                gender.setText(documentSnapshot.getString("Jenis kelamin"));
                tinggi.setText(documentSnapshot.getString("Tinggi badan") + "cm");
                berat.setText(documentSnapshot.getString("Berat badan") + "kg");
                sasaran.setText(documentSnapshot.getString("Berat badan sasaran") + "kg");

                Loading.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                showAlertDialog2();
                return true;
            case R.id.item2:
                editProfile();
                return true;
            case R.id.item3:
                resetPassword();
                return true;
            case R.id.item4 :
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editProfile() {
        startActivity(new Intent(getApplicationContext(), EditProfile.class));
    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Log out");
        alert.setMessage("Anda akan keluar dari akun anda");
        alert.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    public void showAlertDialog2() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Peringatan");
        alert.setMessage("Beberapa jenis smartphone mungkin tidak dapat menggunakan fungsi ini");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeImageProfile();
            }
        });

        alert.create().show();
    }

    private void changeImageProfile() {
        Intent openImageGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openImageGallery, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/"+ userID +"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(circularImageView);
                    }
                });
            }
        });
    }

    private void resetPassword() {
        CustomDialogResetPass customDialogResetPass = new CustomDialogResetPass();
        customDialogResetPass.show(getSupportFragmentManager(), "custom dialog");
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }

    @Override
    public void applyTexts(String password) {
        user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new StyleableToast.Builder(Profil.this)
                        .text("Password telah diganti")
                        .iconStart(R.drawable.ic_lock)
                        .backgroundColor(Color.WHITE)
                        .show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profil.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
