package com.example.musicapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

//Kayitt
        if(getIntent().getExtras().getString("yontem").equals("2")){
            String email = getIntent().getExtras().getString("email");
            String parola = getIntent().getExtras().getString("parola");
            auth = FirebaseAuth.getInstance();


            auth.createUserWithEmailAndPassword(email,parola)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (!task.isSuccessful()) {
                                Toast.makeText(AuthActivity.this, "Yetkilendirme Hatası",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }


                            else {
                                auth=FirebaseAuth.getInstance();
                                firebaseDatabase=FirebaseDatabase.getInstance();
                                databaseReference=firebaseDatabase.getReference().child("Users");
                                databaseReference.child(auth.getCurrentUser().getUid().toString()).child("Bilgiler").child("Email").setValue(email);
                                databaseReference.child(auth.getCurrentUser().getUid().toString()).child("Bilgiler").child("Password").setValue(parola);
                                Toast.makeText(AuthActivity.this, "Basarili Olarak Kayit Yapildi", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AuthActivity.this, PlayMusicActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });



        }

        ///giris
        if(getIntent().getExtras().getString("yontem").equals("1")){
            String email = getIntent().getExtras().getString("email");
            String password = getIntent().getExtras().getString("parola");
            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(AuthActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(AuthActivity.this, PlayMusicActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else {

                                Toast.makeText(AuthActivity.this, "E-mail veya Sifre Hatali!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

        }



    }
}
