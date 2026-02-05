package com.example.blue_vault;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class super_admin_available_researches extends BaseActivity {

    private RecyclerView recyclerView;
    private SuperAdminResearchAdapter adapter;
    private List<ResearchItem> allResearches = new ArrayList<>(); // Store original data
    private List<ResearchItem> filteredResearches = new ArrayList<>(); // Data shown in UI
    private SwipeRefreshLayout swipeRefreshLayout;

    private AutoCompleteTextView schoolDropdown, courseDropdown, statusDropdown;
    private String currentSchoolFilter = "ALL";
    private String currentCourseFilter = "ALL";
    private String currentStatusFilter = "ALL";

    // Course data lists
    private final String[] allCourses = {"ALL", "ABComm", "BSPsych", "BPEd", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};
    private final String[] saseCourses = {"ALL", "ABComm", "BSPsych", "BPEd"};
    private final String[] sbmaCourses = {"ALL", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
    private final String[] secaCourses = {"ALL", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_available_researches);

        // Standard Navigation setup
        setupNavigation();
        // 2. Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sp.getString("email", "N/A");
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);
        // 3. Visibility logic
        if (email.equals("N/A")) {
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        } else {
            navResearches.setVisibility(VISIBLE);
            navSecurity.setVisibility(VISIBLE);
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchHistoryData());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Use filteredResearches for the adapter
        adapter = new SuperAdminResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

        setupDropdowns();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchHistoryData();
    }

    private void setupDropdowns() {
        // 1. School Dropdown
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown = findViewById(R.id.school_dropdown);
        courseDropdown = findViewById(R.id.course_dropdown);
        statusDropdown = findViewById(R.id.status_dropdown);

        if (schoolDropdown != null) {
            schoolDropdown.setAdapter(schoolAdapter);
            schoolDropdown.setText(schools[0], false);
            schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentSchoolFilter = (String) parent.getItemAtPosition(position);
                updateCourseDropdown(currentSchoolFilter);
                currentCourseFilter = "ALL";
                if (courseDropdown != null) courseDropdown.setText("ALL", false);
                applyFilters();
            });
        }

        // 2. Course Dropdown
        updateCourseDropdown("ALL");
        if (courseDropdown != null) {
            courseDropdown.setText("ALL", false);
            courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentCourseFilter = (String) parent.getItemAtPosition(position);
                applyFilters();
            });
        }

        // 3. Status Dropdown (Approved vs Declined)
        String[] statuses = {"ALL", "Approved", "Declined"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses);
        if (statusDropdown != null) {
            statusDropdown.setAdapter(statusAdapter);
            statusDropdown.setText(statuses[0], false);
            statusDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentStatusFilter = (String) parent.getItemAtPosition(position);
                applyFilters();
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

    private void applyFilters() {
        filteredResearches.clear();

        for (ResearchItem item : allResearches) {
            // Check School
            boolean matchesSchool = currentSchoolFilter.equals("ALL") ||
                    (item.getSchool() != null && item.getSchool().equalsIgnoreCase(currentSchoolFilter));

            // Check Course
            boolean matchesCourse = currentCourseFilter.equals("ALL") ||
                    (item.getCourse() != null && item.getCourse().equalsIgnoreCase(currentCourseFilter));

            // Check Status (1 = Approved/Published in your logic earlier, 0 = Declined)
            boolean matchesStatus = currentStatusFilter.equals("ALL");
            if (currentStatusFilter.equalsIgnoreCase("Approved")) {
                // If Approved/Published (Checking Status 2 for Published or 1 for Approved depending on your context)
                matchesStatus = (item.getStatus() == 1 || item.getStatus() == 2 || item.getStatus() == 0); // Adjust based on your specific Status ID for Approved
            } else if (currentStatusFilter.equalsIgnoreCase("Declined")) {
                matchesStatus = (item.getStatus() == 1); // If 1 is your Declined status
            }

            // Note: Update status logic based on your specific ID:
            // Based on your bindView earlier: 1 = Approved, 0 = Declined
            if (currentStatusFilter.equals("Approved")) matchesStatus = (item.getStatus() == 1);
            if (currentStatusFilter.equals("Declined")) matchesStatus = (item.getStatus() == 0);

            if (matchesSchool && matchesCourse && matchesStatus) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchHistoryData() {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/GetUnpublishedAndDeclined.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    Log.d("DEBUG_SUPER", "Response: " + response);

                    allResearches.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

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
                                    0.0f,
                                    false
                            ));
                        }
                        // Initially show all data
                        applyFilters();
                    } catch (JSONException e) {
                        Log.e("DEBUG_SUPER", "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    } finally {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                error -> {
                    Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
}