package Pharmacy_store_managment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

class PurchaseManager {
    Connection conn;
    Scanner scanner = new Scanner(System.in);

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy_db", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recordPurchase() {
        try {
            System.out.print("Medicine ID: ");
            int mid = scanner.nextInt();
            System.out.print("Supplier ID: ");
            int sid = scanner.nextInt();
            System.out.print("Quantity: ");
            int qty = scanner.nextInt();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO purchases (medicine_id, supplier_id, quantity, purchase_date) VALUES (?, ?, ?, NOW())");
            ps.setInt(1, mid);
            ps.setInt(2, sid);
            ps.setInt(3, qty);
            ps.executeUpdate();

            conn.createStatement().executeUpdate("UPDATE medicines SET quantity = quantity + " + qty + " WHERE id = " + mid);
            System.out.println("Purchase recorded and inventory updated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
