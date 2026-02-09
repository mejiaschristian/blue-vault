package com.example.blue_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class view_research_user extends BaseActivity {

    private RatingBar ratingBarInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_user);
        setupNavigation();

        // 1. Initialize Session
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        final String loggedInID = sp.getString("id", "");

        // 2. Initialize Views
        ratingBarInput = findViewById(R.id.ratingBar3);
        Button backBtn = findViewById(R.id.backBtn);
        Button btnDelete = findViewById(R.id.btnDelete);
        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);

        TextView tvRemarksLabel = findViewById(R.id.tvRemarksLabel);
        TextView tvRemarks = findViewById(R.id.tvRemarks);

        // 3. Receive Data
        ResearchItem research = (ResearchItem) getIntent().getSerializableExtra("research_data");

        if (research != null) {
            tvHeaderTitle.setText(research.getTitle());
            etTitle.setText(research.getTitle());
            etAuthors.setText(research.getAuthor());
            etSchool.setText(research.getSchool());
            etCourse.setText(research.getCourse());
            etAbstract.setText(research.getAbstract());
            etTags.setText(research.getTags());

            String remarks = research.getRemarks();
            if (remarks != null && !remarks.trim().isEmpty()) {
                tvRemarks.setText(remarks);
                tvRemarks.setVisibility(View.VISIBLE);
                tvRemarksLabel.setVisibility(View.VISIBLE);
            } else {
                tvRemarks.setVisibility(View.GONE);
                tvRemarksLabel.setVisibility(View.GONE);
            }

            // FIXED: Ensuring the rating reflects the most recent data passed from the intent
            ratingBarInput.setRating(research.getRating());

            if (research.isPublished()) {
                ratingBarInput.setVisibility(View.VISIBLE);
            } else {
                ratingBarInput.setVisibility(View.GONE);
            }

            // 4. OWNERSHIP CHECK: Only show delete if the user owns this research
            // Using research.getIdNumber() (the foreign key IDnumber in your database)
            if (btnDelete != null) {
                if (loggedInID != null && loggedInID.equals(research.getIdNumber())) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Delete Research")
                                .setMessage("Are you sure you want to delete this research? This action cannot be undone.")
                                .setPositiveButton("Yes, Delete", (dialog, which) -> {
                                    deleteResearch(research.getRsID(), research.getSchool());
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    });
                } else {
                    btnDelete.setVisibility(View.GONE);
                }
            }

            // 5. Input Listener
            ratingBarInput.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
                if (fromUser) {
                    submitRating(research.getRsID(), research.getSchool(), rating, loggedInID);
                }
            });

            String doiUrl = research.getDoi();

            if (doiUrl != null && !doiUrl.isEmpty()) {
                doiUrl = doiUrl.trim();

                if (!doiUrl.startsWith("http://") && !doiUrl.startsWith("https://")) {
                    doiUrl = "https://" + doiUrl;
                }

                final String finalUrl = doiUrl;
                tvDoiLink.setText(finalUrl);
                tvDoiLink.setClickable(true);
                tvDoiLink.setPaintFlags(tvDoiLink.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

                tvDoiLink.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "No browser found to open link", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tvDoiLink.setText("No DOI available");
            }
        }

        if (backBtn != null) backBtn.setOnClickListener(v -> finish());
    }

    private void deleteResearch(int rsid, String school) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://" + ip + "/bluevault/DeleteResearch.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(this, "Research deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsid", String.valueOf(rsid));
                params.put("school", school.toLowerCase());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void submitRating(int rsid, String school, float ratingValue, String userId) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/UpdateRating.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String cleanResponse = response.trim();
                    if (!cleanResponse.toLowerCase().contains("error")) {
                        Toast.makeText(this, "Rating Saved! New Average: " + cleanResponse, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsid", String.valueOf(rsid));
                params.put("school", school.toLowerCase());
                params.put("new_rating", String.valueOf(ratingValue));
                params.put("user_id", userId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}