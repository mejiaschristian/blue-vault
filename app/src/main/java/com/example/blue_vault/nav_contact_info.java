package com.example.blue_vault;

import android.os.Bundle;
import android.widget.Button;

public class nav_contact_info extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_contact_info);

        // Initialize common navigation logic
        setupNavigation();

        // Page specific logic: Back button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }
    }
}