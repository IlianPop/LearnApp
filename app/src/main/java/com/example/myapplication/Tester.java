package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

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

public class Tester extends AppCompatActivity {
    private DatabaseReference db, db2;
    private RadioButton a, b, v;
    private TextView question;
    private String user, lesson_theme;
    private int position;
    private List<Test> tests;
    private List<String> s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tester);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    private void init(){
        tests = new ArrayList<>();
        s = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Test");
        db2 =FirebaseDatabase.getInstance().getReference("Result");
        a=findViewById(R.id.radioButton4);
        b=findViewById(R.id.radioButton5);
        position=0;
        v=findViewById(R.id.radioButton6);
        question=findViewById(R.id.textView7);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        user=bundle.getString("user");
        lesson_theme=bundle.getString("lesson_theme");
        Query query = db.orderByChild("lesson_theme").equalTo(lesson_theme);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    tests.add(snapshot1.getValue(Test.class));
                }
                if (!tests.isEmpty()) {
                    question.setText(tests.get(0).getQuestion());
                    a.setText(tests.get(0).getA_answer());
                    b.setText(tests.get(0).getB_answer());
                    v.setText(tests.get(0).getV_answer());
                    for (Test eqw : tests) {
                        s.add("i");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void right(View view){
        if(position<tests.size()-1) {
            String e = "A";
            if (b.isChecked()) {
                e = "B";
            } else if (v.isChecked()) {
                e = "V";
            }
            s.set(position, e);
            position += 1;
            question.setText(tests.get(position).getQuestion());
            a.setText(tests.get(position).getA_answer());
            b.setText(tests.get(position).getB_answer());
            v.setText(tests.get(position).getV_answer());
            switch (s.get(position)){
                case "B":
                    b.setChecked(true);
                    break;
                case "V":
                    v.setChecked(true);
                    break;
                default:
                    a.setChecked(true);
                    break;
            }
        }
    }
    public void left(View view){
        if(position>0) {
            String e = "A";
            if (b.isChecked()) {
                e = "B";
            } else if (v.isChecked()) {
                e = "V";
            }
            s.set(position, e);
            position -= 1;
            question.setText(tests.get(position).getQuestion());
            a.setText(tests.get(position).getA_answer());
            b.setText(tests.get(position).getB_answer());
            v.setText(tests.get(position).getV_answer());
            switch (s.get(position)){
                case "B":
                    b.setChecked(true);
                    break;
                case "V":
                    v.setChecked(true);
                    break;
                default:
                    a.setChecked(true);
                    break;
            }
        }
    }
    public void result(View view){
        String e = "A";
        if (b.isChecked()) {
            e = "B";
        } else if (v.isChecked()) {
            e = "V";
        }
        s.set(position, e);
        int res = 0;
        for(int i =0;i<s.size();i++){
            if(s.get(i).equals(tests.get(i).getAnswer())){
                res+=1;
            }
        }
        s.add("0");
        Result result = new Result(user, lesson_theme, Integer.toString(res), db2.getKey(), Integer.toString(tests.size()));
        Query query = db2.orderByChild("lesson_theme").equalTo(lesson_theme);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("name").getValue(String.class).equals(user)){
                        dataSnapshot.getRef().setValue(result);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    db2.push().setValue(result);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        finish();
    }
    public void resview(View view){
        Intent intent = new Intent(Tester.this, Result_view.class);
        intent.putExtra("lesson_theme", lesson_theme);
        startActivity(intent);
    }
}