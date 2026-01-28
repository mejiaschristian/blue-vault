package com.example.blue_vault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class main_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_registration);

        // Link Java variables to XML IDs
        final EditText etEmail = findViewById(R.id.regEmail);
        final EditText etLastName = findViewById(R.id.regLastName);
        final EditText etFirstName = findViewById(R.id.regFullName);
        final EditText etIdNum = findViewById(R.id.regIdNum);
        final EditText etPassword = findViewById(R.id.regPassword);
        Button btnRegister = findViewById(R.id.regBtn);
        TextView tvLogin = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String first = etFirstName.getText().toString().trim();
            String id = etIdNum.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || last.isEmpty() || first.isEmpty() || id.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, last, first, id, pass);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(main_registration.this, main_login.class));
            finish();
        });

        AutoCompleteTextView schoolDropdown = findViewById(R.id.regSchoolDropdown);
        String[] schools = {"SASE", "SBMA", "SECA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown.setAdapter(adapter);

    }

    private void registerUser(String email, String last, String first, String id, String pass) {
        String URL = "http://10.0.2.2/bluevault/Regis.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, main_login.class));
                        finish();
                    } else {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("lastName", last);
                params.put("firstName", first);
                params.put("idNumber", id);
                params.put("password", pass);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}