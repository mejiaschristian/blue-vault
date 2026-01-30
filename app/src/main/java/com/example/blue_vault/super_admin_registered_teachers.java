package com.example.blue_vault;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class super_admin_registered_teachers extends BaseActivity {

    private TeacherAdapter adapter;
    private List<TeacherItem> allTeachers = new ArrayList<>(); // Master list from DB
    private List<TeacherItem> filteredTeachers = new ArrayList<>(); // List shown in UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_registered_teachers);

        // Initialize common navigation logic
        setupNavigation();
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);

        // Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentName = sp.getString("name", "N/A");
        String currentId = sp.getString("id", "N/A");
        String currentEmail = sp.getString("email", "N/A");

        if (currentName.equals("N/A") || currentId.equals("N/A") || currentEmail.equals("N/A")) {
            if (navResearches != null) navResearches.setVisibility(GONE);
            if (navSecurity != null) navSecurity.setVisibility(GONE);
        }

        // Setup Back Button
        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTeacherList);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Bind to filteredTeachers so UI updates correctly
            adapter = new TeacherAdapter(filteredTeachers);
            recyclerView.setAdapter(adapter);
        }

        // Fetch Real Data from MySQL
        fetchTeachersFromMySQL();
    }

    private void fetchTeachersFromMySQL() {
        // Pointing to the specific PHP file created above
        String URL = "http://10.0.2.2/bluevault/TeacherFetch.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    Log.d("DEBUG_TEACHERS", "Response: " + response);
                    allTeachers.clear();
                    filteredTeachers.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            // Ensure the 'name' and 'id' keys match PHP output
                            TeacherItem item = new TeacherItem(
                                    obj.getString("name"),
                                    obj.getString("id")
                            );
                            allTeachers.add(item);
                        }

                        // IMPORTANT: Sync filtered list with fetched data and refresh UI
                        filteredTeachers.addAll(allTeachers);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        Log.e("VOLLEY_ERROR", "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Data parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Network Error: " + error.toString());
                    Toast.makeText(this, "Connection Error. Check XAMPP.", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
}