package com.example.blue_vault;

import android.os.Bundle;
import android.util.Log;
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

public class super_admin_available_researches extends BaseActivity {

    private RecyclerView recyclerView;
    private SuperAdminResearchAdapter adapter;
    private List<ResearchItem> researchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_available_researches);

        // Standard Navigation setup
        setupNavigation();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        researchList = new ArrayList<>();
        // Assuming your adapter handles the display of the status
        adapter = new SuperAdminResearchAdapter(researchList);
        recyclerView.setAdapter(adapter);

        fetchHistoryData();
    }

    private void fetchHistoryData() {
        String URL = "http://10.0.2.2/bluevault/GetUnpublishedAndDeclined.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    Log.d("DEBUG_SUPER", "Response: " + response);

                    researchList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            // Ensure the 'school' key from PHP matches obj.getString("school")
                            researchList.add(new ResearchItem(
                                    obj.getInt("rsid"),
                                    obj.getString("title"),
                                    obj.getString("author"),
                                    obj.getString("school"), // FETCHING SCHOOL HERE
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
                        Log.e("DEBUG_SUPER", "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
}