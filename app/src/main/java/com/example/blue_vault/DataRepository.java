package com.example.blue_vault;

import java.util.ArrayList;
import java.util.List;

/**
 * Central repository for all data. 
 * Placeholders are used here, which can be easily replaced with database (Firebase/Room) calls later.
 */
public class DataRepository {

    private static DataRepository instance;

    private DataRepository() {}

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public List<ResearchItem> getResearches() {
        List<ResearchItem> items = new ArrayList<>();
        // Placeholder data with full details including star ratings (0-5)
        items.add(new ResearchItem(
            "Automated Library Vault System using QR Code", 
            "Udarbe, LeBron Jamez Z.\nAlinsurin, Emmanuel C.", 
            "SECA", 
            "BSIT", 
            "Oct 24, 2024", 
            "Approved",
            "This research focuses on developing a secure and automated way to manage library resources using QR code technology for student identification and book tracking...",
            "Automation, QR Code, Library, Security",
            "https://doi.org/10.1234/bluevault.2024",
            4
        ));
        
        items.add(new ResearchItem(
            "AI in Modern Education", 
            "Emmanuel Alinsurin", 
            "SECA", 
            "BSCS", 
            "Oct 25, 2024", 
            "Approved",
            "Exploring the impact of Artificial Intelligence on student engagement and academic performance in higher education settings.",
            "AI, Education, Technology",
            "https://doi.org/10.1234/bluevault.ai.2024",
            5
        ));

        items.add(new ResearchItem(
            "Smart City Traffic Management", 
            "Sean Manding", 
            "SASE", 
            "BSIT", 
            "Oct 26, 2024", 
            "Declined",
            "Utilizing IoT and real-time data analytics to optimize traffic flow in urban areas.",
            "IoT, Smart City, Analytics",
            "https://doi.org/10.1234/bluevault.city.2024",
            3
        ));

        items.add(new ResearchItem(
            "Blockchain for Secure Voting", 
            "Ryoji Lagaras", 
            "SBMA", 
            "BSIS", 
            "Oct 27, 2024", 
            "Pending",
            "Implementing a blockchain-based voting system to ensure transparency and security in student council elections.",
            "Blockchain, Voting, Security",
            "https://doi.org/10.1234/bluevault.vote.2024",
            0
        ));
        return items;
    }

    public List<StudentItem> getStudents() {
        List<StudentItem> items = new ArrayList<>();
        items.add(new StudentItem("Alinsurin, Emmanuel C.", "2024-1234567", "SECA"));
        items.add(new StudentItem("Manding, Sean M.", "2024-2468101", "SECA"));
        items.add(new StudentItem("Udarbe, LeBron Jamez Z.", "2024-3691113", "SASE"));
        items.add(new StudentItem("Lagaras, Ryoji B.", "2024-4812162", "SBMA"));
        return items;
    }

    public List<TeacherItem> getTeachers() {
        List<TeacherItem> items = new ArrayList<>();
        items.add(new TeacherItem("Dr. Ricardo Dalisay", "T-2024-001", "SECA"));
        items.add(new TeacherItem("Prof. Cardo Provinci", "T-2024-002", "SASE"));
        items.add(new TeacherItem("Engr. Neri Naig", "T-2024-003", "SECA"));
        return items;
    }
}