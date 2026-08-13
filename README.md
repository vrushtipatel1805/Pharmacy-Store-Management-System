# Pharmacy Store Management System

Pharmacy Store Management System is a Java-based application designed to manage the daily operations of a pharmacy efficiently. The system provides functionalities for managing medicines, customers, suppliers, purchases, sales, inventory, stock levels, and medicine expiry dates. It uses data structures such as Linked Lists and Min Heaps to efficiently organize and process pharmacy information. The application also generates sales bills and stores transaction details for better record management.

## Technology Stack

**Frontend:** Java Console / Command-Line Interface (CLI)

**Backend:** Java, JDBC

**Database:** MySQL

**Libraries:** JUnit, OpenPDF

**Development Tools:** IntelliJ IDEA

## Key Features

The system allows users to add, view, search, update, and manage medicine records including medicine name, price, available stock, and expiry date. Customer information such as name, phone number, and email can be maintained using a custom Linked List implementation. Supplier management provides functionality to add and view supplier details. The purchase module records medicine purchases from suppliers and updates inventory accordingly. The sales module manages medicine sales, checks available stock, calculates the total amount, updates inventory, and generates a bill receipt.

The project also implements an **Expiry Min Heap** to identify medicines with the earliest expiry dates and a **Low Stock Min Heap** to efficiently identify medicines with low inventory levels. These data structures demonstrate practical application of Data Structures and Algorithms in a real-world pharmacy management scenario.

## Project Structure

```text
Pharmacy_Store_Management/
│
├── src/
│   ├── PharmacyStore1.java
│   └── Pharmacy_store_managment/
│       ├── PharmacyStore.java
│       ├── PharmacyStore2.java
│       ├── PharmacyStore3.java
│       ├── Medicen.java
│       ├── MedicineManage.java
│       ├── PurchaseManager.java
│       ├── SalesManager.java
│       └── SupplierManager.java
│
├── bills/
│   └── Bill_1.txt
│
├── lib/
│   ├── JUnit
│   └── OpenPDF
│
├── pharmacy_db.pdf
├── Saels_report.pdf
└── erdaigram.drawio
```

## Database

The application connects to a MySQL database named `pharmacy_db` using JDBC. The database stores medicine, supplier, customer, purchase, and sales information.

This project demonstrates practical knowledge of **Java, Object-Oriented Programming, JDBC, MySQL, Data Structures, File Handling, and basic Database Management**, making it a useful real-world implementation of pharmacy inventory and sales management.
