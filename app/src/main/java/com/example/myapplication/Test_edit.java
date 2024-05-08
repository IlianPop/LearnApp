package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Test_edit extends AppCompatActivity {
    private DatabaseReference db, db2;
    private RadioButton a, b, v;
    private EditText question, a_answer, b_answer, v_answer;
    private String author_mail, lesson_theme;
    private int position;
    private List<Test>tests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.test_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    private void init(){
        tests = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Test");
        db2 = FirebaseDatabase.getInstance().getReference("Result");
        a=findViewById(R.id.radioButton);
        b=findViewById(R.id.radioButton2);
        position=0;
        v=findViewById(R.id.radioButton3);
        question=findViewById(R.id.editTextText5);
        a_answer=findViewById(R.id.editTextText2);
        b_answer=findViewById(R.id.editTextText3);
        v_answer=findViewById(R.id.editTextText4);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        author_mail=bundle.getString("author_mail");
        lesson_theme=bundle.getString("lesson_theme");
        tests.clear();
        Query query = db.orderByChild("lesson_theme").equalTo(lesson_theme);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    tests.add(snapshot1.getValue(Test.class));
                    question.setText(tests.get(1).getQuestion());
                    a_answer.setText(tests.get(1).getA_answer());
                    b_answer.setText(tests.get(1).getB_answer());
                    v_answer.setText(tests.get(1).getV_answer());
                    switch (tests.get(1).getAnswer()) {
                        case "A":
                            a.setChecked(true);
                            b.setChecked(false);
                            v.setChecked(false);
                            break;
                        case "B":
                            a.setChecked(false);
                            b.setChecked(true);
                            v.setChecked(false);
                            break;
                        case "V":
                            a.setChecked(false);
                            b.setChecked(false);
                            v.setChecked(true);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(tests.isEmpty()){
            Test test = new Test("", "", "", "", "A", "",  author_mail, lesson_theme);
            tests.add(test);
        }
    }
    public void right(View view){
        String e ="A";
        if(b.isChecked()){
            e = "B";
        } else if (v.isChecked()) {
            e="V";
        }
        Test test = new Test(question.getText().toString(), a_answer.getText().toString(), b_answer.getText().toString(), v_answer.getText().toString(), e, db.getKey(), author_mail, lesson_theme);
        tests.set(position, test);
        position+=1;
        if (position < tests.size()) {
            question.setText(tests.get(position).getQuestion());
            a_answer.setText(tests.get(position).getA_answer());
            b_answer.setText(tests.get(position).getB_answer());
            v_answer.setText(tests.get(position).getV_answer());
            switch (tests.get(position).getAnswer()){
                case "A":
                    a.setChecked(true);
                    b.setChecked(false);
                    v.setChecked(false);
                    break;
                case "B":
                    a.setChecked(false);
                    b.setChecked(true);
                    v.setChecked(false);
                    break;
                case "V":
                    a.setChecked(false);
                    b.setChecked(false);
                    v.setChecked(true);
                    break;
            }
        } else {
            test = new Test("", "", "", "", "A", "", author_mail, lesson_theme);
            tests.add(test);
            question.setText(tests.get(position).getQuestion());
            a_answer.setText(tests.get(position).getA_answer());
            b_answer.setText(tests.get(position).getB_answer());
            v_answer.setText(tests.get(position).getV_answer());
            switch (tests.get(position).getAnswer()){
                case "A":
                    a.setChecked(true);
                    b.setChecked(false);
                    v.setChecked(false);
                    break;
                case "B":
                    a.setChecked(false);
                    b.setChecked(true);
                    v.setChecked(false);
                    break;
                case "V":
                    a.setChecked(false);
                    b.setChecked(false);
                    v.setChecked(true);
                    break;
            }
        }
    }
    public void load(View view){
        String e = "A";
        if (b.isChecked()) {
            e = "B";
        } else if (v.isChecked()) {
            e = "V";
        }
        Test test = new Test(question.getText().toString(), a_answer.getText().toString(), b_answer.getText().toString(), v_answer.getText().toString(), e, db.getKey(), author_mail, lesson_theme);
        tests.set(position, test);

        // Видаляємо всі тести з вказаною темою
        Query deleteQuery = db.orderByChild("lesson_theme").equalTo(lesson_theme);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }
                // Додаємо нові тести
                for(Test test11 : tests){
                    if(!test11.getA_answer().isEmpty() && !test11.getB_answer().isEmpty() && !test11.getV_answer().isEmpty() && !test11.getQuestion().isEmpty()) {
                        db.push().setValue(test11);
                    }
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обробка помилок
            }
        });
    }
    public void left(View view) {
        if (position > 0) {
            String e = "A";
            if (b.isChecked()) {
                e = "B";
            } else if (v.isChecked()) {
                e = "V";
            }
            Test test = new Test(question.getText().toString(), a_answer.getText().toString(), b_answer.getText().toString(), v_answer.getText().toString(), e, db.getKey(), author_mail, lesson_theme);
            tests.set(position, test);
            position -= 1;
            test = tests.get(position);
            question.setText(test.getQuestion());
            a_answer.setText(test.getA_answer());
            b_answer.setText(test.getB_answer());
            v_answer.setText(test.getV_answer());
            switch (test.getAnswer()) {
                case "A":
                    a.setChecked(true);
                    b.setChecked(false);
                    v.setChecked(false);
                    break;
                case "B":
                    a.setChecked(false);
                    b.setChecked(true);
                    v.setChecked(false);
                    break;
                case "V":
                    a.setChecked(false);
                    b.setChecked(false);
                    v.setChecked(true);
                    break;
            }
        }
    }
    public void delete(View view){
        Query query = db.orderByChild("lesson_theme").equalTo(lesson_theme);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    snapshot1.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query query1 = db2.orderByChild("lesson_theme").equalTo(lesson_theme);
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
    }
}