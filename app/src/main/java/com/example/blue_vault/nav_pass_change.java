package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class nav_pass_change extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_pass_change);

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Button gotoMenu = findViewById(R.id.goto_menu);

        gotoMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Navigation Drawer Item Clicks
        Button btnProfile = findViewById(R.id.nav_profile);
        Button btnAbout = findViewById(R.id.nav_about);
        Button btnSecurity = findViewById(R.id.nav_security);
        Button btnHelp = findViewById(R.id.nav_help);
        Button btnLogout = findViewById(R.id.logout_btn);

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, profile_view_user.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        btnAbout.setOnClickListener(v -> {
            startActivity(new Intent(this, nav_about_us.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        btnSecurity.setOnClickListener(v -> {
            startActivity(new Intent(this, nav_pass_change.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        btnHelp.setOnClickListener(v -> {
            startActivity(new Intent(this, nav_contact_info.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, main_login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}