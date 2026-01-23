package com.example.blue_vault;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class admin_registered_students extends BaseActivity {

    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registered_students);

        // Initialize common navigation logic (including goto_menu)
        setupNavigation();

        // Setup Back Button
        // Fixed: The crash occurred because R.id.backBtn is a Button in XML but was cast to ImageView in Java.
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvStudentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<StudentItem> studentList = new ArrayList<>();
        studentList.add(new StudentItem("Alinsurin, Emmanuel C.", "2024-1234567", "SECA"));
        studentList.add(new StudentItem("Manding, Sean M.", "2024-2468101", "SECA"));
        studentList.add(new StudentItem("Udarbe, Lebron James S.", "2024-3691113", "SASE"));
        studentList.add(new StudentItem("Lagaras, Ryoji B.", "2024-4812162", "SBMA"));
        studentList.add(new StudentItem("Tolentino, Aristotle W.", "2024-5101520", "SECA"));
        studentList.add(new StudentItem("Mejias, Christian Lloyd J.", "2024-6121824", "SASE"));
        studentList.add(new StudentItem("Publico, Adlae Nicolaus T.", "2024-7142128", "SBMA"));

        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        // Setup Filter Dropdown
        String[] depts = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, depts);
        AutoCompleteTextView filterDropdown = findViewById(R.id.filter_dropdown_text);
        
        if (filterDropdown != null) {
            filterDropdown.setAdapter(deptAdapter);
            filterDropdown.setText(depts[0], false); // Set "ALL" as default
            
            filterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                String selectedDept = (String) parent.getItemAtPosition(position);
                adapter.filter(selectedDept);
            });
        }
    }
}