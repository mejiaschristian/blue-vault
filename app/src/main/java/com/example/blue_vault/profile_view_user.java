package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class profile_view_user extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);

        // This replaces all manual drawer logic
        setupNavigation();

        Button btnUpload = findViewById(R.id.addResBtn);
        if (btnUpload != null) {
            btnUpload.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                startActivity(intent);
            });
        }

        // RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ResearchItem> researchList = new ArrayList<>();
        researchList.add(new ResearchItem("My Research 1", "Juan Dela Cruz", "SECA", "BSIT", "Oct 1, 2024"));
        researchList.add(new ResearchItem("My Research 2", "Juan Dela Cruz", "SECA", "CS","Oct 10, 2024"));

        ResearchAdapter adapter = new ResearchAdapter(researchList);
        recyclerView.setAdapter(adapter);
    }
}