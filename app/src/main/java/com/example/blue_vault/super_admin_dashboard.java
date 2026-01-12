package com.example.blue_vault;

import android.content.Intent;
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