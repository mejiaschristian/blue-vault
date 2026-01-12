package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class upload_research extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upload_research);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Button gotoMenu = findViewById(R.id.goto_menu);

        if (gotoMenu != null) {
            gotoMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        EditText etTitle = findViewById(R.id.etTitle);
        AutoCompleteTextView actvCourse = findViewById(R.id.actvCourse);
        Button btnCancel = findViewById(R.id.backBtn);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Setup Course Dropdown
        if (actvCourse != null) {
            String[] courses = {"BSIT", "BSCS", "BSIS", "BSCPE"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
            actvCourse.setAdapter(adapter);
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> finish());
        }

        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                if (etTitle != null) {
                    String title = etTitle.getText().toString();
                    if (title.isEmpty()) {
                        Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Uploading..", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Uploading success! Waiting for approval.", Toast.LENGTH_LONG).show();
                        
                        Intent intent = new Intent(this, profile_view_user.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        if (drawerLayout != null) {
            setupDrawerNavigation(drawerLayout);
        }
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