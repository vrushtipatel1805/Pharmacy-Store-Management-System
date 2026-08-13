package Pharmacy_store_managment;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PharmacyStore {


    // ==== DB CONFIG ====
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/pharmacy_store";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final Scanner sc = new Scanner(System.in);

    // ========= MIN-HEAP for EXPIRY (soonest first) =========
    static class ExpiryMinHeap {
        static class Item {
            int id;
            String name;
            LocalDate expiry;
            int stock;
            Item(int id, String name, LocalDate expiry, int stock) {
                this.id = id; this.name = name; this.expiry = expiry; this.stock = stock;
            }
        }
        private final Item[] heap;
        private int size = 0;
        ExpiryMinHeap(int capacity){ heap = new Item[capacity]; }
        void clear(){ size = 0; }
        boolean isEmpty(){ return size == 0; }
        void insert(Item x){
            if(size >= heap.length) return;
            heap[size] = x;
            int i = size++;
            while(i > 0){
                int p = (i - 1) / 2;
                if(!heap[i].expiry.isBefore(heap[p].expiry)) break; // parent earlier or equal
                swap(i, p); i = p;
            }
        }
        Item extractMin(){
            if(size == 0) return null;
            Item min = heap[0];
            heap[0] = heap[size - 1];
            heap[size - 1] = null;
            size--;
            heapify(0);
            return min;
        }
        private void heapify(int i){
            while(true){
                int l = i * 2 + 1, r = i * 2 + 2, s = i;
                if(l < size && heap[l].expiry.isBefore(heap[s].expiry)) s = l;
                if(r < size && heap[r].expiry.isBefore(heap[s].expiry)) s = r;
                if(s == i) break;
                swap(i, s); i = s;
            }
        }
        private void swap(int a, int b){ Item t = heap[a]; heap[a] = heap[b]; heap[b] = t; }
    }

    // ====== DB helper ======
    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ====== BASIC CRUD: Medicines / Suppliers / Customers ======

    private void addMedicine() {
        try (Connection conn = connectDB()) {
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Price: ");
            double price = sc.nextDouble();
            System.out.print("Stock: ");
            int stock = sc.nextInt();
            sc.nextLine();
            System.out.print("Expiry (YYYY-MM-DD): ");
            String expiry = sc.nextLine();

            String sql = "INSERT INTO medicines (name, price, stock, expiry_date) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setInt(3, stock);
                ps.setString(4, expiry);
                ps.executeUpdate();
            }
            System.out.println("✅ Medicine added.");
        } catch (SQLException e) {
            System.err.println("Error adding medicine: " + e.getMessage());
        }
    }

    private void viewAllMedicines() {
        String sql = "SELECT id, name, price, stock, expiry_date FROM medicines ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-5s %-28s %-10s %-8s %-12s ",
                    "ID","Name","Price","Stock","Expiry");
            System.out.println("=".repeat(95));
            while (rs.next()){
                System.out.printf("%-5d %-28s ₹%-8.2f %-8d %-12s%n ",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("expiry_date"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading medicines: " + e.getMessage());
        }
    }

    private void searchMedicineByName() {
        System.out.print("Enter name (or part): ");
        String q = sc.nextLine();
        String sql = "SELECT id, name, price, stock, expiry_date FROM medicines WHERE name LIKE ? ORDER BY name";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()){
                    if(!any){
                        System.out.printf("%-5s %-28s %-10s %-8s %-12s ",
                                "ID","Name","Price","Stock","Expiry");
                        System.out.println("=".repeat(95));
                        any = true;
                    }
                    System.out.printf("%-5d %-28s ₹%-8.2f %-8d %-12s ",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getString("expiry_date"));

                }
                if(!any) System.out.println("No matches.");
            }
        } catch (SQLException e) {
            System.err.println("Error searching medicines: " + e.getMessage());
        }
    }

    private void addSupplier() {
        try (Connection conn = connectDB()) {
            System.out.print("Supplier name: ");
            String name = sc.nextLine();
            System.out.print("Contact: ");
            String contact = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO suppliers (name, phone, email) VALUES (?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.executeUpdate();
            }
            System.out.println("✅ Supplier added.");
        } catch (SQLException e) {
            System.err.println("Error adding supplier: " + e.getMessage());
        }
    }

    private void viewSuppliers() {
        String sql = "SELECT id, name, phone, email FROM suppliers ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-5s %-28s %-20s %-28s%n", "ID", "Name", "Contact", "Email");
            System.out.println("=".repeat(90));
            while (rs.next()){
                System.out.printf("%-5d %-28s %-20s %-28s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading suppliers: " + e.getMessage());
        }
    }

    private void addCustomer() {
        try (Connection conn = connectDB()) {
            System.out.print("Customer name: ");
            String name = sc.nextLine();
            System.out.print("Contact: ");
            String contact = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO customers (name, phone, email) VALUES (?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.executeUpdate();
            }
            System.out.println("✅ Customer added.");
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    private void viewCustomers() {
        String sql = "SELECT id, name,phone, email FROM customers ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-5s %-28s %-20s %-28s%n", "ID", "Name", "Contact", "Email");
            System.out.println("=".repeat(90));
            while (rs.next()){
                System.out.printf("%-5d %-28s %-20s %-28s%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("phone"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    // ====== PURCHASE: stock in + logs (no purchase bill) ======
    private void recordPurchase() {
        try (Connection conn = connectDB()) {
            int medicineId = chooseMedicineByName(conn);

            if (medicineId == 0) {
                System.out.println("❌ Medicine not found. Please try again.");
                return; // keep flow simple → user can re-run Record Purchase
            }

            System.out.print("Supplier ID: ");
            int supplierId = sc.nextInt();

            System.out.print("Quantity: ");
            int qty = sc.nextInt();

            System.out.print("Unit Price: ");
            double unitPrice = sc.nextDouble();
            sc.nextLine(); // consume newline

            System.out.print("Expiry (YYYY-MM-DD): ");
            String expiryStr = sc.nextLine().trim();
            LocalDate expiryDate = LocalDate.parse(expiryStr);

            // Call the updated procedure
            String sql = "{CALL PurchaseMedicine(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, medicineId);
                cs.setInt(2, supplierId);
                cs.setInt(3, qty);
                cs.setDouble(4, unitPrice);
                cs.setDate(5, Date.valueOf(expiryDate));
                cs.execute();
            }

            System.out.println("✅ Purchase recorded.");

        } catch (SQLException e) {
            System.err.println("Error recording purchase: " + e.getMessage());
        }
    }

    private int chooseMedicineByName(Connection conn) {
        while (true) {
            System.out.print("Enter medicine name (or part, 0 to finish): ");
            String query = sc.nextLine().trim();
            if (query.equals("0")) return 0;

            String sql = "SELECT id, name, price, stock, expiry_date FROM medicines WHERE name LIKE ? ORDER BY name";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + query + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    List<Integer> ids = new ArrayList<>();
                    boolean found = false;

                    System.out.printf("%-5s %-28s %-10s %-8s %-12s%n", "ID","Name","Price","Stock","Expiry");
                    System.out.println("=".repeat(70));

                    while (rs.next()) {
                        found = true;
                        int id = rs.getInt("id");
                        ids.add(id);
                        System.out.printf("%-5d %-28s ₹%-8.2f %-8d %-12s%n",
                                id,
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock"),
                                rs.getString("expiry_date"));
                    }

                    if (!found) {
                        System.out.println("❌ No medicines found. Try again.");
                        continue; // ✅ ask again instead of cancelling
                    }

                    System.out.print("Enter Medicine ID (0 to cancel): ");
                    int chosen = Integer.parseInt(sc.nextLine().trim());
                    if (chosen == 0) return 0;
                    if (ids.contains(chosen)) return chosen;

                    System.out.println("❌ Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.err.println("Error searching medicine: " + e.getMessage());
                return 0;
            }
        }
    }


    private String generateBillNumber(Connection conn) throws SQLException {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // e.g., 20250820
        String sql = "SELECT COUNT(*)+1 AS next_no FROM bills WHERE bill_number LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, today + "%"); // find all bills of today
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int next = rs.getInt("next_no");
                    return today + "-" + next; // e.g., 20250820-2
                }
            }
        }
        return today + "-1"; // fallback
    }

    private int getStock(Connection conn, int medicineId) {
        String sql = "SELECT stock FROM medicines WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching stock: " + e.getMessage());
        }
        return 0;
    }



    // ====== SALES BILL (auto tax 18% of (subtotal - discount)) ======
    private void generateSalesBill() {
        try (Connection conn = connectDB()) {
            System.out.print("Customer ID (0 for walk-in): ");
            int customerId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Payment Method (Cash/Card/UPI/etc): ");
            String paymentMethod = sc.nextLine().trim();

            String billNumber = generateBillNumber(conn); // generate bill number here

            int billId;
            try (CallableStatement cs = conn.prepareCall("{CALL CreateSaleBill(?, ?, ?, ?)}")) {
                if (customerId == 0) cs.setNull(1, Types.INTEGER);
                else cs.setInt(1, customerId);
                cs.setString(2, paymentMethod);      // not default anymore
                cs.setString(3, billNumber);         // pass generated bill number
                cs.registerOutParameter(4, java.sql.Types.INTEGER);
                cs.execute();
                billId = cs.getInt(4);
            }

            while (true) {
                int medId = chooseMedicineByName(conn);
                if (medId == 0) break;

                System.out.print("Qty: ");
                int qty = Integer.parseInt(sc.nextLine().trim());
                int currentStock = getStock(conn, medId);
                if (qty > currentStock) {
                    System.out.println("❌ Not enough stock. Available: " + currentStock);
                    continue; // ask again instead of calling procedure
                }


                double price = getMedicinePrice(conn, medId);

                try (CallableStatement cs = conn.prepareCall("{CALL AddSaleItem(?, ?, ?, ?, ?)}")) {
                    cs.setInt(1, billId);
                    cs.setInt(2, medId);
                    if (customerId == 0) cs.setNull(3, Types.INTEGER);
                    else cs.setInt(3, customerId);
                    cs.setInt(4, qty);
                    cs.setDouble(5, price);
                    cs.execute();
                }
            }
            System.out.print("Enter Discount % (0 if none): ");
            double discountPercent = Double.parseDouble(sc.nextLine().trim());
            if (discountPercent < 0) discountPercent = 0;
            if (discountPercent > 100) discountPercent = 100;


            try (CallableStatement cs = conn.prepareCall("{CALL RecalcBillTotals(?, ?)}")) {
                cs.setInt(1, billId);
                cs.setDouble(2, discountPercent);
                cs.execute();
            }


            System.out.println(" Sales Bill created. Bill ID: " + billId + " | Bill No: " + billNumber);
            printBill(billId);
        } catch (SQLException e) {
            System.err.println("Error generating sales bill: " + e.getMessage());
        }
    }



    // ====== Sales Report (between dates) ======
    private void salesReportBetweenDates() {
        System.out.print("Start date (YYYY-MM-DD): ");
        String start = sc.nextLine().trim();
        System.out.print("End date (YYYY-MM-DD): ");
        String end = sc.nextLine().trim();

// If user accidentally swapped dates, fix it
        if (start.compareTo(end) > 0) {
            String tmp = start;
            start = end;
            end = tmp;
        }


        String sql = "SELECT m.id, m.name, " +
                "SUM(bi.quantity) AS total_qty, " +
                "bi.unit_price, " +
                "SUM(bi.quantity * bi.unit_price) AS total_price " +
                "FROM bill_items bi " +
                "JOIN medicines m ON bi.medicine_id = m.id " +
                "JOIN bills b ON bi.bill_id = b.id " +
                "AND DATE(b.created_at) BETWEEN ? AND ? " +
                "GROUP BY m.id, m.name, bi.unit_price " +
                "ORDER BY m.name";

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            ResultSet rs = ps.executeQuery();

            double grandTotal = 0;
            System.out.printf("%-5s %-30s %-10s %-10s %-12s%n",
                    "ID", "Medicine", "Qty", "Price", "Total");
            System.out.println("============================================================");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int qty = rs.getInt("total_qty");
                double price = rs.getDouble("unit_price");
                double total = rs.getDouble("total_price");

                System.out.printf("%-5d %-30s %-10d ₹%-9.2f ₹%-10.2f%n",
                        id, name, qty, price, total);

                grandTotal += total;
            }

            System.out.println("------------------------------------------------------------");
            System.out.printf("TOTAL SALES (₹): %.2f%n", grandTotal);

        } catch (Exception e) {
            System.err.println("Error generating sales report: " + e.getMessage());
        }
    }



    // ====== EXPIRY REPORT via Min-Heap + remove expired stock ======
