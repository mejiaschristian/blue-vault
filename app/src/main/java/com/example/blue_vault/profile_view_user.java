package com.example.blue_vault;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<ResearchItem> userResearches;
    private ResearchAdapter adapter;
    private RecyclerView recyclerView;
    private TextView recyclerLabel;

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
        Button addResBtn = findViewById(R.id.addResBtn);
        recyclerView = findViewById(R.id.recyclerView);

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
        } else {
            if (recyclerLabel != null) recyclerLabel.setVisibility(VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(VISIBLE);
        }

        // 4. Setup RecyclerView
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            userResearches = new ArrayList<>();
            adapter = new ResearchAdapter(userResearches, true);
            recyclerView.setAdapter(adapter);
        }

        // 5. Fetch only this user's research
        if (!id.equals("N/A") && !school.isEmpty()) {
            fetchUserResearchFromMySQL(id, school);
        }

        // 6. Buttons
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

    private void fetchUserResearchFromMySQL(String id, String school) {
        // Build the URL
        String URL = "http://10.0.2.2/bluevault/GetUserResearch.php?id_number=" + id + "&school=" + school.toLowerCase();

        Log.d("DEBUG_VOLLEY", "Requesting: " + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    // This log is crucial! If you see <br /> here, read the text next to it.
                    Log.d("DEBUG_VOLLEY", "Raw Server Response: " + response);

                    userResearches.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        if (jsonArray.length() == 0) {
                            if (recyclerLabel != null) recyclerLabel.setText("No researches uploaded yet.");
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            userResearches.add(new ResearchItem(
                                    obj.getInt("rsid"),
                                    obj.getString("title"),
                                    obj.getString("author"),
                                    obj.getString("school"),
                                    obj.getString("course"),
                                    obj.getString("date"),
                                    obj.getInt("status"),
                                    obj.getString("abstract"),
                                    obj.getString("tags"),
                                    obj.getString("doi"),
                                    0.0f,
                                    false
                            ));
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("DEBUG_VOLLEY", "JSON Error: " + e.getMessage());
                        // This toast triggers because PHP sent back an HTML Error message
                        Toast.makeText(this, "PHP Error detected. Check Logcat for details.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("DEBUG_VOLLEY", "Network Error: " + error.toString());
                    Toast.makeText(this, "Network error. Check XAMPP.", Toast.LENGTH_SHORT).show();
                }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(stringRequest);
    }
}