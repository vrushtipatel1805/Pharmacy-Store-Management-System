package Pharmacy_store_managment;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PharmacyStore2 {

    public static class CustomerLL {
        static class Node {
            Customer data;
            Node next;
            Node(Customer d){ this.data=d; }
        }

        public static class Customer {
            int id;
            String name, phone, email;
            Customer(int id, String name, String phone, String email){
                this.id=id; this.name=name; this.phone=phone; this.email=email;
            }
        }

        private Node head;

        public void insertAtEnd(Customer c){
            Node n=new Node(c);
            if(head==null){ head=n; return; }
            Node cur=head; while(cur.next!=null) cur=cur.next; cur.next=n;
        }

        public boolean exists(String name, String phone, String email){
            Node c=head;
            while(c!=null){
                if(c.data.name.equalsIgnoreCase(name) ||
                        c.data.phone.equalsIgnoreCase(phone) ||
                        c.data.email.equalsIgnoreCase(email))
                    return true;
                c=c.next;
            }
            return false;
        }

        public void displayAll(){
            if(head==null){ System.out.println("⚠ No customers found."); return; }
            System.out.printf("%-5s %-20s %-15s %-25s%n","ID","Name","Phone","Email");
            System.out.println("=".repeat(70));
            Node c=head;
            while(c!=null){
                System.out.printf("%-5d %-20s %-15s %-25s%n",
                        c.data.id,c.data.name,c.data.phone,c.data.email);
                c=c.next;
            }
        }

        public boolean isEmpty(){ return head==null; }
        public void clear(){ head=null; }
    }



    public static class ExpiryMinHeap {
        static class Item {
            int id;
            String name;
            LocalDate expiry;
            int stock;
            Item(int id,String name,LocalDate expiry,int stock){
                this.id=id; this.name=name; this.expiry=expiry; this.stock=stock;
            }
        }



        private Item[] heap;
        private int size=0;

        public ExpiryMinHeap(int cap){ heap=new Item[cap]; }

        public void insert(Item x){
            if(size>=heap.length) return;
            heap[size]=x;
            int i=size++;
            while(i>0){
                int p=(i-1)/2;
                if(!heap[i].expiry.isBefore(heap[p].expiry)) break;
                swap(i,p); i=p;
            }
        }

        public Item extractMin(){
            if(size==0) return null;
            Item min=heap[0];
            heap[0]=heap[size-1]; heap[size-1]=null;
            size--; heapify(0);
            return min;
        }

        private void heapify(int i){
            while(true){
                int l=i*2+1,r=i*2+2,s=i;
                if(l<size && heap[l].expiry.isBefore(heap[s].expiry)) s=l;
                if(r<size && heap[r].expiry.isBefore(heap[s].expiry)) s=r;
                if(s==i) break;
                swap(i,s); i=s;
            }
        }

        private void swap(int a,int b){ Item t=heap[a]; heap[a]=heap[b]; heap[b]=t; }
    }

    public class LowStockMinHeap {
        static class Item {
            int id;
            String name;
            int stock;
            Item(int id,String name,int stock){ this.id=id; this.name=name; this.stock=stock; }
        }

        private Item[] heap;
        private int size=0;

        public LowStockMinHeap(int cap){ heap=new Item[cap]; }

        public void insert(Item x){
            if(size>=heap.length) return;
            heap[size]=x;
            int i=size++;
            while(i>0){
                int p=(i-1)/2;
                if(heap[i].stock>=heap[p].stock) break;
                swap(i,p); i=p;
            }
        }

        public Item extractMin(){
            if(size==0) return null;
            Item min=heap[0];
            heap[0]=heap[size-1]; heap[size-1]=null;
            size--; heapify(0);
            return min;
        }

        private void heapify(int i){
            while(true){
                int l=i*2+1, r=i*2+2, s=i;
                if(l<size && heap[l].stock<heap[s].stock) s=l;
                if(r<size && heap[r].stock<heap[s].stock) s=r;
                if(s==i) break;
                swap(i,s); i=s;
            }
        }

        private void swap(int a,int b){ Item t=heap[a]; heap[a]=heap[b]; heap[b]=t; }
    }

    public class SupplierLL {
        static class Node {
            Supplier data;
            Node next;
            Node(Supplier d){ this.data = d; }
        }

        public static class Supplier {
            int id;
            String name, phone, email;
            Supplier(int id, String name, String phone, String email){
                this.id=id; this.name=name; this.phone=phone; this.email=email;
            }
        }

        private Node head;

        public void insertAtEnd(Supplier s){
            Node n=new Node(s);
            if(head==null){ head=n; return; }
            Node c=head; while(c.next!=null)c=c.next; c.next=n;
        }

        public boolean exists(String name, String phone, String email){
            Node c=head;
            while(c!=null){
                if(c.data.name.equalsIgnoreCase(name) ||
                        c.data.phone.equalsIgnoreCase(phone) ||
                        c.data.email.equalsIgnoreCase(email))
                    return true;
                c=c.next;
            }
            return false;
        }

        public void displayAll(){
            if(head==null){ System.out.println("⚠ No suppliers found."); return; }
            System.out.printf("%-5s %-20s %-15s %-25s%n","ID","Name","Phone","Email");
            System.out.println("=".repeat(70));
            Node c=head;
            while(c!=null){
                System.out.printf("%-5d %-20s %-15s %-25s%n",
                        c.data.id, c.data.name, c.data.phone, c.data.email);
                c=c.next;
            }
        }

        public boolean isEmpty(){ return head==null; }
        public void clear(){ head=null; }
    }



    public class MedicineLL {
        static class Node {
            Medicine data;
            Node next;
            Node(Medicine data) { this.data = data; }
        }

        public static class Medicine {
            int id;
            String name;
            double price;
            int stock;
            LocalDate expiry;

            public Medicine(int id, String name, double price, int stock, LocalDate expiry) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.expiry = expiry;
            }
        }

        private Node head;

        public void insertAtEnd(Medicine m) {
            Node n = new Node(m);
            if (head == null) { head = n; return; }
            Node c = head;
            while (c.next != null) c = c.next;
            c.next = n;
        }

        public boolean exists(String name, LocalDate expiry) {
            Node c = head;
            while (c != null) {
                if (c.data.name.equalsIgnoreCase(name) && c.data.expiry.equals(expiry))
                    return true;
                c = c.next;
            }
            return false;
        }

        public void displayAll() {
            if (head == null) { System.out.println("⚠ No medicines found."); return; }
            System.out.printf("%-5s %-20s %-8s %-8s %-12s %-20s%n",
                    "ID","Name","Price","Stock","Expiry","Description");
            System.out.println("=".repeat(80));
            Node c = head;
            while (c != null) {
                System.out.printf("%-5d %-20s ₹%-7.2f %-8d %-12s%n",
                        c.data.id, c.data.name, c.data.price, c.data.stock,
                        c.data.expiry);
                c = c.next;
            }
        }

        public boolean isEmpty() { return head == null; }
        public void clear() { head = null; }
    }




    // ==== DB CONFIG ====
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/pharmacy_store";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final Scanner sc = new Scanner(System.in);

    // ==== Custom DS instances ====
    private final MedicineLL medicinesLL = new MedicineLL();
    private final SupplierLL suppliersLL = new SupplierLL();
    private final CustomerLL customersLL = new CustomerLL();

    // ====== DB helper ======
    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ====== LOAD DATA FROM DB → CUSTOM LINKED LISTS ======
    private void loadAllFromDB() {
        loadMedicinesFromDB();
        loadSuppliersFromDB();
        loadCustomersFromDB();
    }

    private void loadMedicinesFromDB() {
        medicinesLL.clear();
        String sql = "SELECT id, name, price, stock, expiry_date FROM medicines ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                LocalDate expiry = (rs.getDate("expiry_date") == null) ? null : rs.getDate("expiry_date").toLocalDate();


                // prevent duplicate in LL: name+expiry
                if (!medicinesLL.exists(name, expiry)) {
                    medicinesLL.insertAtEnd(new MedicineLL.Medicine(id, name, price, stock, expiry));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading medicines: " + e.getMessage());
        }
    }

    private void loadSuppliersFromDB() {
        suppliersLL.clear();
        String sql = "SELECT id, name, phone, email FROM suppliers ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SupplierLL.Supplier s = new SupplierLL.Supplier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                if (!suppliersLL.exists(s.name, s.phone, s.email)) {
                    suppliersLL.insertAtEnd(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading suppliers: " + e.getMessage());
        }
    }

    private void loadCustomersFromDB() {
        customersLL.clear();
        String sql = "SELECT id, name, phone, email FROM customers ORDER BY id";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CustomerLL.Customer c = new CustomerLL.Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                if (!customersLL.exists(c.name, c.phone, c.email)) {
                    customersLL.insertAtEnd(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    // ====== BASIC CRUD: Medicines / Suppliers / Customers ======

    private void addMedicine() {
        try (Connection conn = connectDB()) {
            System.out.print("Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Price: ");
            double price = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Stock: ");
            int stock = Integer.parseInt(sc.nextLine().trim());

            LocalDate expiry = null;
            while (expiry == null) {
                System.out.print("Expiry (YYYY-MM-DD): ");
                String dateStr = sc.nextLine().trim();
                try {
                    expiry = LocalDate.parse(dateStr);
                } catch (Exception e) {
                    System.out.println("❌ Invalid date format. Use YYYY-MM-DD.");
                }
            }

            if (expiry.isBefore(LocalDate.now())) {
                System.out.println("❌ Medicine already expired (" + expiry + "). Not added.");
                return;
            }

            String sql = "INSERT INTO medicines (name, price, stock, expiry_date) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setInt(3, stock);
                ps.setDate(4, java.sql.Date.valueOf(expiry));
                ps.executeUpdate();

                // ✅ Get generated ID
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        // ✅ Insert into Linked List too
                        medicinesLL.insertAtEnd(new MedicineLL.Medicine(id, name, price, stock, expiry));
                    }
                }
            }
            System.out.println("✅ Medicine added.");

        } catch (SQLException e) {
            System.err.println("Error adding medicine: " + e.getMessage());
        }
    }

    private void viewAllMedicines() {
        medicinesLL.displayAll();
    }

    private void searchMedicineByName() {
        System.out.print("Enter name (or part): ");
        String q = sc.nextLine().trim().toLowerCase();

        boolean any = false;
        System.out.printf("%-5s %-20s %-8s %-8s %-12s %-20s%n",
                "ID","Name","Price","Stock","Expiry","Description");
        System.out.println("=".repeat(80));

        // iterate LL (no ArrayList)
        // We'll traverse using a simple cursor
        try (Connection ignored = null) { // just to keep structure similar; not using DB here
            // manually traverse
            class Cursor { MedicineLL.Node n; Cursor(MedicineLL.Node n){ this.n=n; } }
            Cursor cur = new Cursor(getMedHead());
            while (cur.n != null) {
                MedicineLL.Medicine m = cur.n.data;
                if (m.name.toLowerCase().contains(q)) {
                    any = true;
                    System.out.printf("%-5d %-20s ₹%-7.2f %-8d %-12s %-20s%n",
                            m.id, m.name, m.price, m.stock,
                            (m.expiry==null ? "-" : m.expiry.toString()));
                }
                cur.n = cur.n.next;
            }
        } catch (Exception ignored) {}

        if (!any) System.out.println("No matches.");
    }

    // helper to access head (kept package-private via reflection of class structure)
    private MedicineLL.Node getMedHead() {
        try {
            java.lang.reflect.Field head = MedicineLL.class.getDeclaredField("head");
            head.setAccessible(true);
            return (MedicineLL.Node) head.get(medicinesLL);
        } catch (Exception e) {
            return null;
        }
    }

    private SupplierLL.Node getSupHead() {
        try {
            java.lang.reflect.Field head = SupplierLL.class.getDeclaredField("head");
            head.setAccessible(true);
            return (SupplierLL.Node) head.get(suppliersLL);
        } catch (Exception e) {
            return null;
        }
    }

    private CustomerLL.Node getCusHead() {
        try {
            java.lang.reflect.Field head = CustomerLL.class.getDeclaredField("head");
            head.setAccessible(true);
            return (CustomerLL.Node) head.get(customersLL);
        } catch (Exception e) {
            return null;
        }
    }

    private void addSupplier() {
        try (Connection conn = connectDB()) {
            System.out.print("Supplier name: ");
            String name = sc.nextLine().trim();

            String contact;
            while (true) {
                System.out.print("Contact (10-digit, starts with 9): ");
                contact = sc.nextLine().trim();
                if (contact.matches("9\\d{9}")) break;
                System.out.println("❌ Invalid. Enter valid 10-digit number starting with 9.");
            }

            String email;
            while (true) {
                System.out.print("Email: ");
                email = sc.nextLine().trim();
                if (email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) break;
                System.out.println("❌ Invalid email format. Try again.");
            }

            String sql = "INSERT INTO suppliers (name, phone, email) VALUES (?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.executeUpdate();

                // ✅ Get generated ID
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        // ✅ Add to Linked List
                        suppliersLL.insertAtEnd(new SupplierLL.Supplier(id, name, contact, email));
                    }
                }
            }
            System.out.println("✅ Supplier added.");

        } catch (SQLException e) {
            System.err.println("Error adding supplier: " + e.getMessage());
        }
    }

    private void viewSuppliers() {
        suppliersLL.displayAll();
    }

    private void addCustomer() {
        try (Connection conn = connectDB()) {
            System.out.print("Customer name: ");
            String name = sc.nextLine().trim();

            String contact;
            while (true) {
                System.out.print("Contact (10-digit, starts with 9): ");
                contact = sc.nextLine().trim();
                if (contact.matches("9\\d{9}")) break;
                System.out.println("❌ Invalid. Enter valid 10-digit number starting with 9.");
            }

            String email;
            while (true) {
                System.out.print("Email: ");
                email = sc.nextLine().trim().toLowerCase();
                if (email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) break;
                System.out.println("❌ Invalid email format. Try again.");
            }

            String sql = "INSERT INTO customers (name, phone, email) VALUES (?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.executeUpdate();

                // ✅ Get generated ID
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        // ✅ Add to Linked List
                        customersLL.insertAtEnd(new CustomerLL.Customer(id, name, contact, email));
                    }
                }
            }
            System.out.println("✅ Customer added.");

        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    private void viewCustomers() {
        customersLL.displayAll();
    }

    // ====== PURCHASE: stock in + logs (no purchase bill) ======
    private void recordPurchase() {
        try (Connection conn = connectDB()) {
            int medicineId = chooseMedicineByNameFromLL();

            if (medicineId == 0) {
                System.out.println("❌ Medicine not found. Please try again.");
                return;
            }

            System.out.print("Supplier ID: ");
            int supplierId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Unit Price: ");
            double unitPrice = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Expiry (YYYY-MM-DD): ");
            String expiryStr = sc.nextLine().trim();
            LocalDate expiryDate = LocalDate.parse(expiryStr);

            String sql = "{CALL PurchaseMedicine(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, medicineId);
                cs.setInt(2, supplierId);
                cs.setInt(3, qty);
                cs.setDouble(4, unitPrice);
                cs.setDate(5, java.sql.Date.valueOf(expiryDate));
                cs.execute();
            }

            // reflect stock change in LL
            updateMedicineStockInLL(medicineId, qty);

            System.out.println("✅ Purchase recorded.");
        } catch (SQLException e) {
            System.err.println("Error recording purchase: " + e.getMessage());
        }
    }

    private void updateMedicineStockInLL(int medicineId, int delta) {
        MedicineLL.Node cur = getMedHead();
        while (cur != null) {
            if (cur.data.id == medicineId) {
                cur.data.stock += delta;
                break;
            }
            cur = cur.next;
        }
    }

    private int chooseMedicineByNameFromLL() {
        while (true) {
            System.out.print("Enter medicine name (or part, 0 to finish): ");
            String query = sc.nextLine().trim();
            if (query.equals("0")) return 0;

            MedicineLL.Node cur = getMedHead();
            boolean foundAny = false;

            System.out.printf("%-5s %-20s %-10s %-8s %-12s%n", "ID","Name","Price","Stock","Expiry");
            System.out.println("=".repeat(65));
            while (cur != null) {
                MedicineLL.Medicine m = cur.data;
                if (m.name.toLowerCase().contains(query.toLowerCase())) {
                    foundAny = true;
                    System.out.printf("%-5d %-20s ₹%-8.2f %-8d %-12s%n",
                            m.id, m.name, m.price, m.stock, (m.expiry==null?"-":m.expiry.toString()));
                }
                cur = cur.next;
            }

            if (!foundAny) {
                System.out.println("❌ No medicines found. Try again.");
                continue;
            }

            System.out.print("Enter Medicine ID (0 to cancel): ");
            String chosenStr = sc.nextLine().trim();
            int chosen = 0;
            try { chosen = Integer.parseInt(chosenStr); } catch (Exception ignored) {}
            if (chosen == 0) return 0;

            // verify chosen exists
            cur = getMedHead();
            while (cur != null) {
                if (cur.data.id == chosen) return chosen;
                cur = cur.next;
            }
            System.out.println("❌ Invalid choice. Try again.");
        }
    }

    private String generateBillNumber(Connection conn) throws SQLException {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // e.g., 20250820
        String sql = "SELECT COUNT(*)+1 AS next_no FROM bills WHERE bill_number LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, today + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int next = rs.getInt("next_no");
                    return today + "-" + next;
                }
            }
        }
        return today + "-1";
    }

    private int getStockFromLL(int medicineId) {
        MedicineLL.Node cur = getMedHead();
        while (cur != null) {
            if (cur.data.id == medicineId) return cur.data.stock;
            cur = cur.next;
        }
        return 0;
    }

    private double getPriceFromLL(int medicineId) {
        MedicineLL.Node cur = getMedHead();
        while (cur != null) {
            if (cur.data.id == medicineId) return cur.data.price;
            cur = cur.next;
        }
        return 0.0;
    }

    // ====== SALES BILL (auto tax 18% of (subtotal - discount)) ======
    private void generateSalesBill() {
        try (Connection conn = connectDB()) {
            System.out.print("Customer ID (0 for walk-in): ");
            int customerId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Payment Method (Cash/Card/UPI/etc): ");
            String paymentMethod = sc.nextLine().trim();

            String billNumber = generateBillNumber(conn);

            int billId;
            try (CallableStatement cs = conn.prepareCall("{CALL CreateSaleBill(?, ?, ?, ?)}")) {
                if (customerId == 0) cs.setNull(1, Types.INTEGER);
                else cs.setInt(1, customerId);
                cs.setString(2, paymentMethod);
                cs.setString(3, billNumber);
                cs.registerOutParameter(4, java.sql.Types.INTEGER);
                cs.execute();
                billId = cs.getInt(4);
            }

            while (true) {
                int medId = chooseMedicineByNameFromLL();
                if (medId == 0) break;

                System.out.print("Qty: ");
                int qty = Integer.parseInt(sc.nextLine().trim());
                int currentStock = getStockFromLL(medId);
                if (qty > currentStock) {
                    System.out.println("❌ Not enough stock. Available: " + currentStock);
                    continue;
                }

                double price = getPriceFromLL(medId);

                try (CallableStatement cs = conn.prepareCall("{CALL AddSaleItem(?, ?, ?, ?, ?)}")) {
                    cs.setInt(1, billId);
                    cs.setInt(2, medId);
                    if (customerId == 0) cs.setNull(3, Types.INTEGER);
                    else cs.setInt(3, customerId);
                    cs.setInt(4, qty);
                    cs.setDouble(5, price);
                    cs.execute();
                }

                // reflect stock deduction in LL
                updateMedicineStockInLL(medId, -qty);
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

        if (start.compareTo(end) > 0) {
            String tmp = start; start = end; end = tmp;
        }

        String sql = "SELECT m.id, m.name, SUM(bi.quantity) AS total_qty, bi.unit_price, " +
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

    // ====== EXPIRY REPORT via Custom Min-Heap ======
    private void expiryReportWithHeap() {
        // Count items to size heap
        int count = 0;
        MedicineLL.Node cur = getMedHead();
        while (cur != null) { count++; cur = cur.next; }

        if (count == 0) {
            System.out.println("⚠ No medicines to report.");
            return;
        }

        ExpiryMinHeap heap = new ExpiryMinHeap(count);

        // Fill heap from LL
        cur = getMedHead();
        while (cur != null) {
            MedicineLL.Medicine m = cur.data;
            LocalDate ex = (m.expiry == null) ? LocalDate.of(9999,12,31) : m.expiry; // push far future if null
            heap.insert(new ExpiryMinHeap.Item(m.id, m.name, ex, m.stock));
            cur = cur.next;
        }

        LocalDate today = LocalDate.now();
        LocalDate next30 = today.plusDays(30);

        System.out.println("\n=== Already EXPIRED ===");
        boolean expiredAny = false;

        // Extract in ascending expiry order
        while (true) {
            ExpiryMinHeap.Item it = heap.extractMin();
            if (it == null) break;
            if (it.expiry.isBefore(today)) {
                if (!expiredAny) {
                    System.out.printf("%-5s %-30s %-8s %-12s%n", "ID", "Name", "Stock", "Expiry");
                    expiredAny = true;
                }
                System.out.printf("%-5d %-30s %-8d %-12s%n", it.id, it.name, it.stock, it.expiry);
            }
        }
        if (!expiredAny) System.out.println("None");

        // Refill heap to list expiring soon (since we consumed it)
        cur = getMedHead();
        while (cur != null) {
            MedicineLL.Medicine m = cur.data;
            LocalDate ex = (m.expiry == null) ? LocalDate.of(9999,12,31) : m.expiry;
            heap.insert(new ExpiryMinHeap.Item(m.id, m.name, ex, m.stock));
            cur = cur.next;
        }

        System.out.println("\n=== Expiring in Next 30 Days ===");
        boolean soonAny = false;

        while (true) {
            ExpiryMinHeap.Item it = heap.extractMin();
            if (it == null) break;
            if (!it.expiry.isBefore(today) && !it.expiry.isAfter(next30)) {
                if (!soonAny) {
                    System.out.printf("%-5s %-30s %-8s %-12s%n", "ID", "Name", "Stock", "Expiry");
                    soonAny = true;
                }
                System.out.printf("%-5d %-30s %-8d %-12s%n", it.id, it.name, it.stock, it.expiry);
            }
        }
        if (!soonAny) System.out.println("None");
    }

    // ====== Remove all expired medicines (set stock = 0) + log (DB) ======
    private void removeAllExpiredStock() {
        String selectSql = "SELECT id, stock FROM medicines WHERE expiry_date < CURDATE() AND stock > 0";
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

                try (PreparedStatement psLog = conn.prepareStatement(logSql)) {
                    psLog.setInt(1, medId);
                    psLog.setInt(2, stock);
                    psLog.executeUpdate();
                }

                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setInt(1, medId);
                    psUpdate.executeUpdate();
                }

                // reflect in LL
                updateMedicineStockAbsoluteInLL(medId, 0);

                count++;
            }

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

    private void updateMedicineStockAbsoluteInLL(int medicineId, int newStock) {
        MedicineLL.Node cur = getMedHead();
        while (cur != null) {
            if (cur.data.id == medicineId) {
                cur.data.stock = newStock;
                break;
            }
            cur = cur.next;
        }
    }

    // ====== Helpers for Billing Print ======
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

    // ====== LOW STOCK REPORT using custom Min-Heap ======
    private void lowStockReport() {
        System.out.print("Enter stock threshold (e.g., 50): ");
        int threshold;
        try { threshold = Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("Invalid input, using default = 50"); threshold = 50; }

        // count how many are <= threshold (for heap capacity)
        int eligible = 0;
        MedicineLL.Node cur = getMedHead();
        while (cur != null) {
            if (cur.data.stock <= threshold) eligible++;
            cur = cur.next;
        }

        if (eligible == 0) {
            System.out.println("✅ All medicines are above this stock level.");
            return;
        }

        LowStockMinHeap heap = new LowStockMinHeap(eligible);

        cur = getMedHead();
        while (cur != null) {
            MedicineLL.Medicine m = cur.data;
            if (m.stock <= threshold) {
                heap.insert(new LowStockMinHeap.Item(m.id, m.name, m.stock));
            }
            cur = cur.next;
        }

        System.out.println("\n=== LOW STOCK REPORT (≤ " + threshold + ") ===");
        System.out.printf("%-5s %-28s %-8s%n", "ID", "Name", "Stock");
        System.out.println("=".repeat(45));

        while (true) {
            LowStockMinHeap.Item it = heap.extractMin();
            if (it == null) break;
            System.out.printf("%-5d %-28s %-8d%n", it.id, it.name, it.stock);
        }
    }

    private static double round2(double v){ return Math.round(v * 100.0) / 100.0; }

    // ====== MENU ======
     void startMenu() {
        // first load from DB into DS
        loadAllFromDB();

        while (true) {
            System.out.println("\n=== Pharmacy Management System ===");
            System.out.println("1. Medicines  →  [Add, View, Search]");
            System.out.println("2. Parties    →  [Suppliers, Customers]");
            System.out.println("3. Stock Ops  →  [Record Purchase, Sales Bill]");
            System.out.println("4. Reports    →  [Sales Report, Expiry Report, LOW STOCK REPORT]");
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
                    System.out.println("2. Expiry Report (custom heap)");
                    System.out.println("3. Low Stock Report (custom heap)");
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
        PharmacyStore2 app = new PharmacyStore2();
        app.startMenu();
    }
}
