package com.example.blue_vault;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profile_view_user extends BaseActivity {

    private List<ResearchItem> allUserResearches = new ArrayList<>();
    private List<ResearchItem> displayedResearches = new ArrayList<>();

    private ResearchAdapter adapter;
    private RecyclerView recyclerView;
    private TextView recyclerLabel;
    private AutoCompleteTextView statusFilterDropdown;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentStatusFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_user);

        setupNavigation();

        // 1. Initialize UI Elements
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvId = findViewById(R.id.tvProfileId);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        recyclerLabel = findViewById(R.id.recyclerLabel);
        View researchesHeader = findViewById(R.id.researches_header);
        Button addResBtn = findViewById(R.id.addResBtn);
        Button navResearches = findViewById(R.id.nav_researches);
        Button navSecurity = findViewById(R.id.nav_security);
        recyclerView = findViewById(R.id.recyclerView);
        statusFilterDropdown = findViewById(R.id.status_filter_dropdown);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // 2. Load data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "N/A");
        String id = sp.getString("id", "N/A");
        String email = sp.getString("email", "N/A");
        String school = sp.getString("school", "");

        tvName.setText(name);
        tvId.setText(id);
        tvEmail.setText(email);

        // 3. Visibility logic
        if (id.equals("N/A") || school.isEmpty()) {
            if (recyclerLabel != null) recyclerLabel.setVisibility(GONE);
            if (recyclerView != null) recyclerView.setVisibility(GONE);
            if (researchesHeader != null) researchesHeader.setVisibility(GONE);
            if (addResBtn != null) addResBtn.setVisibility(GONE);
            navResearches.setVisibility(GONE);
            navSecurity.setVisibility(GONE);
        } else {
            if (recyclerLabel != null) recyclerLabel.setVisibility(VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(VISIBLE);
            if (researchesHeader != null) researchesHeader.setVisibility(VISIBLE);
            if (addResBtn != null) addResBtn.setVisibility(VISIBLE);
            navResearches.setVisibility(VISIBLE);
            navSecurity.setVisibility(VISIBLE);
        }

        // 4. Setup RecyclerView
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ResearchAdapter(displayedResearches, true);
            recyclerView.setAdapter(adapter);
        }

        setupFilterDropdown();

        if (!id.equals("N/A") && !school.isEmpty()) {
            swipeRefreshLayout.setOnRefreshListener(() -> fetchUserResearchFromMySQL(id, school));
            fetchUserResearchFromMySQL(id, school); // Initial load
        }

        Button backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) backBtn.setOnClickListener(v -> onBackPressed());

        if (addResBtn != null) {
            addResBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_research.class);
                intent.putExtra("user_school", school);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String id = sp.getString("id", "N/A");
        String school = sp.getString("school", "");
        if (!id.equals("N/A") && !school.isEmpty()) {
            // Force a refresh whenever returning to this screen (e.g., after rating)
            fetchUserResearchFromMySQL(id, school);
        }
    }

    private void setupFilterDropdown() {
        String[] statuses = {"All", "Approved", "Declined", "Pending", "Published"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses);
        if (statusFilterDropdown != null) {
            statusFilterDropdown.setAdapter(filterAdapter);
            statusFilterDropdown.setText(statuses[0], false);
            statusFilterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentStatusFilter = (String) parent.getItemAtPosition(position);
                applyStatusFilter();
            });
        }
    }

    private void applyStatusFilter() {
        displayedResearches.clear();
        for (ResearchItem item : allUserResearches) {
            int status = item.getStatus();
            boolean matches = false;
            if (currentStatusFilter.equals("All")) matches = true;
            else if (currentStatusFilter.equals("Approved") && status == 1) matches = true;
            else if (currentStatusFilter.equals("Declined") && status == 0) matches = true;
            else if (currentStatusFilter.equals("Published") && status == 2) matches = true;
            else if (currentStatusFilter.equals("Pending") && status == 3) matches = true;

            if (matches) displayedResearches.add(item);
        }
        if (displayedResearches.isEmpty()) {
            recyclerLabel.setText("No " + currentStatusFilter + " researches.");
        } else {
            recyclerLabel.setText("YOUR RESEARCHES:");
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchUserResearchFromMySQL(String id, String school) {
        String ip = DataRepository.getInstance().getIpAddress();
        // Added a timestamp parameter to bypass potential Volley/Server caching
        String URL = "http://" + ip + "/bluevault/GetUserResearch.php?id_number=" + id + "&school=" + school + "&t=" + System.currentTimeMillis();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    allUserResearches.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            int itemStatus = obj.optInt("status", 3);
                            // optDouble handles both numbers and strings returned from PHP (SQL AVG returns strings often)
                            float rating = (float) obj.optDouble("rating", 0.0);

                            // Status 2 = Published. Stars only show for published items.
                            boolean isPublished = (itemStatus == 2);

                            allUserResearches.add(new ResearchItem(
                                    obj.getInt("rsid"),
                                    obj.getString("idnumber"),
                                    obj.getString("title"),
                                    obj.getString("author"),
                                    obj.getString("school"),
                                    obj.getString("course"),
                                    obj.getString("date"),
                                    itemStatus,
                                    obj.getString("abstract"),
                                    obj.getString("tags"),
                                    obj.getString("doi"),
                                    rating, obj.getString("remarks"),
                                    isPublished
                            ));
                        }
                        applyStatusFilter();
                    } catch (JSONException e) {
                        Log.e("DEBUG_VOLLEY", "JSON Error: " + e.getMessage());
                    } finally {
                        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    }
                },
                error -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }
        );

        // Set shouldCache to false to ensure we get the latest ratings from the database
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        Volley.newRequestQueue(this).add(stringRequest);
    }
}