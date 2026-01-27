package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        final EditText etIdNumber = findViewById(R.id.loginIdNum);
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
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/bluevault/BV_Login.php"); // emulator
                // URL url = new URL("http://192.168.100.4/bluevault/BV_Login.php"); // phone

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                String postData = "id_input=" + URLEncoder.encode(idNumber, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                    br.close();

                    String result = sb.toString().trim();

                    runOnUiThread(() -> {
                        switch (result) {
                            case "role_admin":
                                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, admin_dashboard.class));
                                finish();
                                break;
                            case "role_super_admin":
                                Toast.makeText(this, "Super Admin Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, super_admin_dashboard.class));
                                finish();
                                break;
                            case "role_user":
                                Toast.makeText(this, "Student Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, main_dashboard.class));
                                finish();
                                break;
                            default:
                                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show()
                    );
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
