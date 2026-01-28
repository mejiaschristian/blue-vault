package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class profile_view_user extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        //added brokens na burger layout (copy paste this method to every activity na merong burger icon)
        setupNavigation();

        // Initialize TextViews (Ensure these IDs match your profile_view_user.xml)
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvId = findViewById(R.id.tvProfileId);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        TextView recyclerLabel = findViewById(R.id.recyclerLabel);
        Button addResBtn = findViewById(R.id.addResBtn);
        View recyclerItems = findViewById(R.id.recyclerView);
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");

        // Set text to UI
        tvName.setText(name);
        tvId.setText(id);
        tvEmail.setText(email);

        if (name.equals("N/A") || id.equals("N/A") || email.equals("N/A")) {
            recyclerLabel.setVisibility(GONE);
            addResBtn.setVisibility(GONE);
            recyclerItems.setVisibility(GONE);
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        }

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        //Load researches based on the name
        DataRepository dataRepo = DataRepository.getInstance();
        //Get the logged in name to show the user's researches
        List<ResearchItem> userResearches = dataRepo.getUserResearches(name);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ResearchAdapter(userResearches));

        Button btnUpload = findViewById(R.id.addResBtn);
        if (btnUpload != null) {
            btnUpload.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                startActivity(intent);
            });
        }
    }
}
