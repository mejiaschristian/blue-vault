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

public class super_admin_registered_teachers extends BaseActivity {

    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_registered_teachers);

        // Initialize common navigation logic
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

        // Setup Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTeacherList);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            List<TeacherItem> teacherList = new ArrayList<>();
            teacherList.add(new TeacherItem("Dr. Ricardo Dalisay", "T-2024-001", "SECA"));
            teacherList.add(new TeacherItem("Prof. Cardo Provinci", "T-2024-002", "SASE"));
            teacherList.add(new TeacherItem("Engr. Neri Naig", "T-2024-003", "SECA"));
            teacherList.add(new TeacherItem("Ms. Sarah Geronimo", "T-2024-004", "SBMA"));
            teacherList.add(new TeacherItem("Dr. Jose Mari Chan", "T-2024-005", "SASE"));

            adapter = new TeacherAdapter(teacherList);
            recyclerView.setAdapter(adapter);
        }

        // Setup Filter Dropdown
        String[] depts = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, depts);
        AutoCompleteTextView filterDropdown = findViewById(R.id.filter_dropdown_text);
        
        if (filterDropdown != null) {
            filterDropdown.setAdapter(deptAdapter);
            filterDropdown.setText(depts[0], false); // Set "ALL" as default
            
            filterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                String selectedDept = (String) parent.getItemAtPosition(position);
                if (adapter != null) {
                    adapter.filter(selectedDept);
                }
            });
        }
    }
}