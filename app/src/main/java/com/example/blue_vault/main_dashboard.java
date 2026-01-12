package com.example.blue_vault;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class main_dashboard extends BaseActivity {
    
    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private ResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        // Use the base activity to set up navigation drawer and window insets
        setupNavigation();

        // Initialize Data
        allResearches.add(new ResearchItem("Sample Research Title 1", "Juan Dela Cruz", "SECA", "BSIT", "October 24, 2024"));
        allResearches.add(new ResearchItem("Sample Research Title 2", "Maria Clara", "SECA", "BSIT", "October 25, 2024"));
        allResearches.add(new ResearchItem("Sample Research Title 3", "Jose Rizal", "SASE", "PSY", "October 26, 2024"));
        allResearches.add(new ResearchItem("Sample Research Title 4", "Juan Dela Cruz", "SASE", "PSY", "October 24, 2024"));
        allResearches.add(new ResearchItem("Sample Research Title 5", "Maria Clara", "SBMA", "TOU", "October 25, 2024"));
        allResearches.add(new ResearchItem("Sample Research Title 6", "Jose Rizal", "SECA", "TOU", "October 26, 2024"));
        filteredResearches.addAll(allResearches);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

        // Setup School Dropdown
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown = findViewById(R.id.school_dropdown);
        schoolDropdown.setAdapter(schoolAdapter);
        
        // Make "ALL" selected by default
        schoolDropdown.setText(schools[0], false);

        // Filter Logic
        schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            filterData(selected);
        });
    }

    private void filterData(String school) {
        filteredResearches.clear();
        if (school.equals("ALL")) {
            filteredResearches.addAll(allResearches);
        } else {
            for (ResearchItem item : allResearches) {
                if (item.getSchool() != null && item.getSchool().equals(school)) {
                    filteredResearches.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}