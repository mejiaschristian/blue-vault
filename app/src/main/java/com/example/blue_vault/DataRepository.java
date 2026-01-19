package com.example.blue_vault;

import java.util.ArrayList;
import java.util.List;

/**
 * Central repository for all data. 
 * Placeholders are used here, which can be easily replaced with database (Firebase/Room) calls later.
 */
public class DataRepository {

    private static DataRepository instance;
    private List<ResearchItem> allResearches;

    private DataRepository() {
        allResearches = new ArrayList<>();
        // Initialize with placeholder data
        // Approved and Published
        allResearches.add(new ResearchItem(
            "Automated Library Vault System using QR Code", 
            "Udarbe, LeBron Jamez Z.\nAlinsurin, Emmanuel C.", 
            "SECA", 
            "BSIT", 
            "Oct 24, 2024", 
            "Approved",
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
                "Approved",
                "loremloremloremloremloremloremloremloremloreml oremloremloremlor emloremloremloremloreml oremloremloremloremloremloremloreml " +
                        "oremloremloreml oremloremlorem loremloremloremloremloremlorem loremloremloremloremloremloremloremloremloremloremloremlorem",
                "toilet, amogus",
                "https://doi.org/10.1234/bluevault.2024",
                4.2f,
                true
        ));
        
        allResearches.add(new ResearchItem(
            "AI in Modern Education", 
            "Emmanuel Alinsurin", 
            "SECA", 
            "BSCS", 
            "Oct 25, 2024", 
            "Approved",
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
            "SASE", 
            "BSIT", 
            "Oct 26, 2024", 
            "Approved",
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
            "SBMA", 
            "BSIS", 
            "Oct 27, 2024", 
            "Declined",
            "A study on how late-night gaming affects student sleep patterns.",
            "Gaming, Health, Students",
            "",
            1.2f,
            false
        ));

        // Pending
        allResearches.add(new ResearchItem(
            "Blockchain for Secure Voting", 
            "Mejias Christiano", 
            "SBMA", 
            "BSIS", 
            "Oct 28, 2024", 
            "Pending",
            "Implementing a blockchain-based voting system for transparency.",
            "Blockchain, Voting, Security",
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
     * @return List of all research items across all statuses
     */
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
            if ("Approved".equalsIgnoreCase(item.getStatus()) && !item.isPublished()) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getApprovedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if ("Approved".equalsIgnoreCase(item.getStatus())) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getDeclinedResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if ("Declined".equalsIgnoreCase(item.getStatus())) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<ResearchItem> getPendingResearches() {
        List<ResearchItem> filtered = new ArrayList<>();
        for (ResearchItem item : allResearches) {
            if ("Pending".equalsIgnoreCase(item.getStatus())) {
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