package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profile_view_user extends BaseActivity {

    private List<ResearchItem> userResearches;
    private ResearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);

        setupNavigation();

        // 1. Initialize UI Elements
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvId = findViewById(R.id.tvProfileId);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        TextView recyclerLabel = findViewById(R.id.recyclerLabel);
        Button addResBtn = findViewById(R.id.addResBtn);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // 2. Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");
        String school = sp.getString("school", ""); // Needed to target the right table

        // 3. Set basic info to UI
        tvName.setText(name);
        tvId.setText(id);
        tvEmail.setText(email);

        // Visibility check for Guests
        if (name.equals("N/A") || id.equals("N/A") || email.equals("N/A")) {
            if (recyclerLabel != null) recyclerLabel.setVisibility(GONE);
            if (addResBtn != null) addResBtn.setVisibility(GONE);
            if (recyclerView != null) recyclerView.setVisibility(GONE);
            if (navResearches != null) navResearches.setVisibility(GONE);
            if (navSecurity != null) navSecurity.setVisibility(GONE);
        }

        // 4. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userResearches = new ArrayList<>();
        adapter = new ResearchAdapter(userResearches, true); // true to show status
        recyclerView.setAdapter(adapter);

        // 5. Fetch Research from MySQL instead of local DataRepository
        if (!id.equals("N/A") && !school.isEmpty()) {
            fetchUserResearchFromMySQL(id, school);
        }

        // 6. Buttons Logic
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        if (addResBtn != null) {
            addResBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                startActivity(intent);
            });
        }
    }

    private void fetchUserResearchFromMySQL(String id, String school) {
        // Targets the PHP script created in the previous step
        String URL = "http://10.0.2.2/bluevault/GetUserResearch.php?id_number=" + id + "&school=" + school.toLowerCase();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    userResearches.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            // Map JSON data to ResearchItem constructor
                            userResearches.add(new ResearchItem(
                                    obj.getInt("rsid"),
                                    obj.getString("title"),
                                    obj.getString("author"),
                                    obj.getString("school"),
                                    obj.getString("course"),
                                    obj.getString("date"),
                                    obj.getInt("status"),
                                    obj.getString("abstract"),
                                    obj.getString("tags"),
                                    obj.getString("doi"),
                                    0.0f, // Rating
                                    false // Published
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error loading research items", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}