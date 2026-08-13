package Pharmacy_store_managment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MedicineManager {
    Connection conn;
    Scanner scanner = new Scanner(System.in);
    List<Medicine> medicineList = new ArrayList<>();

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy_db", "root", "");
            loadMedicines();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n--- Medicine Manager ---");
            System.out.println("1. Add Medicine");
            System.out.println("2. View Medicines");
            System.out.println("3. Search Medicine");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addMedicine(); break;
                case 2: viewMedicines(); break;
                case 3: searchMedicine(); break;
                case 4: return;
                default: System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    void loadMedicines() throws SQLException {
        medicineList.clear();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM medicines");
        while (rs.next()) {
            medicineList.add(new Medicine(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDate("expiry_date").toLocalDate()
            ));
        }
    }

    void addMedicine() {
        try {
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Quantity: "); int qty = scanner.nextInt();
            System.out.print("Price: "); double price = scanner.nextDouble();
            System.out.print("Expiry Date (YYYY-MM-DD): "); String exp = scanner.next();
            String sql = "INSERT INTO medicines (name, quantity, price, expiry_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, qty);
            ps.setDouble(3, price);
            ps.setDate(4, Date.valueOf(exp));
            ps.executeUpdate();
            System.out.println("Medicine added.");
            loadMedicines();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewMedicines() {
        for (Medicine m : medicineList) m.display();
    }

    void searchMedicine() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        for (Medicine m : medicineList)
            if (m.name.equalsIgnoreCase(name)) m.display();
    }

    public void showExpiryAlerts() {
        LocalDate now = LocalDate.now();
        for (Medicine m : medicineList)
            if (m.expiryDate.isBefore(now.plusDays(30)))
                System.out.println("⚠ Near expiry: " + m.name + " (" + m.expiryDate + ")");
    }

    public void showStockGraph() {
        for (Medicine m : medicineList)
            System.out.printf("%-15s | %s (%d)\n", m.name, "*".repeat(m.quantity / 2), m.quantity);
    }
}