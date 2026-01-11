package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class nav_about_us extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_about_us);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Button gotoMenu = findViewById(R.id.goto_menu);

        if (gotoMenu != null && drawerLayout != null) {
            gotoMenu.setOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }

        // Navigation Drawer Item Clicks
        Button btnProfile = findViewById(R.id.nav_profile);
        Button btnAbout = findViewById(R.id.nav_about);
        Button btnSecurity = findViewById(R.id.nav_security);
        Button btnHelp = findViewById(R.id.nav_help);
        Button btnLogout = findViewById(R.id.logout_btn);

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, profile_view_user.class));
                if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> {
                if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnSecurity != null) {
            btnSecurity.setOnClickListener(v -> {
                startActivity(new Intent(this, nav_pass_change.class));
                if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.START);
            });
        }

        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> {
                startActivity(new Intent(this, nav_contact_info.class));
                if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.START);
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

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> {
                onBackPressed();
            });
        }
    }
}