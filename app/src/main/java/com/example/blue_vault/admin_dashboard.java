package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class admin_dashboard extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        
        // Use the base activity to set up navigation if needed,
        // or customize the dashboard specific logic here.
        setupNavigation();
        Button studentList = findViewById(R.id.adminStdListBtn);
        Button pendingReq = findViewById(R.id.adminPendReq);
        studentList.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_registered_students.class));
        });
        pendingReq.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_pending_reqs.class));
        });

    }
}