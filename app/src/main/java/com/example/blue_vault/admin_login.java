package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class admin_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        Button adminLoginBtn = findViewById(R.id.adminLoginBtn);
        adminLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(admin_login.this, admin_dashboard.class);
            startActivity(intent);
            finish();
        });
    }
}