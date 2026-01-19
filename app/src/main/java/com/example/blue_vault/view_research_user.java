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
        
        setupNavigation();

        // UI Elements
        Button backBtn = findViewById(R.id.backBtn);
        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);

        // Get data from Intent (passed from Adapter)
        ResearchItem research = (ResearchItem) getIntent().getSerializableExtra("research_data");

        if (research != null) {
            // Data available from database/intent
            tvHeaderTitle.setText(research.getTitle());
            etTitle.setText(research.getTitle());
            etAuthors.setText(research.getAuthor());
            etSchool.setText(research.getSchool());
            etCourse.setText(research.getCourse());
            etAbstract.setText(research.getResearchAbstract());
            etTags.setText(research.getTags());
            
            final String doiUrl = research.getDoi();
            tvDoiLink.setText(doiUrl);
            
            if (doiUrl != null && !doiUrl.isEmpty()) {
                tvDoiLink.setClickable(true);
                tvDoiLink.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(doiUrl));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            // Placeholder/Fallback Data
            tvHeaderTitle.setText("Research Title Placeholder");
            etTitle.setText("Sample Title");
            etAuthors.setText("Sample Author");
            etSchool.setText("Sample School");
            etCourse.setText("BSIT");
            etAbstract.setText("This is a placeholder abstract for when no data is provided.");
            etTags.setText("Placeholder, Sample");
            tvDoiLink.setText("No DOI available");
        }

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }
    }
}