// Expiry Report using MinHeap
    // Expiry Report using MinHeap (view only)
    private void expiryReportWithHeap() {
        try (Connection conn = connectDB()) {
            String sql = "SELECT id, name, stock, expiry_date FROM medicines ORDER BY expiry_date ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // MinHeap for expiry (sorted by expiry_date)
            PriorityQueue<Medicine> minHeap = new PriorityQueue<>(Comparator.comparing(m -> m.expiry));

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int stock = rs.getInt("stock");
                LocalDate expiry = rs.getDate("expiry_date").toLocalDate();
                minHeap.add(new Medicine(id, name, stock, expiry));
            }

            LocalDate today = LocalDate.now();
            LocalDate next30 = today.plusDays(30);

            List<Medicine> expired = new ArrayList<>();
            List<Medicine> expiringSoon = new ArrayList<>();

            while (!minHeap.isEmpty()) {
                Medicine m = minHeap.poll();
                if (m.expiry.isBefore(today)) {
                    expired.add(m);
                } else if (!m.expiry.isAfter(next30)) {
                    expiringSoon.add(m);
                }
            }

            // Print results
            System.out.println("\n=== Already EXPIRED ===");
            if (expired.isEmpty()) {
                System.out.println("None");
            } else {
                System.out.printf("%-5s %-30s %-8s %-12s%n", "ID", "Name", "Stock", "Expiry");
                for (Medicine m : expired) {
                    System.out.printf("%-5d %-30s %-8d %-12s%n",
                            m.id, m.name, m.stock, m.expiry);
                }
            }

            System.out.println("\n=== Expiring in Next 30 Days ===");
            if (expiringSoon.isEmpty()) {
                System.out.println("None");
            } else {
                System.out.printf("%-5s %-30s %-8s %-12s%n", "ID", "Name", "Stock", "Expiry");
                for (Medicine m : expiringSoon) {
                    System.out.printf("%-5d %-30s %-8d %-12s%n",
                            m.id, m.name, m.stock, m.expiry);
                }
            }

        } catch (Exception e) {
            System.out.println("Error in expiry report: " + e.getMessage());
        }
    }

    // Remove all expired medicines (set stock = 0)
    private void removeAllExpiredStock() {
        String selectSql = "SELECT id, stock, expiry_date FROM medicines WHERE expiry_date < CURDATE() AND stock > 0";
        String logSql = "INSERT INTO expired_log (medicine_id, removed_qty, removed_at) VALUES (?, ?, NOW())";
        String updateSql = "UPDATE medicines SET stock = 0 WHERE id = ?";
        String selectSql1 = "SELECT id, medicine_id, removed_qty, removed_at FROM expired_log ORDER BY removed_at DESC";

        try (Connection conn = connectDB();
             PreparedStatement psSelect = conn.prepareStatement(selectSql);
             ResultSet rs = psSelect.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                int medId = rs.getInt("id");
                int stock = rs.getInt("stock");

                // 1. Log into expired_log
                try (PreparedStatement psLog = conn.prepareStatement(logSql)) {
                    psLog.setInt(1, medId);
                    psLog.setInt(2, stock);
                    psLog.executeUpdate();
                }

                // 2. Zero stock in medicines
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setInt(1, medId);
                    psUpdate.executeUpdate();
                }

                count++;
            }

            // Show expired log after processing
            try (PreparedStatement ps = conn.prepareStatement(selectSql1);
                 ResultSet rs1 = ps.executeQuery()) {

                System.out.println("\n=== Expired Medicines Log ===");
                System.out.printf("%-5s %-10s %-10s %-20s%n",
                        "LogID", "MedID", "Qty", "Removed At");
                System.out.println("=".repeat(60));

                boolean found = false;
                while (rs1.next()) {
                    found = true;
                    System.out.printf("%-5d %-10d %-10d %-20s%n",
                            rs1.getInt("id"),
                            rs1.getInt("medicine_id"),
                            rs1.getInt("removed_qty"),
                            rs1.getTimestamp("removed_at"));
                }

                if (!found) {
                    System.out.println("No expired medicines logged yet.");
                }

            }

            if (count == 0) {
                System.out.println("✅ No expired stock found.");
            } else {
                System.out.println("✅ Logged & cleared " + count + " expired medicines.");
            }

        } catch (SQLException e) {
            System.err.println("Error handling expired stock: " + e.getMessage());
        }
    }


    // Helper class for MinHeap
    class Medicine {
        int id;
        String name;
        int stock;
        LocalDate expiry;

        Medicine(int id, String name, int stock, LocalDate expiry) {
            this.id = id;
            this.name = name;
            this.stock = stock;
            this.expiry = expiry;
        }
    }

    // ====== Helpers for Billing ======


    private double getMedicinePrice(Connection conn, int medicineId) {
        String sql = "SELECT price FROM medicines WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("price");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching price: " + e.getMessage());
        }
        return 0.0;
    }

    private void printBill(int billId) {
        String billSql = "SELECT b.bill_number, b.created_at, c.name AS customer, " +
                "b.payment_method, b.total_amount, b.discount, b.tax_amount, b.final_amount " +
                "FROM bills b " +
                "LEFT JOIN customers c ON b.customer_id = c.id " +
                "WHERE b.id = ?";

        String itemsSql = "SELECT m.name, bi.quantity, bi.unit_price, " +
                "(bi.quantity * bi.unit_price) AS line_total " +
                "FROM bill_items bi " +
                "JOIN medicines m ON bi.medicine_id = m.id " +
                "WHERE bi.bill_id = ?";

        try (Connection conn = connectDB();
             PreparedStatement billPs = conn.prepareStatement(billSql);
             PreparedStatement itemsPs = conn.prepareStatement(itemsSql)) {

            billPs.setInt(1, billId);
            try (ResultSet brs = billPs.executeQuery()) {
                if (brs.next()) {
                    System.out.println("\n================= SALES BILL =================");
                    System.out.println("Bill No   : " + brs.getString("bill_number"));
                    System.out.println("Date      : " + brs.getTimestamp("created_at"));
                    System.out.println("Customer  : " + (brs.getString("customer") == null ? "Walk-in" : brs.getString("customer")));
                    System.out.println("Payment   : " + brs.getString("payment_method"));
                    System.out.println("------------------------------------------------");

                    // Print items
                    itemsPs.setInt(1, billId);
                    try (ResultSet irs = itemsPs.executeQuery()) {
                        System.out.printf("%-25s %-8s %-10s %-10s%n", "Medicine", "Qty", "Price", "Total");
                        System.out.println("------------------------------------------------");
                        while (irs.next()) {
                            System.out.printf("%-25s %-8d ₹%-9.2f ₹%-9.2f%n",
                                    irs.getString("name"),
                                    irs.getInt("quantity"),
                                    irs.getDouble("unit_price"),
                                    irs.getDouble("line_total"));
                        }
                    }

                    System.out.println("------------------------------------------------");
                    System.out.printf("Subtotal : ₹%.2f%n", brs.getDouble("total_amount"));
                    System.out.printf("Discount : ₹%.2f%n", brs.getDouble("discount"));
                    System.out.printf("Tax      : ₹%.2f%n", brs.getDouble("tax_amount"));
                    System.out.printf("FINAL    : ₹%.2f%n", brs.getDouble("final_amount"));
                    System.out.println("===============================================\n");
                } else {
                    System.out.println("❌ Bill not found.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error printing bill: " + e.getMessage());
        }
    }


    private void lowStockReport() {
        System.out.print("Enter stock threshold (e.g., 50): ");
        int threshold;
        try {
            threshold = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("⚠ Invalid input, using default = 50");
            threshold = 50;
        }

        String sql = "SELECT id, name, price, stock, expiry_date " +
                "FROM medicines " +
                "WHERE stock <= ? ORDER BY stock ASC";

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n=== LOW STOCK REPORT (≤ " + threshold + ") ===");
            System.out.printf("%-5s %-28s %-10s %-8s %-12s%n",
                    "ID", "Name", "Price", "Stock", "Expiry");
            System.out.println("=".repeat(70));

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-28s ₹%-8.2f %-8d %-12s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("expiry_date"));
            }
            if (!found) {
                System.out.println("✅ All medicines are above this stock level.");
            }

        } catch (SQLException e) {
            System.err.println("Error generating low stock report: " + e.getMessage());
        }
    }


    private static double round2(double v){ return Math.round(v * 100.0) / 100.0; }

    // ====== MENU ======
    void startMenu() {
        while (true) {
            System.out.println("\n=== Pharmacy Management System ===");
            System.out.println("1. Medicines  →  [Add, View, Search]");
            System.out.println("2. Parties    →  [Suppliers, Customers]");
            System.out.println("3. Stock Ops  →  [Record Purchase, Sales Bill]");
            System.out.println("4. Reports    →  [Sales Report, Expiry Report , LOW STOCK REPORT]");
            System.out.println("5. Maintenance→  [Remove Expired Stock]");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String in = sc.nextLine().trim();
            int ch;
            try { ch = Integer.parseInt(in); }
            catch (Exception e){ System.out.println("Invalid input."); continue; }

            switch (ch) {
                case 1 -> {
                    System.out.println("\n-- Medicines --");
                    System.out.println("1. Add Medicine");
                    System.out.println("2. View Medicines");
                    System.out.println("3. Search by Name");
                    System.out.print("Choice: ");
                    int m = Integer.parseInt(sc.nextLine().trim());
                    if (m==1) addMedicine();
                    else if (m==2) viewAllMedicines();
                    else if (m==3) searchMedicineByName();
                }
                case 2 -> {
                    System.out.println("\n-- Parties --");
                    System.out.println("1. Add Supplier");
                    System.out.println("2. View Suppliers");
                    System.out.println("3. Add Customer");
                    System.out.println("4. View Customers");
                    System.out.print("Choice: ");
                    int p = Integer.parseInt(sc.nextLine().trim());
                    if (p==1) addSupplier();
                    else if (p==2) viewSuppliers();
                    else if (p==3) addCustomer();
                    else if (p==4) viewCustomers();
                }
                case 3 -> {
                    System.out.println("\n-- Stock Operations --");
                    System.out.println("1. Record Purchase");
                    System.out.println("2. Generate Sales Bill");
                    System.out.print("Choice: ");
                    int s = Integer.parseInt(sc.nextLine().trim());
                    if (s==1) recordPurchase();
                    else if (s==2) generateSalesBill();
                }
                case 4 -> {
                    System.out.println("\n-- Reports --");
                    System.out.println("1. Sales Report (between dates)");
                    System.out.println("2. Expiry Report");
                    System.out.println("3. Low Stock Report");
                    System.out.print("Choice: ");
                    int r = Integer.parseInt(sc.nextLine().trim());
                    if (r==1) salesReportBetweenDates();
                    else if (r==2) expiryReportWithHeap();
                    else if (r==3) lowStockReport();
                }

                case 5 -> removeAllExpiredStock();
                case 0 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }


    // ====== MAIN ======
    public static void main(String[] args) {
        PharmacyStore app = new PharmacyStore();
        app.startMenu();
    }
}