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

public class Result_view extends AppCompatActivity {
    private EditText search;
    private DatabaseReference db;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listData;
    private String Lesson_theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.result_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        start();
        on_itemlistener();
    }
    public void rest(View view){
        start();
    }
    public void search(View view){
        Query query = db.orderByChild("lesson_theme").equalTo(Lesson_theme).limitToLast(20);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Result res = snap.getValue(Result.class);
                    if(res.getName().contains(search.getText().toString())) {
                        listData.add(res.getName());
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void init(){
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Lesson_theme=bundle.getString("lesson_theme");
        db = FirebaseDatabase.getInstance().getReference("Result");
        search = findViewById(R.id.editTextTextsearch);
        listView = findViewById(R.id.listview5);
        listData = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(arrayAdapter);
    }
    public void on_itemlistener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Result_view.this, Res.class);
                i.putExtra("Lesson_theme", Lesson_theme);
                String mail = listData.get(position);
                i.putExtra("user", mail);
                startActivity(i);
            }
        });
    }
    public void start(){
        Query query = db.orderByChild("lesson_theme").equalTo(Lesson_theme).limitToLast(20);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Result res = snap.getValue(Result.class);
                    listData.add(res.getName());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}