package com.example.codeml.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chatApp extends AppCompatActivity {
    private final String FIREBASE_USERNAME = "satyamgoel@gmail.com";
    private final String FIREBASE_PASSWORD = "awsome";
    private final static int REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);
        //Connect to FireBase using
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(FIREBASE_USERNAME, FIREBASE_PASSWORD);

        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();

        if(!isSignin()){
            //TODO
        }
    }

    //Check if user has signIn previously
    public Boolean isSignin(){


        SharedPreferences internal_data = getSharedPreferences("check", MODE_PRIVATE);
        String username = internal_data.getString("username", null);

        if(username == null){
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void tryLogIn(View view) {

        Intent intent = new Intent(chatApp.this ,logIN.class);
        startActivityForResult(intent, REQ_CODE);

    }

    public void trySignIn(View view) {
        Intent intent = new Intent(chatApp.this ,signIn.class);
        startActivityForResult(intent, REQ_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQ_CODE == resultCode){
            isSignin();
        }
    }


}
