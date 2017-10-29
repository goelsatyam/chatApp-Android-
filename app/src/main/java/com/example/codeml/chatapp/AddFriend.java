package com.example.codeml.chatapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity {
    private ArrayList<String> names;
    private   String my_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        SharedPreferences internal_data = getSharedPreferences("check", MODE_PRIVATE);
        my_username = internal_data.getString("username", null);

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.contains(" ")){
                    Toast.makeText(AddFriend.this, "Spaces are not allowed", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ShowMatches(s);
                return false;
            }
        });

    }

    public void ShowMatches(String s){
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("users/");
        Query query = data.orderByChild("username").startAt(s);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                names = new ArrayList<String>();

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    String username = data.getValue(user.class).username;
                    if(username.equals(my_username))
                        continue;
                    names.add(username);
                }

                if(names.isEmpty()){
                    return;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddFriend.this,android.R.layout.simple_list_item_1,names);
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        add(i);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }
        });

    }
    public void add(int index){
        final String username = names.get(index);

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("friendList");
        Query query = data.child(my_username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> friends = new ArrayList<String>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    friends.add((String) data.getValue());
                }

                if(friends.contains(username)){
                    Toast.makeText(AddFriend.this, "Already Friend", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference data = FirebaseDatabase.getInstance().getReference("friendList/");
                data.child(my_username).child(String.valueOf(friends.size())). setValue(username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
