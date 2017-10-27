package com.example.codeml.chatapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class signIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    //if user click sign in button
    public void trySignIn(View view) {
        EditText eView;
        //fetch username
        eView = (EditText) findViewById(R.id.username);
        String username = String.valueOf(eView.getText());

        //fetch password
        eView = (EditText) findViewById(R.id.password);
        String password = String.valueOf(eView.getText());

        //fetch name
        eView = (EditText) findViewById(R.id.name);
        String name = String.valueOf(eView.getText());

        //if anyone of fields is empty
        if(password.length() == 0 || username.length() == 0 || name.length() == 0){
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //if usename has spaces
        if(username.contains(" ")){
            Toast.makeText(this, "Spaces are not allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        //check if usename is already picked or not
        checkUsername(name, username, password);

    }

    //add the user
    public void addUSER(String name, String username, String password){
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users");
        String userID = data.push().getKey();
        //saving value in Firebase
        data.child(userID).setValue(new user(name, username, password));
        saveUsername(username);
        Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    //check if username is not picked
    public void checkUsername(final String name, final String username, final String password){
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("users/");
        Query query = data.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user check = dataSnapshot.getValue(user.class);
                if(check == null){
                    addUSER(name, username, password);
                }
                else{
                    Toast.makeText(signIn.this, "Username already taken", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               //DO Nothing
            }
        });
    }

   //save username in app
   public void saveUsername(String username){
       SharedPreferences internal_data = getSharedPreferences("check", MODE_PRIVATE);
       SharedPreferences.Editor edit = internal_data.edit();
       edit.putString("username", username);
   }

}
