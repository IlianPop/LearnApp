package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg_log extends AppCompatActivity {
    private EditText mail, password, namet;
    public DatabaseReference db, db2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.reg_log);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    public void init(){
        db = FirebaseDatabase.getInstance().getReference("User");
        db2 = FirebaseDatabase.getInstance().getReference("Black");
        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        namet = findViewById(R.id.nametext);
    }
    public void register(View view){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(mail.getText().toString());
        if(matcher.matches()&& !mail.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()&&!namet.getText().toString().isEmpty()) {
            Query query = db.orderByChild("mail").equalTo(mail.getText().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Query query1 = db2.orderByChild("mail").equalTo(mail.getText().toString());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    User user = new User(namet.getText().toString(), mail.getText().toString(), password.getText().toString(), db.getKey());
                                    db.push().setValue(user);
                                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                                    mail.setText("");
                                    password.setText("");
                                    namet.setText("");
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "This address is blocked", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "The mail was already registered", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "incorrect data", Toast.LENGTH_SHORT).show();
        }
    }
    public void signin(View view){
        Query query =db.orderByChild("log_pas").equalTo(mail.getText().toString()+"_"+password.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user=new User();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        user=snapshot1.getValue(User.class);
                    }
                    Intent intent = new Intent(Reg_log.this, Home.class);
                    intent.putExtra("User_name", user.getName());
                    intent.putExtra("User_mail", user.getMail());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "incorect data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}