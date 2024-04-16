package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.QwertyKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class User_viewer extends AppCompatActivity {
    private DatabaseReference db, db2, db3;
    private Button button1, button2;
    private TextView name_view, mail_view;
    public String name, mail, inf, bool="0";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_viewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        on_itemlistener();
    }
    public void init(){
        button1 = findViewById(R.id.button7);
        button2 = findViewById(R.id.button8);
        listView = findViewById(R.id.listview2);
        listData = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(arrayAdapter);
        name_view = findViewById(R.id.textView5);
        mail_view = findViewById(R.id.textView6);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("User_name");
        mail = bundle.getString("User_mail");
        inf = bundle.getString("inf");
        db = FirebaseDatabase.getInstance().getReference("User");
        db2 = FirebaseDatabase.getInstance().getReference("Lesson");
        db3 = FirebaseDatabase.getInstance().getReference("Black");
        Query query4 = db.orderByChild("mail").equalTo(mail);
        query4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    ViewGroup group1 = (ViewGroup) button1.getParent();
                    ViewGroup group2 = (ViewGroup) button2.getParent();
                    if(!snapshot1.getValue(User.class).getAdmin().equals("2")){
                        group1.removeView(button1);
                    }
                    if(!snapshot1.getValue(User.class).getAdmin().equals("1")&&!snapshot1.getValue(User.class).getAdmin().equals("2")){
                        group2.removeView(button2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = db.orderByChild("mail").equalTo(inf);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    name_view.setText(snapshot1.getValue(User.class).getName());
                    ViewGroup group2 = (ViewGroup) button2.getParent();
                    if(inf.equals("ilian.pop05@gmail.com")){
                        group2.removeView(button2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mail_view.setText(inf);
        Query query = db2.orderByChild("author_mail").equalTo(inf);
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
    }
    public void on_itemlistener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(User_viewer.this, Lesson_view.class);
                i.putExtra("User_name", name);
                i.putExtra("User_mail", mail);
                i.putExtra("inf", listData.get(position));
                startActivity(i);
            }
        });
    }
    public void delete(View view){
        Query query1 = db2.orderByChild("author_mail").equalTo(inf);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    snapshot1.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query query2 = db.orderByChild("mail").equalTo(inf);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    snapshot1.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Black_list black_list = new Black_list(db3.getKey(), inf);
        db3.push().setValue(black_list);
    }
    public void rules(View view){
        Query query = db.orderByChild("mail").equalTo(inf);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    if(user.getAdmin().equals("0")){
                        user.setAdmin("1");
                        Toast.makeText(getApplicationContext(), "User is now admin", Toast.LENGTH_SHORT).show();
                    }
                    else if(user.getAdmin().equals("1")){
                        user.setAdmin("0");
                        Toast.makeText(getApplicationContext(), "Еhe user is not an admin", Toast.LENGTH_SHORT).show();
                    }
                    snapshot1.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}