package com.example.blue_vault;

import static android.view.View.GONE;

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
    private final String[] allCourses = {"ALL", "ABComm", "BSPsych", "BPEd", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};
    private final String[] saseCourses = {"ALL", "ABComm", "BSPsych", "BPEd"};
    private final String[] sbmaCourses = {"ALL", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
    private final String[] secaCourses = {"ALL", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pending_reqs);

        // 1. Setup BaseActivity Navigation (Drawer)
        setupNavigation();
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);
        navResearches.setVisibility(GONE);
        navSecurity.setVisibility(GONE);

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
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/GetPendingResearch.php";

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
        // Setup School Dropdown
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown = findViewById(R.id.school_dropdown);
        courseDropdown = findViewById(R.id.course_dropdown);

        if (schoolDropdown != null) {
            schoolDropdown.setAdapter(schoolAdapter);
            schoolDropdown.setText(schools[0], false);
            schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentSchoolFilter = (String) parent.getItemAtPosition(position);
                updateCourseDropdown(currentSchoolFilter);
                currentCourseFilter = "ALL";
                courseDropdown.setText("ALL", false);
                applyFilters(currentSchoolFilter, currentCourseFilter);
            });
        }

        updateCourseDropdown("ALL");
        if (courseDropdown != null) {
            courseDropdown.setText("ALL", false);
            courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentCourseFilter = (String) parent.getItemAtPosition(position);
                applyFilters(currentSchoolFilter, currentCourseFilter);
            });
        }
    }

    private void updateCourseDropdown(String school) {
        String[] courses;
        switch (school) {
            case "SASE": courses = saseCourses; break;
            case "SBMA": courses = sbmaCourses; break;
            case "SECA": courses = secaCourses; break;
            default: courses = allCourses; break;
        }
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        if (courseDropdown != null) {
            courseDropdown.setAdapter(courseAdapter);
        }
    }

    private void applyFilters(String school, String course) {
        filteredResearches.clear();

        for (ResearchItem item : allResearches) {
            boolean matchesSchool = school.equals("ALL") || (item.getSchool() != null && item.getSchool().equalsIgnoreCase(school));
            boolean matchesCourse = course.equals("ALL") || (item.getCourse() != null && item.getCourse().equalsIgnoreCase(course));

            if (matchesSchool && matchesCourse) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}