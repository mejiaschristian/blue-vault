package com.example.blue_vault;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class view_research_admin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_admin);
        setupNavigation();

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etDoi = findViewById(R.id.etDoi);
        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnDecline = findViewById(R.id.btnDecline);

        // Setting Filler Data
        etTitle.setText("A Study on AI in Modern Education");
        etAuthors.setText("John Doe, Jane Smith");
        etCourse.setText("BSIT");
        etAbstract.setText("This research explores the impact of Artificial Intelligence on student engagement and academic performance in higher education settings.");
        etDoi.setText("https://doi.org/10.1234/bluevault.2024");

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