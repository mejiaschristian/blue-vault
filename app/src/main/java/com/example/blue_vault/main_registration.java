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

        // Link Java variables to XML IDs
        final EditText etEmail = findViewById(R.id.regEmail);
        final EditText etLastName = findViewById(R.id.regLastName);
        final EditText etFirstName = findViewById(R.id.regFullName);
        final EditText etIdNum = findViewById(R.id.regIdNum);
        final EditText etPassword = findViewById(R.id.regPassword);
        final AutoCompleteTextView schoolDropdown = findViewById(R.id.regSchoolDropdown);
        final AutoCompleteTextView courseDropdown = findViewById(R.id.regCourseDropdown);
        Button btnRegister = findViewById(R.id.regBtn);
        TextView tvLogin = findViewById(R.id.tvLoginLink);

        String[] schools = {"SASE", "SBMA", "SECA"};
        String[] saseCourses = {"ABComm", "BSPsych", "BPEd"};
        String[] sbmaCourses = {"BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
        String[] secaCourses = {"BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

        // Combine all courses for initial list
        List<String> allCoursesList = new ArrayList<>();
        allCoursesList.addAll(Arrays.asList(saseCourses));
        allCoursesList.addAll(Arrays.asList(sbmaCourses));
        allCoursesList.addAll(Arrays.asList(secaCourses));
        String[] allCourses = allCoursesList.toArray(new String[0]);

        // Set up School Dropdown
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        schoolDropdown.setAdapter(schoolAdapter);

        // Set up Course Dropdown initially with all courses
        ArrayAdapter<String> allCoursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        courseDropdown.setAdapter(allCoursesAdapter);

        // Handle dynamic Course Dropdown based on School selection
        schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSchool = (String) parent.getItemAtPosition(position);
            String[] selectedCourses;

            switch (selectedSchool) {
                case "SASE":
                    selectedCourses = saseCourses;
                    break;
                case "SBMA":
                    selectedCourses = sbmaCourses;
                    break;
                case "SECA":
                    selectedCourses = secaCourses;
                    break;
                default:
                    selectedCourses = allCourses;
                    break;
            }

            ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(main_registration.this, android.R.layout.simple_list_item_1, selectedCourses);
            courseDropdown.setAdapter(courseAdapter);
            courseDropdown.setText("", false); // Clear course selection when school changes
        });

        // Handle dynamic School selection based on Course selection
        courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCourse = (String) parent.getItemAtPosition(position);
            String foundSchool = "";

            if (Arrays.asList(saseCourses).contains(selectedCourse)) {
                foundSchool = "SASE";
            } else if (Arrays.asList(sbmaCourses).contains(selectedCourse)) {
                foundSchool = "SBMA";
            } else if (Arrays.asList(secaCourses).contains(selectedCourse)) {
                foundSchool = "SECA";
            }

            if (!foundSchool.isEmpty()) {
                schoolDropdown.setText(foundSchool, false);
                
                // Also update the course adapter to only show relevant courses for the auto-selected school
                String[] coursesForSchool;
                if (foundSchool.equals("SASE")) coursesForSchool = saseCourses;
                else if (foundSchool.equals("SBMA")) coursesForSchool = sbmaCourses;
                else coursesForSchool = secaCourses;

                ArrayAdapter<String> filteredAdapter = new ArrayAdapter<>(main_registration.this, android.R.layout.simple_list_item_1, coursesForSchool);
                courseDropdown.setAdapter(filteredAdapter);
                // Maintain the selected text
                courseDropdown.setText(selectedCourse, false);
            }
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String first = etFirstName.getText().toString().trim();
            String id = etIdNum.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String school = schoolDropdown.getText().toString().trim();
            String course = courseDropdown.getText().toString().trim();

            if (email.isEmpty() || last.isEmpty() || first.isEmpty() || id.isEmpty() || pass.isEmpty() || school.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, last, first, id, pass, school, course);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(main_registration.this, main_login.class));
            finish();
        });
    }

    private void registerUser(String email, String last, String first, String id, String pass, String school, String course) {
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
                error -> Toast.makeText(this, "Connection Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
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