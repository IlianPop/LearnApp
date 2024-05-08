package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Res extends AppCompatActivity {
    private TextView name, theme, result, rating;
    private EditText nResult;
    private int Result, Rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.res_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void init(){
        name = findViewById(R.id.textView8);
        theme = findViewById(R.id.textView9);
        result = findViewById(R.id.textView10);
        rating = findViewById(R.id.textView11);
        nResult = findViewById(R.id.editTextText6);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String n = bundle.getString("user");
        String m = bundle.getString("lesson_theme");
        Query query = FirebaseDatabase.getInstance().getReference("Result").orderByChild("lesson_theme").equalTo(m).orderByChild("name").equalTo(n);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Result res = snapshot1.getValue(Result.class);
                    Result = Integer.parseInt(res.getResult());
                    Rating = Integer.parseInt(res.getRating());
                    name.setText(res.getName());
                    theme.setText(res.getLesson_theme());
                    result.setText(Integer.toString(Result)+"/"+Integer.toString(Rating));
                    rating.setText(result.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void translate(View view){
        rating.setText(Integer.toString(Result/Rating*Integer.parseInt(nResult.getText().toString()))+"/"+nResult.getText().toString());
    }
}