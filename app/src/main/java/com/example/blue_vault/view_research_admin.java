package com.example.blue_vault;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View; // Required for View.GONE
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class view_research_admin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_research_admin);
        setupNavigation();

        // 1. Initialize Views
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

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sp.getString("email", "N/A");
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);
        // 3. Visibility logic
        if (email.equals("N/A")) {
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        } else {
            navResearches.setVisibility(VISIBLE);
            navSecurity.setVisibility(VISIBLE);
        }

        // 2. BACK BUTTON FIX: Ensure it is set before any logic that might 'finish()'
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        // 3. INTENT KEY FIX: Use "RESEARCH_ITEM" to match your admin_pending_reqs key
        // If this key is wrong, 'research' will be null and the activity will finish immediately
        ResearchItem research = (ResearchItem) getIntent().getSerializableExtra("RESEARCH_ITEM");

        if (research != null) {
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
            }

            // 4. CLICKABILITY LOGIC: Setup listeners for Approve and Decline
            if (btnApprove != null) {
                btnApprove.setOnClickListener(v -> {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Confirm Approval")
                            .setMessage("Are you sure you want to APPROVE this research? This action cannot be undone.")
                            .setPositiveButton("Yes, Approve", (dialog, which) -> {
                                updateStatusWithRemarks(research, "1", "");
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });
            }
            if (btnDecline != null) {
                btnDecline.setOnClickListener(v -> {
                    // Create an EditText inside the dialog for remarks
                    final EditText etRemarks = new EditText(this);
                    etRemarks.setHint("Enter reason for declining...");
                    etRemarks.setPadding(50, 20, 50, 20);

                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Decline Research")
                            .setMessage("Please provide a reason for declining this research. This will be visible to the user.")
                            .setView(etRemarks) // Add the input field to the dialog
                            .setPositiveButton("Submit Decline", (dialog, which) -> {
                                String remarks = etRemarks.getText().toString().trim();
                                if (remarks.isEmpty()) {
                                    Toast.makeText(this, "Remarks are required to decline", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Status "0" for Decline, passing remarks to the method
                                    updateStatusWithRemarks(research, "0", remarks);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });
            }

        } else {
            // This runs if the "RESEARCH_ITEM" Intent extra was missing or null
            Toast.makeText(this, "Error: No data found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateStatusWithRemarks(ResearchItem research, String status, String remarks) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/UpdateResearchStatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(this, "Research Status Updated with Remarks", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, admin_pending_reqs.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsid", String.valueOf(research.getRsID()));
                params.put("status", status);
                params.put("remarks", remarks); // SENDING THE REMARKS TO PHP
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}