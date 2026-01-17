package com.example.blue_vault;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class main_dashboard extends BaseActivity {
    
    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private ResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown;
    private TextInputEditText searchInput;
    private String currentSchoolFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        setupNavigation();

        // Fetch Data from Repository
        allResearches = DataRepository.getInstance().getResearches();
        filteredResearches.addAll(allResearches);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

        // Setup Search Input
        searchInput = findViewById(R.id.search_input);
        if (searchInput != null) {
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    applyFilters(s.toString(), currentSchoolFilter);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Setup School Dropdown
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown = findViewById(R.id.school_dropdown);
        if (schoolDropdown != null) {
            schoolDropdown.setAdapter(schoolAdapter);
            schoolDropdown.setText(schools[0], false);
            schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentSchoolFilter = (String) parent.getItemAtPosition(position);
                applyFilters(searchInput.getText().toString(), currentSchoolFilter);
            });
        }
    }

    private void applyFilters(String query, String school) {
        filteredResearches.clear();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (ResearchItem item : allResearches) {
            boolean matchesSchool = school.equals("ALL") || (item.getSchool() != null && item.getSchool().equals(school));
            
            boolean matchesQuery = lowerCaseQuery.isEmpty() || 
                (item.getTitle() != null && item.getTitle().toLowerCase().contains(lowerCaseQuery)) ||
                (item.getAuthor() != null && item.getAuthor().toLowerCase().contains(lowerCaseQuery)) ||
                (item.getTags() != null && item.getTags().toLowerCase().contains(lowerCaseQuery));

            if (matchesSchool && matchesQuery) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}