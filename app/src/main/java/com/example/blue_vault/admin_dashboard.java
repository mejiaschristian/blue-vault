package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class admin_dashboard extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        
        // Use the base activity to set up navigation if needed,
        // or customize the dashboard specific logic here.
        setupNavigation();
        Button studentList = findViewById(R.id.adminStdListBtn);
        Button pendingReq = findViewById(R.id.adminPendReq);
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

        studentList.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_registered_students.class));
        });
        pendingReq.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_pending_reqs.class));
        });

    }
}