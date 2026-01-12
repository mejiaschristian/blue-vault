package com.example.blue_vault;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class view_research_user extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_user);
        
        // Initialize common navigation and side menu logic
        setupNavigation();

        // Setup UI Elements
        Button backBtn = findViewById(R.id.backBtn);
        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);

        // Populate with Sample Filler Content
        String title = "Automated Library Vault System using QR Code";
        tvHeaderTitle.setText(title);
        etTitle.setText(title);
        etAuthors.setText("Udarbe, LeBron Jamez Z.\nAlinsurin, Emmanuel C.");
        etCourse.setText("BSIT");
        etAbstract.setText("This research focuses on developing a secure and automated way to manage library resources using QR code technology for student identification and book tracking...");
        etTags.setText("Automation, QR Code, Library, Security");

        // Set DOI Link
        final String doiUrl = "https://doi.org/10.1234/bluevault.2024";
        tvDoiLink.setText(doiUrl);

        // Make the TextView clickable and handle the browser redirect
        tvDoiLink.setClickable(true);
        tvDoiLink.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(doiUrl));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation Logic
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

    }
}