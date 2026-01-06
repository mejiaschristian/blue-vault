package com.example.blue_vault;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ResearchItem> researchList = new ArrayList<>();
        researchList.add(new ResearchItem("Research ko to!!!!", "Sean Manding", "BSIT", "October 24, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 2", "Maria Clara", "BSCS", "October 25, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 3", "Jose Rizal", "BSIS", "October 26, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 4", "Maria Clara", "BSCS", "October 25, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 5", "Jose Rizal", "BSIS", "October 26, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 6", "Maria Clara", "BSCS", "October 25, 2024"));
        researchList.add(new ResearchItem("Sample Research Title 7", "Jose Rizal", "BSIS", "October 26, 2024"));

        ResearchAdapter adapter = new ResearchAdapter(researchList);
        recyclerView.setAdapter(adapter);
    }
}