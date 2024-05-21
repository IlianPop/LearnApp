package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Lesson_view extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    WebView webView;
    String name, mail, author_mail;
    TextView theme, goal, description, author;
    WebView veb;
    DatabaseReference db, db1;
    ImageButton button, button2;

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
        youTubePlayerView = findViewById(R.id.ytv);
        button=findViewById(R.id.button4);
        button2=findViewById(R.id.imageButton8);
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
        Query query2 = FirebaseDatabase.getInstance().getReference("Test").orderByChild("lesson_theme").equalTo(name);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    button2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    getLifecycle().addObserver(youTubePlayerView);
                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(url1, 0);
                        }
                    });
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
    public void test(View view){
        Intent intent = new Intent(Lesson_view.this, Tester.class);
        intent.putExtra("user", mail);
        intent.putExtra("lesson_theme", name);
        startActivity(intent);
    }
}