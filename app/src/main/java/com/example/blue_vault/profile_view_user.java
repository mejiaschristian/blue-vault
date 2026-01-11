package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class profile_view_user extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_view_user);

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

        // RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ResearchItem> researchList = new ArrayList<>();
        researchList.add(new ResearchItem("My Research 1", "Juan Dela Cruz", "BSIT", "Oct 1, 2024"));
        researchList.add(new ResearchItem("My Research 2", "Juan Dela Cruz", "BSIT", "Oct 10, 2024"));

        ResearchAdapter adapter = new ResearchAdapter(researchList);
        recyclerView.setAdapter(adapter);
    }
}