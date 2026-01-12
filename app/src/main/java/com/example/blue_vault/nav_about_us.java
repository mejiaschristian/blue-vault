package com.example.blue_vault;

import android.os.Bundle;
import android.widget.Button;

public class nav_about_us extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_about_us);

        // Initialize common navigation and side menu logic from BaseActivity
        setupNavigation();

        // Page specific logic: Back button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }
    }
}