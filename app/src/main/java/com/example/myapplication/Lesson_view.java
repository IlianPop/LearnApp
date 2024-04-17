package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
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

public class Lesson_view extends AppCompatActivity {
    WebView webView;
    String name, mail, author_mail;
    TextView theme, goal, description, author;
    WebView veb;
    DatabaseReference db, db1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.lesson_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    public void Edit(View view){
        Intent intent = new Intent(Lesson_view.this, Less_edit.class);
        intent.putExtra("theme", theme.getText().toString());
        startActivity(intent);
        finish();
    }
    public void init(){
        button=findViewById(R.id.button4);
        webView=findViewById(R.id.web);
        theme=findViewById(R.id.textView);
        goal=findViewById(R.id.textView2);
        author=findViewById(R.id.textView4);
        description=findViewById(R.id.textView3);
        db = FirebaseDatabase.getInstance().getReference("Lesson");
        db1 = FirebaseDatabase.getInstance().getReference("User");
        Bundle i = getIntent().getExtras();
        name = i.getString("inf");
        mail = i.getString("User_mail");
        Lesson less = new Lesson();

        Query query = db.orderByChild("theme").equalTo(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Lesson les =snapshot1.getValue(Lesson.class);
                    assert les != null;
                    theme.setText(les.getTheme());
                    goal.setText(les.getGoal());
                    author.setText(les.getAuthor_name());
                    description.setText(les.getDescription());
                    author_mail=les.getAuthor_mail();
                    String url1=les.getUrl();
                    String[]urls=url1.split("/");
                    String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/";
                    video+=urls[3];
                    video+="title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                    webView.loadData(video, "text/html", "utf-8");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebChromeClient(new WebChromeClient());
                    if(!author_mail.equals(mail)) {
                        button.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = db1.orderByChild("mail").equalTo(mail);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(!snapshot1.getValue(User.class).getAdmin().equals("0")){
                        button.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void author(View view){
        Intent i = new Intent(Lesson_view.this, User_viewer.class);
        i.putExtra("User_name", name);
        i.putExtra("User_mail", mail);
        i.putExtra("inf", author_mail);
        startActivity(i);
    }
}