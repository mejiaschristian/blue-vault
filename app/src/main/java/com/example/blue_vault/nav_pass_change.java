package com.example.blue_vault;

import android.os.Bundle;
import android.widget.Button;

public class nav_pass_change extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_pass_change);
        
        // Initialize common navigation and side menu logic
        setupNavigation();

        // Page specific logic
        Button backBtn = findViewById(R.id.backBtn);
        Button applyBtn = findViewById(R.id.confirmPassBtn);

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        if (applyBtn != null) {
            applyBtn.setOnClickListener(v -> {
                // Here you would normally handle password change logic
                // For now, redirecting to login as requested
                logout(); 
            });
        }
    }
}