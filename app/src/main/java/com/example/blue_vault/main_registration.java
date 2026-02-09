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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_registration);

        // 1. Initialize XML Views
        final EditText etEmail = findViewById(R.id.regEmail);
        final EditText etLastName = findViewById(R.id.regLastName);
        final EditText etFirstName = findViewById(R.id.regFullName);
        final EditText etIdNum = findViewById(R.id.regIdNum);
        final EditText etPassword = findViewById(R.id.regPassword);
        final AutoCompleteTextView schoolDropdown = findViewById(R.id.regSchoolDropdown);
        final AutoCompleteTextView courseDropdown = findViewById(R.id.regCourseDropdown);
        Button btnRegister = findViewById(R.id.regBtn);
        TextView tvLogin = findViewById(R.id.tvLoginLink);

        // 2. Data Arrays
        String[] schools = {"SASE", "SBMA", "SECA"};
        String[] saseCourses = {"ABComm", "BSPsych", "BPEd"};
        String[] sbmaCourses = {"BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
        String[] secaCourses = {"BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

        List<String> allCoursesList = new ArrayList<>();
        allCoursesList.addAll(Arrays.asList(saseCourses));
        allCoursesList.addAll(Arrays.asList(sbmaCourses));
        allCoursesList.addAll(Arrays.asList(secaCourses));
        String[] allCourses = allCoursesList.toArray(new String[0]);

        // 3. Set up Adapters
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown.setAdapter(schoolAdapter);

        ArrayAdapter<String> allCoursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        courseDropdown.setAdapter(allCoursesAdapter);

        // 4. Handle dynamic Course filtering when School is selected
        schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSchool = (String) parent.getItemAtPosition(position);
            String[] selectedCourses;

            switch (selectedSchool) {
                case "SASE": selectedCourses = saseCourses; break;
                case "SBMA": selectedCourses = sbmaCourses; break;
                case "SECA": selectedCourses = secaCourses; break;
                default: selectedCourses = allCourses; break;
            }

            ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedCourses);
            courseDropdown.setAdapter(courseAdapter);
            courseDropdown.setText("", false);
        });

        // 5. Handle dynamic School selection when Course is selected
        courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCourse = (String) parent.getItemAtPosition(position);
            String foundSchool = "";

            if (Arrays.asList(saseCourses).contains(selectedCourse)) foundSchool = "SASE";
            else if (Arrays.asList(sbmaCourses).contains(selectedCourse)) foundSchool = "SBMA";
            else if (Arrays.asList(secaCourses).contains(selectedCourse)) foundSchool = "SECA";

            if (!foundSchool.isEmpty()) {
                schoolDropdown.setText(foundSchool, false);
            }
        });

        // 6. Register Button Listener
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String first = etFirstName.getText().toString().trim();
            String id = etIdNum.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String school = schoolDropdown.getText().toString().trim();
            String course = courseDropdown.getText().toString().trim();

            // Validate Fields
            if (email.isEmpty() || last.isEmpty() || first.isEmpty() || id.isEmpty() || pass.isEmpty() || school.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@") || !email.contains(".com")) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            } else if (!id.matches("^\\d{4}-\\d{7}$")) {
                // Validation for ID format: XXXX-XXXXXXX (12 chars total, '-' at index 4)
                Toast.makeText(this, "Invalid ID Number (Format should be: 2024-1234567)", Toast.LENGTH_SHORT).show();
            } else if (pass.length() < 8) {
                Toast.makeText(this, "Password must be at least 8-12 characters long", Toast.LENGTH_SHORT).show();
            }

            else {
                // If all validations pass, proceed to registration
                registerUser(email, last, first, id, pass, school, course);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, main_login.class));
            finish();
        });
    }

    private void registerUser(String email, String last, String first, String id, String pass, String school, String course) {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/Regis.php";

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
                error -> Toast.makeText(this, "Connection Error: Check Server", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("lastName", last);
                params.put("firstName", first);
                params.put("idNumber", id);
                params.put("password", pass);
                params.put("school", school);
                params.put("course", course);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
