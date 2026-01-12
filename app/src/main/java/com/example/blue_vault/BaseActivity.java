package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Base activity to handle common UI elements like the Navigation Drawer
 * and Edge-to-Edge display across the application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
    }

    /**
     * Call this in onCreate after setContentView to initialize the navigation drawer.
     */
    protected void setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            // Standard Window Insets handling
            ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Toggle drawer button
            Button gotoMenu = findViewById(R.id.goto_menu);
            if (gotoMenu != null) {
                gotoMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
            }

            setupDrawerButtons();
        }
    }

    private void setupDrawerButtons() {
        Button btnResearches = findViewById(R.id.nav_researches);
        Button btnProfile = findViewById(R.id.nav_profile);
        Button btnAbout = findViewById(R.id.nav_about);
        Button btnSecurity = findViewById(R.id.nav_security);
        Button btnHelp = findViewById(R.id.nav_help);
        Button btnAdmin = findViewById(R.id.nav_admin);
        Button btnLogout = findViewById(R.id.logout_btn);

        if (btnResearches != null) btnResearches.setOnClickListener(v -> navigateTo(main_dashboard.class));
        if (btnProfile != null) btnProfile.setOnClickListener(v -> navigateTo(profile_view_user.class));
        if (btnAbout != null) btnAbout.setOnClickListener(v -> navigateTo(nav_about_us.class));
        if (btnSecurity != null) btnSecurity.setOnClickListener(v -> navigateTo(nav_pass_change.class));
        if (btnHelp != null) btnHelp.setOnClickListener(v -> navigateTo(nav_contact_info.class));
        if (btnAdmin != null) btnAdmin.setOnClickListener(v -> navigateTo(admin_login.class));
        if (btnLogout != null) btnLogout.setOnClickListener(v -> logout());
    }

    private void navigateTo(Class<?> targetActivity) {
        // Prevent relaunching the same activity
        if (this.getClass() != targetActivity) {
            startActivity(new Intent(this, targetActivity));
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    void logout() {
        Intent intent = new Intent(this, main_login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}