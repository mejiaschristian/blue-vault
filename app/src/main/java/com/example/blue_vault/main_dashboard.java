package com.example.blue_vault;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class main_dashboard extends BaseActivity {

    private List<ResearchItem> allResearches = new ArrayList<>();
    private List<ResearchItem> filteredResearches = new ArrayList<>();
    private ResearchAdapter adapter;
    private AutoCompleteTextView schoolDropdown, courseDropdown, ratingDropdown;
    private TextInputEditText searchInput;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String currentSchoolFilter = "ALL";
    private String currentCourseFilter = "ALL";
    private String currentRatingFilter = "ALL";

    private final String[] allCourses = {"ALL", "ABComm", "BSPsych", "BPEd", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};
    private final String[] saseCourses = {"ALL", "ABComm", "BSPsych", "BPEd"};
    private final String[] sbmaCourses = {"ALL", "BSA", "BSMA", "BSBA-MM", "BSBA-FM", "BSBA-HRM", "BSHM", "BSTM"};
    private final String[] secaCourses = {"ALL", "BSIT", "BSCS", "BSCE", "BSCpE", "BSArch"};

    // Rating filter options
    private final String[] ratingOptions = {"ALL", "5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        setupNavigation();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchApprovedResearches());

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResearchAdapter(filteredResearches, false);
        recyclerView.setAdapter(adapter);

        // Setup Search Input
        searchInput = findViewById(R.id.search_input);
        if (searchInput != null) {
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    applyFilters(s.toString(), currentSchoolFilter, currentCourseFilter, currentRatingFilter);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Setup Dropdowns
        setupFilterDropdowns();
    }

    private void setupFilterDropdowns() {
        schoolDropdown = findViewById(R.id.school_dropdown);
        courseDropdown = findViewById(R.id.course_dropdown);
        ratingDropdown = findViewById(R.id.rating_dropdown);

        // 1. School Filter
        String[] schools = {"ALL", "SECA", "SASE", "SBMA"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        if (schoolDropdown != null) {
            schoolDropdown.setAdapter(schoolAdapter);
            schoolDropdown.setText(schools[0], false);
            schoolDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentSchoolFilter = (String) parent.getItemAtPosition(position);
                updateCourseDropdown(currentSchoolFilter);
                currentCourseFilter = "ALL";
                courseDropdown.setText("ALL", false);
                applyFilters(searchInput.getText().toString(), currentSchoolFilter, currentCourseFilter, currentRatingFilter);
            });
        }

        // 2. Course Filter
        updateCourseDropdown("ALL");
        if (courseDropdown != null) {
            courseDropdown.setText("ALL", false);
            courseDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentCourseFilter = (String) parent.getItemAtPosition(position);
                applyFilters(searchInput.getText().toString(), currentSchoolFilter, currentCourseFilter, currentRatingFilter);
            });
        }

        // 3. Rating Filter
        ArrayAdapter<String> ratingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ratingOptions);
        if (ratingDropdown != null) {
            ratingDropdown.setAdapter(ratingAdapter);
            ratingDropdown.setText(ratingOptions[0], false);
            ratingDropdown.setOnItemClickListener((parent, view, position, id) -> {
                currentRatingFilter = (String) parent.getItemAtPosition(position);
                applyFilters(searchInput.getText().toString(), currentSchoolFilter, currentCourseFilter, currentRatingFilter);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchApprovedResearches();
    }

    private void fetchApprovedResearches() {
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://" + ip + "/bluevault/GetApprovedResearch.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    allResearches.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String school = obj.has("department") ? obj.getString("department") : obj.getString("school");

                            allResearches.add(new ResearchItem(
                                    obj.getInt("rsid"),
                                    obj.optString("idnumber", ""),
                                    obj.getString("title"),
                                    obj.getString("author"),
                                    school,
                                    obj.getString("course"),
                                    obj.getString("date"),
                                    obj.getInt("status"),
                                    obj.getString("abstract"),
                                    obj.getString("tags"),
                                    obj.getString("doi"),
                                    (float) obj.optDouble("rating", 0.0),
                                    obj.optString("remarks", ""),
                                    true
                            ));
                        }
                        applyFilters(searchInput.getText().toString(), currentSchoolFilter, currentCourseFilter, currentRatingFilter);
                    } catch (JSONException e) {
                        Log.e("REFRESH_CHECK", "JSON Error: " + e.getMessage());
                    } finally {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                error -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                }
        );
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updateCourseDropdown(String school) {
        String[] courses;
        switch (school) {
            case "SASE": courses = saseCourses; break;
            case "SBMA": courses = sbmaCourses; break;
            case "SECA": courses = secaCourses; break;
            default: courses = allCourses; break;
        }
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        if (courseDropdown != null) {
            courseDropdown.setAdapter(courseAdapter);
        }
    }

    private void applyFilters(String query, String school, String course, String rating) {
        filteredResearches.clear();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (ResearchItem item : allResearches) {
            // Check School & Course
            boolean matchesSchool = school.equals("ALL") || (item.getSchool() != null && item.getSchool().equalsIgnoreCase(school));
            boolean matchesCourse = course.equals("ALL") || (item.getCourse() != null && item.getCourse().equalsIgnoreCase(course));

            // Check Rating Logic
            boolean matchesRating = rating.equals("ALL");
            if (!matchesRating) {
                float itemRating = item.getRating();
                if (rating.equals("5 Stars")) matchesRating = (itemRating >= 4.5);
                else if (rating.equals("4 Stars")) matchesRating = (itemRating >= 3.5 && itemRating < 4.5);
                else if (rating.equals("3 Stars")) matchesRating = (itemRating >= 2.5 && itemRating < 3.5);
                else if (rating.equals("2 Stars")) matchesRating = (itemRating >= 1.5 && itemRating < 2.5);
                else if (rating.equals("1 Star")) matchesRating = (itemRating >= 0.5 && itemRating < 1.5);
            }

            // Check Search Query
            boolean matchesQuery = lowerCaseQuery.isEmpty() ||
                    (item.getTitle() != null && item.getTitle().toLowerCase().contains(lowerCaseQuery)) ||
                    (item.getAuthor() != null && item.getAuthor().toLowerCase().contains(lowerCaseQuery)) ||
                    (item.getTags() != null && item.getTags().toLowerCase().contains(lowerCaseQuery));

            if (matchesSchool && matchesCourse && matchesQuery && matchesRating) {
                filteredResearches.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}