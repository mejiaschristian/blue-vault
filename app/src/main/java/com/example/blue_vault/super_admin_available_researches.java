package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class super_admin_available_researches extends BaseActivity {

    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private SuperAdminResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown, courseDropdown;
    
    private String currentSchoolFilter = "ALL";
    private String currentCourseFilter = "ALL";

    // Course Arrays (Same as dashboard for consistency)
    private final String[] allCourses = {"ALL", "ABComm", "BSPsych", "BPEd", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};
    private final String[] saseCourses = {"ALL", "ABComm", "BSPsych", "BPEd"};
    private final String[] sbmaCourses = {"ALL", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
    private final String[] secaCourses = {"ALL", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_available_researches);

        setupNavigation();
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentName = sp.getString("name", "N/A");
        String currentId = sp.getString("id", "N/A");
        String currentEmail = sp.getString("email", "N/A");

        if (currentName.equals("N/A") || currentId.equals("N/A") || currentEmail.equals("N/A")) {
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        }

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Initialize Data from Repository: Combine both Unpublished and Declined lists
        allResearches.clear();
        allResearches.addAll(DataRepository.getInstance().getUnpublishedResearches());
        allResearches.addAll(DataRepository.getInstance().getDeclinedResearches());
        
        filteredResearches.clear();
        filteredResearches.addAll(allResearches);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SuperAdminResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

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

        // Initial Course Dropdown Setup
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
        courseDropdown.setAdapter(courseAdapter);
    }

    private void applyFilters(String school, String course) {
        filteredResearches.clear();
        for (ResearchItem item : allResearches) {
            boolean matchesSchool = school.equals("ALL") || (item.getSchool() != null && item.getSchool().equals(school));
            boolean matchesCourse = course.equals("ALL") || (item.getCourse() != null && item.getCourse().equals(course));
            
            if (matchesSchool && matchesCourse) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}