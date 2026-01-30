package com.example.blue_vault;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class admin_registered_students extends BaseActivity {

    private StudentAdapter adapter;
    private List<StudentItem> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registered_students);

        setupNavigation();
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);
        navResearches.setVisibility(GONE);
        navSecurity.setVisibility(GONE);

        // Setup Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvStudentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        // Fetch data from MySQL
        fetchStudentsFromServer();

        // Setup Filter Dropdown
        String[] depts = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, depts);
        AutoCompleteTextView filterDropdown = findViewById(R.id.filter_dropdown_text);

        if (filterDropdown != null) {
            filterDropdown.setAdapter(deptAdapter);
            filterDropdown.setText(depts[0], false);

            filterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                String selectedDept = (String) parent.getItemAtPosition(position);
                adapter.filter(selectedDept);
            });
        }
    }

    private void fetchStudentsFromServer() {
        // 1. Corrected filename as per your request
        String URL = "http://10.0.2.2/bluevault/StudentFetch.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    // Clear the list before adding new data from MySQL
                    studentList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            // Adding data from the registration table
                            studentList.add(new StudentItem(
                                    obj.getString("name"),
                                    obj.getString("id"),
                                    obj.getString("dept")
                            ));
                        }

                        // CRITICAL: Update the Adapter's internal copy so "ALL" filter works
                        adapter.updateOriginalList(studentList);

                        // Refresh the UI
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}