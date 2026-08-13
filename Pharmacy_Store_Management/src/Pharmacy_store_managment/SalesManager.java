package Pharmacy_store_managment;
import java.io.FileWriter;
import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.time.LocalDate;
class SalesManager {
    Connection conn;
    Scanner scanner = new Scanner(System.in);

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy_db", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sellMedicine() {
        try {
            System.out.print("Enter Medicine ID: ");
            int mid = scanner.nextInt();
            System.out.print("Enter Quantity to Sell: ");
            int qty = scanner.nextInt();

            ResultSet rs = conn.createStatement().executeQuery("SELECT name, quantity, price FROM medicines WHERE id = " + mid);
            if (rs.next()) {
                String name = rs.getString("name");
                int stock = rs.getInt("quantity");
                double price = rs.getDouble("price");

                if (stock < qty) {
                    System.out.println("Insufficient stock.");
                    return;
                }

                double total = price * qty;
                conn.createStatement().executeUpdate("UPDATE medicines SET quantity = quantity - " + qty + " WHERE id = " + mid);
                conn.createStatement().executeUpdate("INSERT INTO sales (medicine_id, quantity, total_amount, sale_date) VALUES (" + mid + "," + qty + "," + total + ",NOW())");

                FileWriter fw = new FileWriter("receipt_" + System.currentTimeMillis() + ".txt");
                fw.write("Medicine: " + name + "\nQuantity: " + qty + "\nTotal: ₹" + total);
                fw.close();
                System.out.println("✅ Medicine sold. Bill saved.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
