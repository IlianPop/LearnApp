package com.example.myapplication;

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

public class Less_plus extends AppCompatActivity {
    public DatabaseReference db;
    private String name, mail;
    public EditText theme, goal, description, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.less_plus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    public void init(){
        db = FirebaseDatabase.getInstance().getReference("Lesson");
        theme = findViewById(R.id.themetext);
        goal = findViewById(R.id.goaltext);
        description = findViewById(R.id.descriptiontext);
        url = findViewById(R.id.urltext);
        Bundle i = getIntent().getExtras();
        assert i != null;
        mail = i.getString("User_mail");
        name = i.getString("User_name");
    }
    public void plus(View view){
        Query query = db.orderByChild("theme").equalTo(theme.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists() || !theme.getText().toString().contains("User: ")){
                    if(url.getText().toString().matches("^(https?|ftp)://.*$")) {
                        String[]urls=url.getText().toString().split("/");
                        String ewq = urls[3];
                        ewq = ewq.split("\\?")[0];
                        Lesson lesson = new Lesson(theme.getText().toString(), goal.getText().toString(), description.getText().toString(), ewq, db.getKey(), mail, name, "");
                        db.push().setValue(lesson);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Не посилання", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Така тема вже існує", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        finish();
    }
}