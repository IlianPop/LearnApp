package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private EditText search;
    private DatabaseReference db, db2;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String>listData;
    private String mail, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        start();
        on_itemlistener();
    }
    public void init(){
        search = findViewById(R.id.editTextText);
        Bundle i = getIntent().getExtras();
        mail = i.getString("User_mail");
        name = i.getString("User_name");
        listView = findViewById(R.id.listview);
        listData = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(arrayAdapter);
        db = FirebaseDatabase.getInstance().getReference("User");
        db2 = FirebaseDatabase.getInstance().getReference("Lesson");
    }
    public void plus(View view){
        Intent intent = new Intent(Home.this, Less_plus.class);
        intent.putExtra("User_mail", mail);
        intent.putExtra("User_name", name);
        startActivity(intent);
    }
    public void start(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Lesson less = snap.getValue(Lesson.class);
                    listData.add(less.getTheme());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db2.limitToFirst(20).addValueEventListener(valueEventListener);
    }
    public void on_itemlistener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!listData.get(position).contains("User: ")) {
                    Intent i = new Intent(Home.this, Lesson_view.class);
                    i.putExtra("User_name", name);
                    i.putExtra("User_mail", mail);
                    i.putExtra("inf", listData.get(position));
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Home.this, User_viewer.class);
                    i.putExtra("User_name", name);
                    i.putExtra("User_mail", mail);
                    String mail1 = listData.get(position);
                    String h1 = mail1.split(" ")[1];
                    i.putExtra("inf", h1);
                    startActivity(i);
                }
            }
        });
    }
    public void search(View view){
        if(!search.getText().toString().isEmpty()) {
            String h = search.getText().toString();
            Query query = db2.orderByChild("search_name").startAt(h.toUpperCase()).endAt(h.toUpperCase() + "\uf8ff").limitToFirst(20);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listData.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Lesson less = snap.getValue(Lesson.class);
                        listData.add(less.getTheme());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Query query2 = db.orderByChild("mail").startAt(h).endAt(h + "\uf8ff").limitToFirst(20);
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        User user = snap.getValue(User.class);
                        listData.add("User: " + user.getMail());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void back(View view){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Lesson less = snap.getValue(Lesson.class);
                    listData.add(less.getTheme());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db2.addValueEventListener(valueEventListener);
    }
}