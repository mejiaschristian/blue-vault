package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class nav_about_us extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_about_us);
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // Initialize common navigation and side menu logic from BaseActivity
        setupNavigation();
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");

        if (name.equals("N/A") || id.equals("N/A") || email.equals("N/A")) {
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        }

        // Page specific logic: Back button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }
    }
}