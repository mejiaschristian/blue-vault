package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

        TextView profileName = findViewById(R.id.tvProfileName);
        TextView profileId = findViewById(R.id.tvProfileId);
        TextView profileEmail = findViewById(R.id.tvProfileEmail);

        // Fetch logged in user info from repository
        StudentItem currentUser = DataRepository.getInstance().getLoggedInUser();
        String currentEmail = DataRepository.getInstance().getLoggedInUserEmail();

        if (profileName != null && profileId != null && profileEmail != null && currentUser != null) {
            profileName.setText(currentUser.getName());
            profileId.setText(currentUser.getStudentID());
            profileEmail.setText(currentEmail);
        }

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
            // Using the current user's name from the repository
            List<ResearchItem> userResearches = DataRepository.getInstance().getUserResearches(currentUser != null ? currentUser.getName() : "Juan Dela Cruz");

            // Initialize adapter with isProfileView = true to show status
            ResearchAdapter adapter = new ResearchAdapter(userResearches, true);
            recyclerView.setAdapter(adapter);
        }
    }
}