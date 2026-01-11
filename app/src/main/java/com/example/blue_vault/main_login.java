package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class main_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(main_login.this, main_dashboard.class);
            startActivity(intent);
        });

        TextView tvSignUp = findViewById(R.id.tvRegisterLink);
        tvSignUp.setText(R.string.don_t_have_an_account_yet_sign_up_now);

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(main_login.this, main_registration.class);
            startActivity(intent);
        });
    }
}