package com.example.blue_vault;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class view_research_admin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_admin);
        setupNavigation();

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);
        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnDecline = findViewById(R.id.btnDecline);
        Button backBtn = findViewById(R.id.backBtn4);

        // Setting Filler Data
        etTitle.setText("A Study on AI in Modern Education");
        etAuthors.setText("John Doe, Jane Smith");
        etSchool.setText("SECA");
        etCourse.setText("BSIT");
        etAbstract.setText("This research explores the impact of Artificial Intelligence on student engagement and academic performance in higher education settings.");
        etTags.setText("AI, Education, Modern");
        
        final String doiUrl = "https://doi.org/10.1234/bluevault.2024";
        tvDoiLink.setText(doiUrl);
        tvDoiLink.setClickable(true);
        tvDoiLink.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(doiUrl));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show();
            }
        });

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        if (btnApprove != null) {
            btnApprove.setOnClickListener(v -> {
                Toast.makeText(this, "Research Approved", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        if (btnDecline != null) {
            btnDecline.setOnClickListener(v -> {
                Toast.makeText(this, "Research Declined", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}