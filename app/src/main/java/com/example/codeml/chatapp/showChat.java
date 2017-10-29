package com.example.codeml.chatapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        mydatabase = openOrCreateDatabase("chat",MODE_PRIVATE,null);

        Intent intent = getIntent();
        my_username = intent.getStringExtra("username");
        friend_username = intent.getStringExtra("friendUsername");
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.messages);
        String message = String.valueOf(editText.getText());
        addMessage(message);
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
         String query = "CREATE TABLE IF NOT EXISTS "+friend_username+"(id INTEGER,m_date Text, s_message Text);";
         mydatabase.execSQL(query);

         Date currentTime = Calendar.getInstance().getTime();
         Toast.makeText(this, (CharSequence) currentTime, Toast.LENGTH_SHORT).show();
     }
}
