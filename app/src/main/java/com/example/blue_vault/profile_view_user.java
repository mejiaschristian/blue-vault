package com.example.blue_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class profile_view_user extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);

        // Initialize TextViews (Ensure these IDs match your profile_view_user.xml)
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvId = findViewById(R.id.tvProfileId);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);

        // Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");

        // Set text to UI
        tvName.setText(name);
        tvId.setText(id);
        tvEmail.setText(email);

        Button btnUpload = findViewById(R.id.addResBtn);
        if (btnUpload != null) {
            btnUpload.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                startActivity(intent);
            });
        }

    }
}
