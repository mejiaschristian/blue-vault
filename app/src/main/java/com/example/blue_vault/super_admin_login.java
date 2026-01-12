package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class super_admin_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_login);

        Button superAdminLoginBtn = findViewById(R.id.superAdminLoginBtn);
        superAdminLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(super_admin_login.this, super_admin_dashboard.class);
            startActivity(intent);
            finish();
        });
    }
}