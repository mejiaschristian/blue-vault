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

        // Filler Content
        String title = "Smart City Traffic Management System";
        etTitle.setText(title);
        etAuthors.setText("Dalisay, Ricardo\nProvinci, Cardo");
        etSchool.setText("SASE");
        etCourse.setText("BSCS");
        etAbstract.setText("A comprehensive study on utilizing IoT and real-time data analytics to optimize traffic flow in urban areas...");
        etTags.setText("IoT, Smart City, Analytics");
        
        final String doiUrl = "https://doi.org/10.bluevault/superadmin.2024";
        tvDoiLink.setText(doiUrl);
        tvDoiLink.setClickable(true);
        tvDoiLink.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(doiUrl));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Link error", Toast.LENGTH_SHORT).show();
            }
        });

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