package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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

        // Initialize Input Fields
        final EditText etIdNumber = findViewById(R.id.loginIdNum); // This is your ID input
        final EditText etPassword = findViewById(R.id.loginPassword);
        Button loginBtn = findViewById(R.id.loginBtn);

        // Login Button Logic
        loginBtn.setOnClickListener(v -> {
            String idNumber = etIdNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (idNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(main_login.this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(idNumber, password);
            }
        });

        // Sign Up Link Logic
        TextView tvSignUp = findViewById(R.id.tvRegisterLink);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(main_login.this, main_registration.class);
            startActivity(intent);
        });
    }

    private void loginUser(String idNumber, String password) {
        String URL = "http://10.0.2.2/bluevault/BV_Login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String result = response.trim();

                    if (result.equals("role_admin")) {
                        Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, admin_dashboard.class));
                        finish();
                    }
                    else if (result.equals("role_super_admin")) {
                        Toast.makeText(this, "Super Admin Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, super_admin_dashboard.class));
                        finish();
                    }
                    else if (result.equals("role_user")) {
                        Toast.makeText(this, "Student Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, main_dashboard.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_input", idNumber);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}