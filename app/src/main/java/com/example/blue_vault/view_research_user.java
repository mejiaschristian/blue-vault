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
            etAbstract.setText(research.getAbstract());
            etTags.setText(research.getTags());

            String doiUrl = research.getDoi();

            if (doiUrl != null && !doiUrl.isEmpty()) {
                doiUrl = doiUrl.trim(); // Remove accidental spaces

                // FIX 1: Ensure URL starts with http:// or https://
                if (!doiUrl.startsWith("http://") && !doiUrl.startsWith("https://")) {
                    doiUrl = "https://" + doiUrl;
                }

                final String finalUrl = doiUrl;
                tvDoiLink.setText(finalUrl);
                tvDoiLink.setClickable(true);
                tvDoiLink.setPaintFlags(tvDoiLink.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG); // Make it look like a link

                tvDoiLink.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                        startActivity(intent);
                    } catch (Exception e) {
                        // FIX 2: Better error message for debugging
                        Toast.makeText(this, "No browser found to open link", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tvDoiLink.setText("No DOI available");
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