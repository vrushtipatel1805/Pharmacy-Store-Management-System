package Pharmacy_store_managment;

import java.sql.Connection;
import java.io.FileWriter;
import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.time.LocalDate;
public class SupplierManager {
    Connection conn;
    Scanner scanner = new Scanner(System.in);

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy_db", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n--- Supplier Manager ---");
            System.out.println("1. Add Supplier");
            System.out.println("2. View Suppliers");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addSupplier(); break;
                case 2: viewSuppliers(); break;
                case 3: return;
                default: System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    void addSupplier() {
        try {
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Contact: "); String contact = scanner.nextLine();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO suppliers (name, contact) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.executeUpdate();
            System.out.println("Supplier added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewSuppliers() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM suppliers");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Contact: %s\n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("contact"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
