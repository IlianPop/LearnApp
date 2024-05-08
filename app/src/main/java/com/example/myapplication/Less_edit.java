package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class Less_edit extends AppCompatActivity {
    DatabaseReference db, db2, db3;
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
        db2 = FirebaseDatabase.getInstance().getReference("Test");
        db3 = FirebaseDatabase.getInstance().getReference("Result");
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
                    String ewq = "https://youtu.be/"+lesson.getUrl();
                    url.setText(ewq);
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
        Query query2 = db.orderByChild("theme").equalTo(theme1.getText().toString());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    if (url.getText().toString().matches("^(https?|ftp)://.*$")) {
                        Query query = db.orderByChild("theme").equalTo(theme);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    String[] urls = url.getText().toString().split("/");
                                    String ewq = urls[3];
                                    if (ewq.contains("?")) {
                                        ewq = ewq.split("\\?")[0];
                                    }
                                    Lesson lesson = new Lesson(theme1.getText().toString(), goal.getText().toString(), description.getText().toString(), ewq, db.getKey(), author_mail, author_name, "");
                                    snapshot1.getRef().setValue(lesson);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Query query1 = db2.orderByChild("lesson_theme").equalTo(theme);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1:snapshot.getChildren()){
                                    Test test = snapshot1.getValue(Test.class);
                                    test.setLesson_theme(theme1.getText().toString());
                                    snapshot1.getRef().removeValue();
                                    db2.push().setValue(test);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Query query3 = db3.orderByChild("lesson_theme").equalTo(theme);
                        query3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1:snapshot.getChildren()){
                                    Result res = snapshot1.getValue(Result.class);
                                    res.setLesson_theme(theme1.getText().toString());
                                    snapshot1.getRef().removeValue();
                                    db3.push().setValue(res);                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Не посилання", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Назва зайнята", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        Query query1 = db2.orderByChild("lesson_theme").equalTo(theme);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
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
        Query query2 = db3.orderByChild("lesson_theme").equalTo(theme);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
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
    }
    public void teste(View ciew){
        Intent intent = new Intent(Less_edit.this, Test_edit.class);
        intent.putExtra("author_mail", author_mail);
        intent.putExtra("lesson_theme", theme);
        startActivity(intent);
    }
}