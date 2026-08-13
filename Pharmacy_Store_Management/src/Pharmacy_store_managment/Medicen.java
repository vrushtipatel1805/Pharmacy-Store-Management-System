package Pharmacy_store_managment;

import java.time.LocalDate;

class Medicine {
    public int id, quantity;
    public String name;
    public double price;
    public LocalDate expiryDate;

    public Medicine(int id, String name, int quantity, double price, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public void display() {
        System.out.printf("ID: %d | Name: %s | Qty: %d | Price: ₹%.2f | Exp: %s\n",
                id, name, quantity, price, expiryDate);
    }
}
