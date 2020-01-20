package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchMusicActivity extends AppCompatActivity {

    private ImageView back,anamenu,likedsong,tavsiye;
    private ListView lv;
    private int refInt;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;
    ArrayList<String> muzikler;
    int sayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);
        menu();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Musics");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listele(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void listele(DataSnapshot dataSnapshot) {

        String products[] = new String[(int) dataSnapshot.getChildrenCount()];

        sayac=0;
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            for (DataSnapshot snapshot: ds.getChildren()){
                if(snapshot.getKey().equals("Name")){
                    Log.e("liste;   ",sayac+" : "+snapshot.getValue().toString() );
                    products[sayac]=snapshot.getValue().toString();
                }
            }
            sayac++;
        }
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.sarki_ara_main);


        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                SearchMusicActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("LastSongs");
                //uniqkey
                String key = FirebaseDatabase.getInstance().getReference().child("quiz").push().getKey();
                //reference.child(key).setValue(lv.getItemAtPosition(i).toString());

                /*reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if(refInt>Integer.valueOf(dataSnapshot1.getKey())){
                                refInt=Integer.valueOf(dataSnapshot1.getKey())-1;
                            }
                        }
                        reference.child(String.valueOf(refInt)).setValue(lv.getItemAtPosition(i).toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
                Log.e("SelectedItemPosition",""+ lv.getSelectedItemPosition() );
                Log.e("ItemAtPosition",""+ i );
                Log.e("selectedName",""+ lv.getItemAtPosition(lv.getSelectedItemPosition()) );
                Intent intnt = new Intent(SearchMusicActivity.this,PlayScreenActivity.class);
                muzikler=new ArrayList<String>();
                for (int s =0; s<products.length;s++){
                    muzikler.add(products[s]);
                }
                intnt.putStringArrayListExtra("sarkilar",muzikler);
                int index=i;
                for(int j=0;j<muzikler.size();j++){
                    if(muzikler.get(j).equals(lv.getItemAtPosition(i).toString())){
                        index=j;
                    }
                }
                intnt.putExtra("index",String.valueOf(index));
                startActivity(intnt);
                Toast.makeText(SearchMusicActivity.this, lv.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                /////play funch
            }
        });


    }

    private void menu() {
        ImageView mBack;
        Button mAnamenu,mLikedsongs,mTavsiye;
        mBack= findViewById(R.id.prev_search_music);
        mAnamenu=findViewById(R.id.bt_home_btn);
        mLikedsongs = findViewById(R.id.bt_liked_btn);
        mTavsiye = findViewById(R.id.bt_recmd_btn);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}