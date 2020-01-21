package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.login.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity {

    private List<Model_musicNamePic> lastList,likedList,reccmdList;
    private FirebaseAuth auth;
    int[] images = new int[65];


    private String snapName="";
    private int snapRes=0;
    int okyLast=0;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    ArrayList<String> muzikler;
    private EditText search;
    private RecyclerView lastSongsRecycle,likedSongsRecycle,recmmSongsRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        setImages();
        initId();
        //////not:aktif edilmeli
        //MuzikListesiniGuncelle();
        ButtnOnClick();
        initRecycles();
        menu();





    }

    private void menu() {

        Button recmd=findViewById(R.id.bt_recmd_btn);
        Button liked=findViewById(R.id.bt_liked_btn);
        Button home =findViewById(R.id.bt_home_btn);
        TextView textMenu= findViewById(R.id.bt_home_txt);
        textMenu.setTextColor(Color.parseColor("#f05408"));


        home.setBackgroundResource(R.drawable.ic_home_orange_24dp);

        recmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayMusicActivity.this,RecmmdSongsActivity.class);
                startActivity(i);
            }
        });
        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayMusicActivity.this,LikedSongsActivity.class);
                startActivity(i);
            }
        });

    }

    private void setImages() {
        for(int i =0 ;i<65;i++){
            String str="cover"+String.valueOf(i);
            images[i] = getResources().getIdentifier(str,"drawable",getPackageName());
        }
    }

    private void initId() {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        search = findViewById(R.id.sarki_ara_ana_ekran);
        ///recycle
        lastSongsRecycle = findViewById(R.id.recycle_last_music);
        likedSongsRecycle = findViewById(R.id.recycle_liked_songs);
        recmmSongsRecycle = findViewById(R.id.recycle_tavsiye_edilenler);


    }
    private void MuzikListesiniGuncelle() {

        StorageReference listRef = firebaseStorage.getReference();
        databaseReference=firebaseDatabase.getReference().child("Musics");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {

                        }
                        int sayac=0;
                        muzikler = new ArrayList<String>();
                        Random r = new Random();
                        for (StorageReference item : listResult.getItems()) {
                            muzikler.add(item.getName());
                            databaseReference.child(String.valueOf(sayac)).child("Name").setValue(item.getName());
                            databaseReference.child(String.valueOf(sayac)).child("ResourceId").setValue(String.valueOf(images[r.nextInt(images.length)]));
                            sayac++;

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });


    }
    private void initRecycles() {

        ///////Son Müzikler
        DatabaseReference lastRef=firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("LastSongs");
        lastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                verileriGoster(dataSnapshot);
                RecyclerViewHorizontal recyclerAdapter = new RecyclerViewHorizontal(PlayMusicActivity.this,lastList);
                LinearLayoutManager recyce = new LinearLayoutManager(PlayMusicActivity.this,LinearLayoutManager.HORIZONTAL,false);
                lastSongsRecycle.setLayoutManager(recyce);
                lastSongsRecycle.setItemAnimator( new DefaultItemAnimator());
                lastSongsRecycle.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference likedRef=firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("LikedSongs");
        likedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                verileriGosterLiked(dataSnapshot);
                RecyclerViewHorizontal recyclerAdapter = new RecyclerViewHorizontal(PlayMusicActivity.this,likedList);
                LinearLayoutManager recyce = new LinearLayoutManager(PlayMusicActivity.this,LinearLayoutManager.HORIZONTAL,false);
                likedSongsRecycle.setLayoutManager(recyce);
                likedSongsRecycle.setItemAnimator( new DefaultItemAnimator());
                likedSongsRecycle.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference recmmdRef=firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("RecommendedSongs");
        recmmdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                verileriGosterRccmd(dataSnapshot);
                RecyclerViewHorizontal recyclerAdapter = new RecyclerViewHorizontal(PlayMusicActivity.this,reccmdList);
                LinearLayoutManager recyce = new LinearLayoutManager(PlayMusicActivity.this,LinearLayoutManager.HORIZONTAL,false);
                recmmSongsRecycle.setLayoutManager(recyce);
                recmmSongsRecycle.setItemAnimator( new DefaultItemAnimator());
                recmmSongsRecycle.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void ButtnOnClick() {
        search.setInputType(InputType.TYPE_NULL);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayMusicActivity.this,SearchMusicActivity.class);
                i.putStringArrayListExtra("musics",muzikler);
                startActivity(i);
            }
        });
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Intent i = new Intent(PlayMusicActivity.this,SearchMusicActivity.class);
                    i.putStringArrayListExtra("musics",muzikler);
                    startActivity(i);
                }
            }
        });
    }


    private void verileriGoster(DataSnapshot dataSnapshot) {

        lastList = new ArrayList<Model_musicNamePic>();
        ////0,1,2,3,4
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Random r = new Random();
            Model_musicNamePic mmnp = new Model_musicNamePic();
            mmnp.setIsim(ds.getValue().toString());
            //mmnp.setResim(images[r.nextInt(images.length)]);
            DatabaseReference dbr=firebaseDatabase.getReference().child("Musics");
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            if(dataSnapshot2.getKey().equals("Name")){
                                 snapName=dataSnapshot2.getValue().toString();
                            }
                            if(dataSnapshot2.getKey().equals("ResourceId")){
                                 snapRes= Integer.valueOf(dataSnapshot2.getValue().toString());
                            }
                            if(snapName.equals(mmnp.getIsim())){
                                mmnp.setResim(snapRes);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            lastList.add(mmnp);


        }
    }
    private void verileriGosterLiked(DataSnapshot dataSnapshot) {

        likedList = new ArrayList<Model_musicNamePic>();
        ////0,1,2,3,4
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Random r = new Random();
            Model_musicNamePic mmnp = new Model_musicNamePic();
            mmnp.setIsim(ds.getValue().toString());
            mmnp.setResim(images[r.nextInt(images.length)]);

            DatabaseReference dbr=firebaseDatabase.getReference().child("Musics");
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){//012233
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            if(dataSnapshot2.getKey().equals("Name")){
                                snapName=dataSnapshot2.getValue().toString();
                            }
                            if(dataSnapshot2.getKey().equals("ResourceId")){
                                snapRes= Integer.valueOf(dataSnapshot2.getValue().toString());
                            }

                        }
                        if(snapName.equals(mmnp.getIsim())){
                            mmnp.setResim(snapRes);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            likedList.add(mmnp);

        }
    }
    private void verileriGosterRccmd(DataSnapshot dataSnapshot) {

        reccmdList = new ArrayList<Model_musicNamePic>();
        ////0,1,2,3,4
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Random r = new Random();
            Model_musicNamePic mmnp = new Model_musicNamePic();
            mmnp.setIsim(ds.getValue().toString());
            mmnp.setResim(images[r.nextInt(images.length)]);

            DatabaseReference dbr=firebaseDatabase.getReference().child("Musics");
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            if(dataSnapshot2.getKey().equals("Name")){
                                snapName=dataSnapshot2.getValue().toString();
                            }
                            if(dataSnapshot2.getKey().equals("ResourceId")){
                                snapRes= Integer.valueOf(dataSnapshot2.getValue().toString());
                            }
                            if(snapName.equals(mmnp.getIsim())){
                                mmnp.setResim(snapRes);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reccmdList.add(mmnp);
        }
    }








}

/*
* RANDOM IMAGE
*
* int[] images = {R.drawable.img1,R.drawable.im2};
    Random r = new Random();
    imageView.setImageResource(images[r.nextInt(images.length)]);


* */
