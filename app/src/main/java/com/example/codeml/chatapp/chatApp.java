package com.example.codeml.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatApp extends AppCompatActivity {
    private final String FIREBASE_USERNAME = "satyamgoel@gmail.com";
    private final String FIREBASE_PASSWORD = "awsome";
    private final static int REQ_CODE = 1234;
    private static String my_username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);
        //Connect to FireBase using
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(FIREBASE_USERNAME, FIREBASE_PASSWORD);

        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();

        isSignin();
    }

    //Check if user has signIn previously
    public void isSignin(){


        SharedPreferences internal_data = getSharedPreferences("check", MODE_PRIVATE);
        String username = internal_data.getString("username", null);
        my_username = username;

        if(username == null){
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_LONG).show();
            return;
        }

        showFriends();

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

        if(resultCode == 19){
            showList();
        }

    }

    public void showFriends(){

        removeButton(R.id.signin);
        removeButton(R.id.login);

        showList();
    }

    //remove signIN and logIN button
    public void removeButton(int id){
        Button butt = (Button) findViewById(id);
        ViewGroup layout = (ViewGroup) butt.getParent();
        layout.removeView(butt);
    }

    public void showList(){

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("friendList");
        Query query = data.child(my_username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> friends = new ArrayList<String>();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    friends.add((String) data.getValue());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(chatApp.this,android.R.layout.simple_list_item_1, friends);
                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(chatApp.this, showChat.class);
                        intent.putExtra("friendUsername", friends.get(i));
                        intent.putExtra("username", my_username);
                        startActivityForResult(intent, 0);


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addfriend && my_username != null) {
            Intent intent = new Intent(chatApp.this, AddFriend.class);
            startActivityForResult(intent, 19);
            return true;
        }
        else if (id == R.id.removeFriend && my_username != null) {
            Intent intent = new Intent(chatApp.this, removeFriend.class);
            intent.putExtra("username", my_username);
            startActivityForResult(intent, 19);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
