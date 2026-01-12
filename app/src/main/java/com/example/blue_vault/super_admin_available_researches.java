package com.example.blue_vault;

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
    private AutoCompleteTextView statusDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_available_researches);

        // Initialize common navigation and drawer
        setupNavigation();

        // Setup Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Initialize Data
        allResearches.add(new ResearchItem("AI in Healthcare", "Dr. Smith", "SECA", "BSIT", "Oct 20, 2024", "Approved"));
        allResearches.add(new ResearchItem("Cybersecurity Trends", "Prof. James", "SECA", "BSCS", "Oct 21, 2024", "Declined"));
        allResearches.add(new ResearchItem("Blockchain for Finance", "Sarah Lee", "SBMA", "BSBA", "Oct 22, 2024", "Approved"));
        allResearches.add(new ResearchItem("Quantum Computing Intro", "Chris Evans", "SASE", "BS Physics", "Oct 23, 2024", "Declined"));
        filteredResearches.addAll(allResearches);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SuperAdminResearchAdapter(filteredResearches);
        recyclerView.setAdapter(adapter);

        // Setup Status Dropdown
        String[] statuses = {"ALL", "Approved", "Declined"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses);
        statusDropdown = findViewById(R.id.filter_status_text);
        if (statusDropdown != null) {
            statusDropdown.setAdapter(statusAdapter);
            
            // Make "ALL" selected by default
            statusDropdown.setText(statuses[0], false);

            // Filter Logic
            statusDropdown.setOnItemClickListener((parent, view, position, id) -> {
                String selected = (String) parent.getItemAtPosition(position);
                filterData(selected);
            });
        }
    }

    private void filterData(String status) {
        filteredResearches.clear();
        if (status.equals("ALL")) {
            filteredResearches.addAll(allResearches);
        } else {
            for (ResearchItem item : allResearches) {
                if (item.getStatus() != null && item.getStatus().equalsIgnoreCase(status)) {
                    filteredResearches.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}