package com.example.musicapp.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.musicapp.AuthActivity;
import com.example.musicapp.MainActivity;
import com.example.musicapp.PlayMusicActivity;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";
    EditText id,sifre;
    public  int a;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    public LoginFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);

        id= inflate.findViewById(R.id.girisId);
        sifre = inflate.findViewById(R.id.girisSifre);
        inflate.findViewById(R.id.forgot_password).setOnClickListener(v ->
                Toast.makeText(getContext(), "Forgot password clicked", Toast.LENGTH_SHORT).show());
        return inflate;
    }

    @Override
    public void login() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


        String email = id.getText().toString();
        String password = sifre.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Lutfen Id'nizi Giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Lutfen Sifrenizi Giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }

       Intent i = new Intent(getContext(),AuthActivity.class);
        i.putExtra("yontem","1");
        i.putExtra("email",email);
        i.putExtra("parola",password);
        startActivity(i);



    }
}
