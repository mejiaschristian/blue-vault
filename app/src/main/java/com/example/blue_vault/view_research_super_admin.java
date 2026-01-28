package com.example.blue_vault;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class view_research_super_admin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_super_admin);
        setupNavigation();

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnPublish = findViewById(R.id.btnPublish);
        Button backBtn = findViewById(R.id.backBtn3);

        // Get actual data from Intent (passed from SuperAdminResearchAdapter)
        ResearchItem research = (ResearchItem) getIntent().getSerializableExtra("research_data");

        if (research != null) {
            etTitle.setText(research.getTitle());
            etAuthors.setText(research.getAuthor());
            etSchool.setText(research.getSchool());
            etCourse.setText(research.getCourse());
            etAbstract.setText(research.getAbstract());
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
            // Fallback content
            etTitle.setText("Super Admin View");
            etAuthors.setText("N/A");
            etSchool.setText("N/A");
            etCourse.setText("N/A");
            etAbstract.setText("No data provided.");
            etTags.setText("N/A");
            tvDoiLink.setText("No link");
        }

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> finish());
        }

        if (btnPublish != null) {
            btnPublish.setOnClickListener(v -> {
                Toast.makeText(this, "Research Published Successfully!", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}