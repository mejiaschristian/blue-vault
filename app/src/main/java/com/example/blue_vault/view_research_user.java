package com.example.blue_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class view_research_user extends BaseActivity {

    private RatingBar ratingBarInput; // @+id/ratingBar3

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
        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etCourse = findViewById(R.id.etCourse);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        TextView tvDoiLink = findViewById(R.id.tvDoiLink);

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

            // Set the bar to show the user's CURRENT rating (if we had it)
            // or just the current average as a starting point
            ratingBarInput.setRating(research.getRating());

            // 4. Input Listener (Preventing duplicate counts using IDnumber)
            ratingBarInput.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
                if (fromUser) {
                    submitRating(research.getRsID(), research.getSchool(), rating, loggedInID);
                }
            });

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
        }

        if (backBtn != null) backBtn.setOnClickListener(v -> finish());
    }

    private void submitRating(int rsid, String school, float ratingValue, String userId) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/UpdateRating.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String cleanResponse = response.trim();
                    if (!cleanResponse.toLowerCase().contains("error")) {
                        Toast.makeText(this, "Rating Saved! New Average: " + cleanResponse, Toast.LENGTH_SHORT).show();
                        // Note: The main dashboard will refresh automatically via onResume
                    }
                },
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsid", String.valueOf(rsid));
                params.put("school", school.toLowerCase());
                params.put("new_rating", String.valueOf(ratingValue));
                params.put("user_id", userId); // IDnumber from SharedPreferences
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}