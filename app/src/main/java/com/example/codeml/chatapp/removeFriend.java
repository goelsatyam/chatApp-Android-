package com.example.codeml.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class removeFriend extends AppCompatActivity {

    private static String my_username;
    private static ArrayList<String> names;
    private static int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_friend);

        Intent intent = getIntent();
        my_username = intent.getStringExtra("username");

        DatabaseReference data = FirebaseDatabase.getInstance().getReference("friendList/");
        Query query = data.child(my_username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               //Toast.makeText(removeFriend.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                names = new ArrayList<String>();
                count = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    names.add((String) data.getValue());
                    count++;
                }

                showList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showList(){
        ListView listView = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                names.set(i, names.get(count-1));
                names.remove(count-1);
                adapter.notifyDataSetChanged();

                DatabaseReference db= FirebaseDatabase.getInstance().getReference("friendList");
                db.child(my_username).child(String.valueOf(count-1)).removeValue();
                db.child(my_username).child(String.valueOf(i)).setValue(names.get(i));

                Toast.makeText(removeFriend.this, "Removed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
