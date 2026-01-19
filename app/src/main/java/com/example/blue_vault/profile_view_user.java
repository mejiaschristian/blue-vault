package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class profile_view_user extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);

        // Standard setup from BaseActivity
        setupNavigation();

        Button btnUpload = findViewById(R.id.addResBtn);
        if (btnUpload != null) {
            btnUpload.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                startActivity(intent);
            });
        }

        // RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Fetch user-specific researches from the Data Repository
            // Using "Juan Dela Cruz" as the placeholder current user
            List<ResearchItem> userResearches = DataRepository.getInstance().getUserResearches("Juan Dela Cruz");

            // Initialize adapter with isProfileView = true to show status
            ResearchAdapter adapter = new ResearchAdapter(userResearches, true);
            recyclerView.setAdapter(adapter);
        }
    }
}