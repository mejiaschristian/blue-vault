package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class super_admin_dashboard extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_dashboard);
        
        // Initialize base activity logic (drawer, edge-to-edge)
        setupNavigation();

        Button teachersListBtn = findViewById(R.id.saRegTeachersBtn);
        Button availableResBtn = findViewById(R.id.saApprovedReqs);
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");

        if (name.equals("N/A") || id.equals("N/A") || email.equals("N/A")) {
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        }

        if (teachersListBtn != null) {
            teachersListBtn.setOnClickListener(v -> {
                startActivity(new Intent(this, super_admin_registered_teachers.class));
            });
        }

        if (availableResBtn != null) {
            availableResBtn.setOnClickListener(v -> {
                startActivity(new Intent(this, super_admin_available_researches.class));
            });
        }
    }
}