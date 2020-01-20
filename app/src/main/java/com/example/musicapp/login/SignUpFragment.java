package com.example.musicapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicapp.AuthActivity;
import com.example.musicapp.PlayMusicActivity;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpFragment extends Fragment implements OnSignUpListener{
    private static final String TAG = "SignUpFragment";
    EditText id,sifre,tekrarSifre;
    private FirebaseAuth auth;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);
        id = inflate.findViewById(R.id.kayitId);
        sifre = inflate.findViewById(R.id.kayitSifre);
        tekrarSifre = inflate.findViewById(R.id.kayitSifreTekrar);

        return inflate;
    }

    @Override
    public void signUp() {
        String email = id.getText().toString();
        String parola = sifre.getText().toString();
        String tekrarparola = tekrarSifre.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getContext(),"Lutfen emailinizi giriniz",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(parola)){
            Toast.makeText(getContext(),"Lutfen sifrenizi giriniz",Toast.LENGTH_SHORT).show();
            return;
        }
        if (parola.length()<6){
            Toast.makeText(getContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!parola.equals(tekrarparola)){
            Toast.makeText(getContext(),"Sifreler Ayni Olmalidir",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(getContext(), AuthActivity.class);
        i.putExtra("yontem","2");
        i.putExtra("email",email);
        i.putExtra("parola",parola);
        startActivity(i);



    }
}
