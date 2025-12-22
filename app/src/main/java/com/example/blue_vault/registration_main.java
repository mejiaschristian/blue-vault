package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class registration_main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registration_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registration_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvLogIn = findViewById(R.id.tvLoginLink);
        tvLogIn.setText(R.string.already_have_an_account_log_in);

        tvLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(registration_main.this, login_main.class);
            startActivity(intent);
        });
    }
}
