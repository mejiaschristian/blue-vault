package com.example.blue_vault;

import java.util.ArrayList;
import java.util.List;

/**
 * Central repository for all data.
 * HARDCODED DATA REMOVED: Now using MySQL database via Volley.
 */
public class DataRepository {

    private static DataRepository instance;
    private List<ResearchItem> allResearches;

    // Status Constants: 0=Declined, 1=Approved, 3=Pending
    public static final int STATUS_DECLINED = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_PENDING = 3;

    private DataRepository() {
        // We leave this empty because data is now fetched from the Database
        allResearches = new ArrayList<>();
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    // You can keep these as fallbacks if needed,
    // but ideally, these should also be replaced with SharedPreferences or DB calls.
    public StudentItem getLoggedInUser() {
        return new StudentItem("User", "0000", "N/A");
    }

    public String getLoggedInUserEmail() {
        return "user@example.com";
    }

    public List<ResearchItem> getAllResearches() {
        return allResearches;
    }

    // These methods can remain as helper filters if you ever pass
    // a full list to the repository, but they won't cause errors anymore.
    public List<ResearchItem> getPendingResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getStatus() == STATUS_PENDING) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    // ... rest of the filter methods (getApproved, getDeclined, etc.) can stay
    // as they are logically fine once the constructor errors are gone.
}