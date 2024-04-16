package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class Less_edit extends AppCompatActivity {
    DatabaseReference db;
    String theme, author_name, author_mail;
    public EditText theme1, goal, description, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.less_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    public void init(){
        author_mail="";
        author_name="";
        theme1=findViewById(R.id.themetext);
        goal=findViewById(R.id.goaltext);
        description=findViewById(R.id.descriptiontext);
        url = findViewById(R.id.urltext);
        db = FirebaseDatabase.getInstance().getReference("Lesson");
        Bundle i = getIntent().getExtras();
        theme=i.getString("theme");
        Query query = db.orderByChild("theme").equalTo(theme);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Lesson lesson = snapshot1.getValue(Lesson.class);
                    theme1.setText(lesson.getTheme());
                    goal.setText(lesson.getGoal());
                    description.setText(lesson.getDescription());
                    url.setText(lesson.getUrl());
                    author_name=lesson.getAuthor_name();
                    author_mail=lesson.getAuthor_mail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void save(View view){
        if(url.getText().toString().matches("^(https?|ftp)://.*$")) {
            Query query = db.orderByChild("theme").equalTo(theme);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Lesson lesson = new Lesson(theme1.getText().toString(), goal.getText().toString(), description.getText().toString(), url.getText().toString(), db.getKey(), author_mail, author_name);
                        snapshot1.getRef().setValue(lesson);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void delete(View view){
        Query query = db.orderByChild("theme").equalTo(theme);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    snapshot1.getRef().removeValue();
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}