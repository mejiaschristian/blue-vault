package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
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

public class admin_pending_reqs extends BaseActivity {

    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private AdminResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown, courseDropdown;
    private String currentSchoolFilter = "ALL";
    private String currentCourseFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pending_reqs);

        // 1. Setup BaseActivity Navigation (Drawer)
        setupNavigation();

        // 2. Fix Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializing adapter with the Click Listener to open the details view
        adapter = new AdminResearchAdapter(filteredResearches, item -> {
            Intent intent = new Intent(admin_pending_reqs.this, view_research_admin.class);
            intent.putExtra("RESEARCH_ITEM", item);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // 5. Fetch Data and Setup Filters
        fetchPendingResearches();
        setupDropdowns();
    }

    private void fetchPendingResearches() {
        String URL = "http://10.0.2.2/bluevault/GetPendingResearch.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    allResearches.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            allResearches.add(new ResearchItem(
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
                                    0.0f, false
                            ));
                        }
                        applyFilters(currentSchoolFilter, currentCourseFilter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void setupDropdowns() {
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown = findViewById(R.id.school_dropdown);
        courseDropdown = findViewById(R.id.course_dropdown);

        if (schoolDropdown != null) {
            schoolDropdown.setAdapter(schoolAdapter);
            schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentSchoolFilter = (String) parent.getItemAtPosition(position);
                applyFilters(currentSchoolFilter, currentCourseFilter);
            });
        }
    }

    private void applyFilters(String school, String course) {
        filteredResearches.clear();
        for (ResearchItem item : allResearches) {
            boolean matchesSchool = school.equals("ALL") || item.getSchool().equalsIgnoreCase(school);
            if (matchesSchool) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}