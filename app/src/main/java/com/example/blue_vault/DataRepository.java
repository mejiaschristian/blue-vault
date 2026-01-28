package com.example.blue_vault;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Central repository for all data. 
 * Placeholders are used here, which can be easily replaced with database (Firebase/Room) calls later.
 */
public class DataRepository {

    private static DataRepository instance;
    private List<ResearchItem> allResearches;

    // Status Constants: 0=Declined, 1=Approved, 3=Pending
    public static final int STATUS_DECLINED = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_PENDING = 3;

    private DataRepository() {
        allResearches = new ArrayList<>();
        // Initialize with placeholder data using int status
        
        // Approved and Published
        allResearches.add(new ResearchItem(
            "Automated Library Vault System using QR Code", 
            "Udarbe, LeBron Jamez Z.\nAlinsurin, Emmanuel C.", 
            "SECA", 
            "BSIT", 
            "Oct 24, 2024", 
            STATUS_APPROVED,
            "This research focuses on developing a secure and automated way to manage library resources using QR code technology...",
            "Automation, QR Code, Library, Security",
            "https://doi.org/10.1234/bluevault.2024",
            4.2f,
            true // Published
        ));
        
        allResearches.add(new ResearchItem(
                "Amogus Skibiddy Toilet",
                "John Galli A. Mendoza",
                "SECA",
                "BSIT",
                "March 3, 2024",
                STATUS_APPROVED,
                "A deep dive into the cultural phenomenon of Skibidi Toilet...",
                "toilet, amogus",
                "https://doi.org/10.1234/bluevault.2024",
                2.2f,
                true
        ));
        
        allResearches.add(new ResearchItem(
            "AI in Modern Education", 
            "Emmanuel Alinsurin", 
            "SECA", 
            "BSCS", 
            "Oct 25, 2024", 
            STATUS_APPROVED,
            "Exploring the impact of Artificial Intelligence on student engagement and academic performance.",
            "AI, Education, Technology",
            "https://doi.org/10.1234/bluevault.ai.2024",
            5.0f,
            true // Published
        ));

        // Approved but NOT yet Published
        allResearches.add(new ResearchItem(
            "Smart City Traffic Management", 
            "Sean Manding", 
            "SECA",
            "BSIT", 
            "Oct 26, 2024", 
            STATUS_APPROVED,
            "Utilizing IoT and real-time data analytics to optimize traffic flow in urban areas.",
            "IoT, Smart City, Analytics",
            "https://doi.org/10.1234/bluevault.city.2024",
            3.2f,
            false // Not Published
        ));

        // Declined
        allResearches.add(new ResearchItem(
            "Impact of Gaming on Sleep", 
            "Ryoji Lagaras", 
            "SECA",
            "BSCS",
            "Oct 27, 2024", 
            STATUS_DECLINED,
            "A study on how late-night gaming affects student sleep patterns.",
            "Gaming, Health, Students",
            "https://doi.org/10.1234/bluevault.city.2024",
            1.2f,
            false
        ));

        allResearches.add(new ResearchItem(
                "Basic Study",
                "Rayuji Lagaras",
                "SECA",
                "BSIT-MWA",
                "Sept 23, 2024",
                STATUS_PENDING,
                "A study on insomnia.",
                "Students, NUD",
                "https://doi.org/10.1234/bluevault.city.2024",
                0f,
                false
        ));

        allResearches.add(new ResearchItem(
                "Psychological Factors of Insomnia",
                "Galliard",
                "SASE",
                "BPEd",
                "Sept 23, 2024",
                STATUS_DECLINED,
                "A study on insomnia.",
                "Sleep, Health, Students",
                "https://doi.org/10.1234/bluevault.city.2024",
                1.2f,
                false
        ));

        // Pending
        allResearches.add(new ResearchItem(
            "Blockchain for Secure Voting", 
            "Mejias Christiano", 
            "SECA", 
            "BSIT",
            "Oct 28, 2024", 
            STATUS_PENDING,
            "Implementing a blockchain-based voting system for transparency.",
            "Blockchain, Voting, Security",
            "https://doi.org/10.1234/bluevault.city.2024",
            0.0f,
            false
        ));

        // User's specific researches (Migrated from profile_view_user)
        allResearches.add(new ResearchItem(
                "My Research 1",
                "Juan Dela Cruz",
                "SECA",
                "BSIT",
                "Oct 1, 2024",
                STATUS_APPROVED,
                "Detailed abstract for My Research 1...",
                "User, Research, Skills",
                "",
                2.3f,
                true
        ));

        allResearches.add(new ResearchItem(
                "My Research 2",
                "Juan Dela Cruz",
                "SECA",
                "BSIT",
                "Oct 3, 2024",
                STATUS_DECLINED,
                "Detailed abstract for My Research 2...",
                "User, Research, Skills",
                "",
                0.0f,
                false
        ));

        allResearches.add(new ResearchItem(
                "My Research 3",
                "Juan Dela Cruz",
                "SECA",
                "BSIT",
                "Oct 5, 2024",
                STATUS_PENDING,
                "Detailed abstract for My Research 3...",
                "User, Research, Skills",
                "",
                0.0f,
                false
        ));
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    /**
     * Simulation of getting current logged-in user info
     */
    public StudentItem getLoggedInUser() {
        return new StudentItem("Juan Dela Cruz", "2024-3691113", "SECA");
    }

    public String getLoggedInUserEmail() {
        return "test@mail.com";
    }

    public List<ResearchItem> getAllResearches() {
        return allResearches;
    }

    public List<ResearchItem> getPublishedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.isPublished()) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getUnpublishedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getStatus() == STATUS_APPROVED && !item.isPublished()) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getApprovedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getStatus() == STATUS_APPROVED) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getDeclinedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getStatus() == STATUS_DECLINED) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getPendingResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getStatus() == STATUS_PENDING) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getUserResearches(String authorName) {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if (item.getAuthor().contains(authorName)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<StudentItem> getStudents() {
        List<StudentItem> items = new ArrayList<>();
        items.add(new StudentItem("Alinsurin, Emmanuel C.", "2024-1234567", "SECA"));
        items.add(new StudentItem("Manding, Sean M.", "2024-2468101", "SECA"));
        items.add(new StudentItem("Udarbe, LeBron Jamez Z.", "2024-3691113", "SASE"));
        return items;
    }

    public List<TeacherItem> getTeachers() {
        List<TeacherItem> items = new ArrayList<>();
        items.add(new TeacherItem("Dr. Ricardo Dalisay", "T-2024-001", "SECA"));
        items.add(new TeacherItem("Prof. Cardo Provinci", "T-2024-002", "SASE"));
        return items;
    }
}