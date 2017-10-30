package com.example.codeml.chatapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class showChat extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    private static String my_username;
    private static String friend_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chat);

        mydatabase = openOrCreateDatabase("chat", MODE_PRIVATE, null);

        Intent intent = getIntent();
        my_username = intent.getStringExtra("username");
        friend_username = intent.getStringExtra("friendUsername");

        showAllmessages();
        fetchMessages();
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.messages);
        String message = String.valueOf(editText.getText());
        addMessage(message);
        messageTOdatabase(message);
    }

    public void addMessage(String message){
        addText(message, "#DCDCDC", "Me");
        addtoDatabase(message, 0);
    }

    public void addText(String message, String color, String by){

        TextView text = new TextView(this);
        text.setText(by+ ": " + message);
        text.setTextSize(25);
        text.setBackgroundColor(Color.parseColor(color));

        LinearLayout layout = (LinearLayout) findViewById(R.id.ll);
        layout.addView(text);
     }

     public void addtoDatabase(String message, int id){

         createTable();

         String query;
         String time = DateFormat.getDateTimeInstance().format(new Date());

         query = "INSERT INTO " + friend_username + " (id, m_date, s_message) VALUES (" + String.valueOf(id) + ",'" + time + "','" + message + "');" ;
         mydatabase.execSQL(query);
     }

     public void showAllmessages(){

         createTable();
         Cursor cr = mydatabase.rawQuery("SELECT * from " + friend_username + " ORDER BY m_date",null );
         int id;
         String message;
         if(cr.moveToFirst()){

             do{
                 id = cr.getInt(cr.getColumnIndex("id"));
                 message = cr.getString(cr.getColumnIndex("s_message"));
                 choose(message, id);
             }while (cr.moveToNext());

         }
     }

     public void createTable(){

         String query = "CREATE TABLE IF NOT EXISTS " + friend_username +" (id integer, m_date text, s_message text);";
         mydatabase.execSQL(query);
     }

     public void choose(String message, int id){
         if(id == 0){
             addText(message, "#DCDCDC", "Me");
         }
         else{
             addText(message, "#FFFFFF", friend_username);
         }
     }

     public void messageTOdatabase(String message){
         String time = DateFormat.getDateTimeInstance().format(new Date());
         DatabaseReference data = FirebaseDatabase.getInstance().getReference("CHATS");
         DatabaseReference id = data.child(my_username + "_" + friend_username).push();
         id.setValue(message);
         Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
     }

     public  void fetchMessages(){

         DatabaseReference data = FirebaseDatabase.getInstance().getReference("CHATS");
         data = data.child(friend_username + "_" + my_username +"/");
         final DatabaseReference finalData = data;
         data.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(final DataSnapshot datasnapshot, String s) {

                 String Key = datasnapshot.getKey();

                 String message = (String) datasnapshot.getValue();
                 choose(message, 1);
                 removeKey(Key);
                 addtoDatabase(message, 1);

             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //DO nothing
             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {
                 //DO nothing
             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                 //DO nothing
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
                 //DO nothing
             }
         });

     }

     public void removeKey(String Key){
         DatabaseReference data = FirebaseDatabase.getInstance().getReference("CHATS");
         data = data.child(friend_username + "_" + my_username);
         data.child(Key).removeValue();
     }

}
