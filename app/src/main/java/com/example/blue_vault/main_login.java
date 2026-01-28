package com.example.blue_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class main_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);

        final EditText etIdNum = findViewById(R.id.loginIdNum);
        final EditText etPassword = findViewById(R.id.loginPassword);
        TextView registerBtn = findViewById(R.id.tvRegisterLink);
        Button loginBtn = findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(v ->
                startActivity(new Intent(main_login.this, main_registration.class)));

        loginBtn.setOnClickListener(v -> {
            String id = etIdNum.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (id.isEmpty() || pass.isEmpty()) {
                Toast.makeText(main_login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(id, pass);
            }
        });
    }

    private void loginUser(final String idNumber, String password) {
        String URL = "http://10.0.2.2/bluevault/BV_Login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String result = response.trim();

                    // 1. Handle Student (User) Login
                    if (result.startsWith("role_user")) {
                        String[] parts = result.split("\\|", -1);
                        SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();

                        if (parts.length >= 5) {
                            editor.putString("name", parts[1]);
                            editor.putString("id", parts[2]);
                            editor.putString("email", parts[3]);
                            editor.putString("school",parts[4]);
                        } else {
                            // Fallback if PHP didn't send pipes
                            editor.putString("name", "Student User");
                            editor.putString("id", idNumber);
                            editor.putString("email", "Not Set");
                        }
                        editor.apply();

                        Toast.makeText(main_login.this, "Student Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(main_login.this, main_dashboard.class));
                        finish();
                    }
                    // 2. Handle Admin Login
                    else if (result.equals("role_admin")) {
                        Toast.makeText(main_login.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(main_login.this, admin_dashboard.class));
                        finish();
                    }
                    // 3. Handle Super Admin Login
                    else if (result.equals("role_super_admin")) {
                        Toast.makeText(main_login.this, "Super Admin Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(main_login.this, super_admin_dashboard.class));
                        finish();
                    }
                    // 4. Handle Error Messages from PHP
                    else {
                        Toast.makeText(main_login.this, result, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(main_login.this, "Connection Error: Check XAMPP", Toast.LENGTH_SHORT).show()) {
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