package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class PreMainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ArrayList<String> musics;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            auth=FirebaseAuth.getInstance();
            if (auth.getCurrentUser()!=null){
                Intent i = new Intent(PreMainActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
                /*Intent i = new Intent(PreMainActivity.this,MainActivity.class);
                startActivity(i);
                finish();*/
                Intent i = new Intent(PreMainActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_main);
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pre");
        myRef.setValue("Hello, World!");*/
        //tekseferlik();
        handler.postDelayed(runnable, 2000);
        //kontrol();

    }

    private void kontrol() {
        DatabaseReference musicRef= FirebaseDatabase.getInstance().getReference().child("Musics");
        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("kullanıcı sayisi", ""+dataSnapshot.getChildrenCount() );
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot11:dataSnapshot1.getChildren()){
                        if(dataSnapshot11.getKey().equals("ResourceId")){
                            Log.e("bosta kalanlar:", dataSnapshot11.getValue().toString() );
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void tekseferlik() {

        DatabaseReference musicRef= FirebaseDatabase.getInstance().getReference().child("Musics");
        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musics= new ArrayList<String>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot11:dataSnapshot1.getChildren()){
                        if(dataSnapshot11.getKey().equals("Name")){
                            musics.add(dataSnapshot11.getValue().toString());
                        }
                    }
                }
                Random r = new Random();
                int rand[]=new int[musics.size()];
                for(int i=0;i<musics.size();i++){
                    rand[i]=i;
                }
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
                for(int a=0;a<70;a++){
                    String key = FirebaseDatabase.getInstance().getReference().child("quiz").push().getKey();
                    for(int likedCount=0;likedCount<6;likedCount++){
                        String key2 = FirebaseDatabase.getInstance().getReference().child("quiz1").push().getKey();
                        int del=r.nextInt(musics.size());
                        databaseReference.child(key).child("LikedSongs").child(key2).setValue(musics.get(del));
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot11:dataSnapshot1.getChildren()){
                                if(dataSnapshot11.getKey().equals("Name")){
                                    if(dataSnapshot11.getValue().toString().equals(musics.get(del))){
                                        dataSnapshot11.getRef().removeValue();
                                    }
                                }
                            }
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
