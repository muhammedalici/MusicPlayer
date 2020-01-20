package com.example.musicapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayScreenActivity extends AppCompatActivity {
    public TextView startTimeField,finishTimeField;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private int isLiked=0;
    private int isResource=0;
    private int resrce=0;
    private SeekBar seekbar;
    private Button playButton,next,prev,fav;
    private ImageView songImage;
    String myUri="";
    int start=0,lenght=0,isSeekbarKullanilabilir=1;
    public static int oneTimeOnly = 0;
    int index;
    ArrayList<String> muzikler=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        menu();
        songImage= findViewById(R.id.play_song_img);
        startTimeField =(TextView)findViewById(R.id.textView1);
        finishTimeField =(TextView)findViewById(R.id.textViewfinish);
        seekbar = (SeekBar)findViewById(R.id.seekBar1);
        playButton = (Button) findViewById(R.id.imageButton1);
        next = (Button) findViewById(R.id.next_button_playscr);
        prev = (Button) findViewById(R.id.back_button_playscr);
        fav = (Button) findViewById(R.id.favorite_button);
        seekbar.setProgress(0);



        index=Integer.valueOf(getIntent().getExtras().getString("index"));
        muzikler = getIntent().getStringArrayListExtra("sarkilar");


        String url="gs://musicapp-5a5f0.appspot.com/01 - rammstein - reise reise.mp3";
        url="gs://musicapp-5a5f0.appspot.com/"+muzikler.get(index);
        //Firebase
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef=storage.getReferenceFromUrl(url);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                myUri=uri.toString();
            }
        });

        StartSong();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null){
                    if(index!=muzikler.size()-1)index++;
                    else index=0;
                    startTimeField.setText("0:00");
                    oneTimeOnly = 0;
                    mediaPlayer.stop();
                    mediaPlayer=null;
                    startTime=0;
                    playButton.setBackgroundResource(R.drawable.play_24dp);
                    seekbar.setProgress(0);
                    String url="gs://musicapp-5a5f0.appspot.com/"+muzikler.get(index);
                    StorageReference storageRef=storage.getReferenceFromUrl(url);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myUri=uri.toString();
                        }
                    });
                    StartSong();

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null){
                    if(index!=0)index--;
                    else index=muzikler.size()-1;
                    startTimeField.setText("0:00");
                    oneTimeOnly = 0;
                    mediaPlayer.stop();
                    mediaPlayer=null;
                    startTime=0;
                    playButton.setBackgroundResource(R.drawable.play_24dp);
                    seekbar.setProgress(0);
                    String url="gs://musicapp-5a5f0.appspot.com/"+muzikler.get(index);
                    StorageReference storageRef=storage.getReferenceFromUrl(url);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myUri=uri.toString();
                        }
                    });
                    StartSong();

                }
            }
        });


        ////


    }

    private void menu() {
        ImageView back= findViewById(R.id.play_back_finish);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null){
                    mediaPlayer.stop();
                    mediaPlayer=null;
                    /*Intent intent = new Intent(getApplicationContext(),PlayMusicActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    finish();

                }
                else {
                    /*Intent intent = new Intent(getApplicationContext(),PlayMusicActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    finish();
                }


            }
        });

    }

    private void StartSong() {
        startTime = 0;
        oneTimeOnly=0;
        isLiked=0;
        fav.setBackgroundResource(R.drawable.ic_favorite_gray_24dp);

        TextView textView = findViewById(R.id.music_name_play);
        textView.setText(muzikler.get(index));

        //resim guncelle
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Musics");
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isResource=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    for (DataSnapshot resource : ds.getChildren()){
                        if(resource.getValue().toString().equals(muzikler.get(index))){
                            isResource=1;
                        }
                        if(resource.getKey().equals("ResourceId")){
                            resrce=Integer.valueOf(resource.getValue().toString());
                        }
                    }
                    if(isResource==1){
                        songImage.setBackgroundResource(resrce);
                    }
                    isResource=0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //kuyruğa ekleme
        DatabaseReference lastRef=   FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LastSongs");
        String key = FirebaseDatabase.getInstance().getReference().child("quiz").push().getKey();
        lastRef.child(key).setValue(muzikler.get(index));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LikedSongs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1: dataSnapshot.getChildren()){
                    if(ds1.getValue().toString().equals(muzikler.get(index))){
                        fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                        isLiked=1;
                    }
                }
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Silinecek kısım::::.::.
                        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Musics");
                        dbr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    for (DataSnapshot del: ds.getChildren()){
                                        if(del.getKey().equals("Name")&&del.getValue().toString().equals(muzikler.get(index))){
                                            //ds.getRef().removeValue();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        ////::::::::
                        if(isLiked==1){
                            fav.setBackgroundResource(R.drawable.ic_favorite_gray_24dp);
                            for (DataSnapshot ds1: dataSnapshot.getChildren()){
                                if(ds1.getValue().toString().equals(muzikler.get(index))){
                                    //ds1.getRef().removeValue();
                                    isLiked=0;
                                }
                            }
                        }
                        else{
                            fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                            DatabaseReference add = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LikedSongs");
                            String key = FirebaseDatabase.getInstance().getReference().child("quiz").push().getKey();
                            add.child(key).setValue(muzikler.get(index));
                            isLiked=1;
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(startTime==0){
                        mediaPlayer=new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(myUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        start=1;
                        playButton.setBackgroundResource(R.drawable.pause_24dp);
                        finalTime = mediaPlayer.getDuration();
                        if(lenght==finalTime){
                            start=1;
                            mediaPlayer.start();
                        }
                        startTime = mediaPlayer.getCurrentPosition();
                        if(oneTimeOnly == 0){
                            seekbar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        startTimeField.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                        );
                        finishTimeField.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) finalTime)))
                        );
                        seekbar.setProgress((int)startTime);
                        myHandler.postDelayed(UpdateSongTime,100);
                    }
                }
                catch (IOException e){
                    startTime=0;
                    Toast.makeText(PlayScreenActivity.this,"Şarkı Yüklenemedi Play Tuşuna Basınız.",Toast.LENGTH_SHORT).show();
                }

            }
        },2000);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                isSeekbarKullanilabilir=0;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarKullanilabilir=1;
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                else{
                    seekBar.setProgress(0);
                }
            }
        });
    }


    //**Play butonuna basıldıgında çalısacak methodu yazıyoruz...

    public void play(View view){
        if(start==0){
            try {
                mediaPlayer=new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                start=1;
                playButton.setBackgroundResource(R.drawable.pause_24dp);

            } catch (Exception e) {
                startTime=0;
                Toast.makeText(getApplicationContext(), "Şarkı Yükleniyor Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(start==1){
            mediaPlayer.pause();
            lenght=mediaPlayer.getCurrentPosition();
            start=2;
            playButton.setBackgroundResource(R.drawable.play_24dp);
        }
        else if(start==2){
            lenght=mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(lenght);
            mediaPlayer.start();
            start=1;
            playButton.setBackgroundResource(R.drawable.pause_24dp);
        }
        finalTime = mediaPlayer.getDuration();
        if(lenght==finalTime){
            start=1;
            mediaPlayer.start();
        }
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        startTimeField.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)))
        );
        finishTimeField.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) finalTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            if(mediaPlayer!=null){
                startTime = mediaPlayer.getCurrentPosition();

                if(startTime>=finalTime){
                    seekbar.setProgress(0);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.stop();
                    start=0;
                    startTimeField.setText("0:00");
                    playButton.setBackgroundResource(R.drawable.play_24dp);
                }
                else{

                    startTimeField.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
                    );
                    //Muziğin hangi sürede oldugunu gostermek icin, seekbar kullarak gosteriyoruz...
                    if(isSeekbarKullanilabilir==1){
                        seekbar.setProgress((int)startTime);
                    }
                    myHandler.postDelayed(this, 100);
                }
            }

        }
    };
    @Override
    public void  onBackPressed(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer=null;
            /*Intent intent = new Intent(getApplicationContext(),PlayMusicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();

        }
        else {
            /*Intent intent = new Intent(getApplicationContext(),PlayMusicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();
        }

    }

}
