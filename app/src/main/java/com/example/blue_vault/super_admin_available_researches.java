package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class super_admin_available_researches extends BaseActivity {

    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private SuperAdminResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown, courseDropdown, statusDropdown;

    private String currentSchoolFilter = "ALL";
    private String currentCourseFilter = "ALL";
    private String currentStatusFilter = "ALL";

    private final String[] allCourses = {"ALL", "ABComm", "BSPsych", "BPEd", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};
    private final String[] saseCourses = {"ALL", "ABComm", "BSPsych", "BPEd"};
    private final String[] sbmaCourses = {"ALL", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
    private final String[] secaCourses = {"ALL", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_available_researches);

        setupNavigation();

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SuperAdminResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

        // FETCH FROM DATABASE
        fetchUnpublishedAndDeclined();

        // Dropdowns setup
        setupFilters();
    }

    private void fetchUnpublishedAndDeclined() {
        String URL = "http://10.0.2.2/bluevault/GetUnpublishedAndDeclined.php";

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
                        applyFilters(currentSchoolFilter, currentCourseFilter, currentStatusFilter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void setupFilters() {
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        String[] statuses = {"ALL", "Approved", "Declined"};

        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses);

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
                applyFilters(currentSchoolFilter, currentCourseFilter, currentStatusFilter);
            });
        }

        updateCourseDropdown("ALL");
        if (courseDropdown != null) {
            courseDropdown.setText("ALL", false);
            courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentCourseFilter = (String) parent.getItemAtPosition(position);
                applyFilters(currentSchoolFilter, currentCourseFilter, currentStatusFilter);
            });
        }

        if (statusDropdown != null) {
            statusDropdown.setAdapter(statusAdapter);
            statusDropdown.setText(statuses[0], false);
            statusDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentStatusFilter = (String) parent.getItemAtPosition(position);
                applyFilters(currentSchoolFilter, currentCourseFilter, currentStatusFilter);
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
        if (courseDropdown != null) courseDropdown.setAdapter(courseAdapter);
    }

    private void applyFilters(String school, String course, String status) {
        filteredResearches.clear();
        for (ResearchItem item : allResearches) {
            boolean matchesSchool = school.equals("ALL") || item.getSchool().equalsIgnoreCase(school);
            boolean matchesCourse = course.equals("ALL") || item.getCourse().equalsIgnoreCase(course);
            
            boolean matchesStatus = true;
            if (!status.equals("ALL")) {
                int statusInt = status.equalsIgnoreCase("Approved") ? 1 : 0;
                matchesStatus = (item.getStatus() == statusInt);
            }

            if (matchesSchool && matchesCourse && matchesStatus) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}