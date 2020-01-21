package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecmmdSongsActivity extends AppCompatActivity {

    private String snapName="";
    private int snapRes=0;
    private List<Model_musicNamePic> likedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recmmd_songs);
        menu();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("RecommendedSongs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initializeData(dataSnapshot);

                RecyclerView myrv = (RecyclerView) findViewById(R.id.recycle_activity_reccmdsongs);
                RecyclerViewVertical myAdapter = new RecyclerViewVertical(RecmmdSongsActivity.this,likedList);
                GridLayoutManager lLayout = new GridLayoutManager(RecmmdSongsActivity.this, 1);
                myrv.setLayoutManager(lLayout);
                myrv.setAdapter(myAdapter);
                myrv.setItemAnimator(new DefaultItemAnimator());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void menu() {
        ImageView back=findViewById(R.id.reccmd_back_finish);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button recmd=findViewById(R.id.bt_recmd_btn);
        Button liked=findViewById(R.id.bt_liked_btn);
        Button home =findViewById(R.id.bt_home_btn);
        TextView textMenu= findViewById(R.id.bt_recmd_txt);
        textMenu.setTextColor(Color.parseColor("#f05408"));

        recmd.setBackgroundResource(R.drawable.ic_supervisor_account_orange_24dp);

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecmmdSongsActivity.this,LikedSongsActivity.class);
                startActivity(i);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecmmdSongsActivity.this,PlayMusicActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }

    private void initializeData(DataSnapshot dataSnapshot) {
        likedList = new ArrayList<Model_musicNamePic>();
        ////0,1,2,3,4
        for (DataSnapshot ds : dataSnapshot.getChildren()){

            Model_musicNamePic mmnp = new Model_musicNamePic();
            mmnp.setIsim(ds.getValue().toString());


            DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("Musics");
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
}
