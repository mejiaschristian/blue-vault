package com.example.blue_vault;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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

            // --- DYNAMIC BUTTON LOGIC START ---
            int status = research.getStatus();
            if (status == 1) { // Approved
                btnPublish.setText("Publish Research");
                btnPublish.setBackgroundColor(Color.parseColor("#FFD700")); // Yellow/Gold
            } else if (status == 0) { // Declined
                btnPublish.setText("Return to Pending");
                btnPublish.setBackgroundColor(Color.GRAY);
            }
            // --- DYNAMIC BUTTON LOGIC END ---


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
                if (research != null) {
                    if (research.getStatus() == 1) {
                        // If Approved, update to Published (Status 2)
                        updateStatus(research, "2");
                        Toast.makeText(this, "Research Published Successfully!", Toast.LENGTH_LONG).show();
                    } else if (research.getStatus() == 0) {
                        // If Declined, update back to Pending (Status 3)
                        updateStatus(research, "3");
                        Toast.makeText(this, "Moved back to Pending Requests", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void updateStatus(ResearchItem research, String status) {
        String URL = "http://10.0.2.2/bluevault/UpdateResearchStatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, super_admin_available_researches.class);
                        startActivity(intent);
                        finish(); // Return to list
                    } else {
                        Toast.makeText(this, "Error: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsid", String.valueOf(research.getRsID()));
                params.put("school", research.getSchool().toLowerCase());
                params.put("status", status);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}