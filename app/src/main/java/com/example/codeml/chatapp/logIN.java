package com.example.codeml.chatapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class logIN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    //if user clicked logIn button
    public void tryLogIn(View view) {
        //fetch userna,e
        EditText eView;
        eView = (EditText) findViewById(R.id.username);

        //fetch password
        String username = String.valueOf(eView.getText());
        eView = (EditText) findViewById(R.id.password);
        String password = String.valueOf(eView.getText());

        //check if anyone filed is empty
        if(password.length() == 0 || username.length() == 0){
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //if username contain spaces
        if(username.contains(" ")){
            Toast.makeText(this, "Spaces are not allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking username and password with database
        checkUsername(username, password);

    }

    public void checkUsername(final String username, final String password){
        //fetching data of username entered by user
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("users/");
        Query query = data.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                user check;

                try {
                     check = data.getChildren().iterator().next().getValue(user.class);
                    
                    if(password.equals(check.password)){
                        saveUsername(username);
                        Toast.makeText(logIN.this, "Log in successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        //password is in correct
                        Toast.makeText(logIN.this, "Incorrect password or username", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    //username is incorrect
                    Toast.makeText(logIN.this, "Please Sign In First", Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //DO Nothing
            }
        });
    }


    public void saveUsername(String username){
        SharedPreferences internal_data = getSharedPreferences("check", MODE_PRIVATE);
        SharedPreferences.Editor edit = internal_data.edit();
        edit.putString("username", username);
    }
}
