package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class nav_menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This activity might not need a setContentView if it's just handling logic,
        // but typically it would be R.layout.nav_menu_layout if you're using it as a standalone activity.
        setContentView(R.layout.nav_menu_layout);

        TextView userEmail = findViewById(R.id.user_email);
        Button btnProfile = findViewById(R.id.nav_profile);
        Button btnAbout = findViewById(R.id.nav_about);
        Button btnSecurity = findViewById(R.id.nav_security);
        Button btnHelp = findViewById(R.id.nav_help);
        Button btnLogout = findViewById(R.id.logout_btn);
        
        if (userEmail != null) {
            userEmail.setText(DataRepository.getInstance().getLoggedInUserEmail());
        }
        
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, profile_view_user.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(this, nav_about_us.class);
            startActivity(intent);
        });

        btnSecurity.setOnClickListener(v -> {
            Intent intent = new Intent(this, nav_pass_change.class);
            startActivity(intent);
        });

        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(this, nav_contact_info.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, main_login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}