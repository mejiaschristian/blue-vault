package com.example.blue_vault;

import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class admin_pending_reqs extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pending_reqs);
        setupNavigation();

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for pending requests
        List<ResearchItem> pendingList = new ArrayList<>();
        pendingList.add(new ResearchItem("Pending Research 1", "Juan Dela Cruz", "SECA", "BSIT", "Nov 1, 2024"));
        pendingList.add(new ResearchItem("Pending Research 2", "Maria Clara", "SASE", "BSCS", "Nov 2, 2024"));

        // Use the new AdminResearchAdapter which is specifically for this page
        // and uses the rc_admin_item_research layout (which contains the "View Research" button).
        AdminResearchAdapter adapter = new AdminResearchAdapter(pendingList);
        recyclerView.setAdapter(adapter);
    }
}
