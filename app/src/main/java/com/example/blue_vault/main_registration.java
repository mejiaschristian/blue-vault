package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class main_registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registration_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvLogIn = findViewById(R.id.tvLoginLink);
        tvLogIn.setText(R.string.already_have_an_account_log_in);

        tvLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(main_registration.this, main_login.class);
            startActivity(intent);
        });
    }
}
