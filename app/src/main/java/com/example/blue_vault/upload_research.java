package com.example.blue_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class upload_research extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.upload_research);

        // Retrieve User Session Data (ID and School table)
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        final String loggedID = sp.getString("id", "");
        final String loggedSchool = sp.getString("school", "");

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Button gotoMenu = findViewById(R.id.goto_menu);

        if (gotoMenu != null) {
            gotoMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // Initialize Input Fields
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthors = findViewById(R.id.etAuthors);
        EditText etAbstract = findViewById(R.id.etAbstract);
        EditText etTags = findViewById(R.id.etTags);
        EditText etDoi = findViewById(R.id.etDoi);

        Button btnCancel = findViewById(R.id.backBtn);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> finish());
        }

        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                String title = etTitle.getText().toString().trim();
                String authors = etAuthors.getText().toString().trim();
                String resAbstract = etAbstract.getText().toString().trim();
                String tags = etTags.getText().toString().trim();
                String doi = etDoi.getText().toString().trim();

                if (title.isEmpty() || authors.isEmpty() || resAbstract.isEmpty() || tags.isEmpty() || doi.isEmpty()) {
                    Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
                } else if (loggedSchool.isEmpty()) {
                    Toast.makeText(this, "Session error. Please re-login.", Toast.LENGTH_SHORT).show();
                } else {
                    // Send data to MySQL
                    uploadDataToServer(loggedID, loggedSchool, title, authors, resAbstract, tags, doi);
                }
            });
        }

        if (drawerLayout != null) {
            setupDrawerNavigation(drawerLayout);
        }
    }

    private void uploadDataToServer(String id, String school, String title, String authors, String resAbstract, String tags, String doi) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/UploadResearch.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String result = response.trim();
                    if (result.equals("success")) {
                        Toast.makeText(this, "Uploading success! Waiting for approval.", Toast.LENGTH_LONG).show();
                        // Navigate back to user profile after success
                        Intent intent = new Intent(this, profile_view_user.class);
                        startActivity(intent);
                        finish();
                    } else if (result.equals("duplicate")) {
                        // Logic to handle existing title check from PHP
                        Toast.makeText(this, "A research with the same title already exists", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_number", id);
                params.put("school", school.toLowerCase());
                params.put("title", title);
                params.put("authors", authors);
                params.put("abstract", resAbstract);
                params.put("tags", tags);
                params.put("doi", doi);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setupDrawerNavigation(DrawerLayout drawerLayout) {
        Button btnProfile = findViewById(R.id.nav_profile);
        Button btnAbout = findViewById(R.id.nav_about);
        Button btnSecurity = findViewById(R.id.nav_security);
        Button btnHelp = findViewById(R.id.nav_help);
        Button btnLogout = findViewById(R.id.logout_btn);

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, profile_view_user.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> {
                startActivity(new Intent(this, nav_about_us.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnSecurity != null) {
            btnSecurity.setOnClickListener(v -> {
                startActivity(new Intent(this, nav_pass_change.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> {
                startActivity(new Intent(this, nav_contact_info.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(this, main_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}