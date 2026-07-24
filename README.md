File Processing Platform

Overview
This project is a Spring Boot based file processing application that allows users to upload Customer and Transaction files through a REST API or UI. The application validates file names, file size, headers, and content before storing the file on disk and saving metadata in the database.

Features
Upload Customer (.txt) files
Upload Transaction (.csv) files
File name validation
File size validation
Header validation
Content validation
Email validation
Transaction data validation
Custom exception handling
Store uploaded files on local disk
Store file metadata in database
Simple HTML UI for file upload


Technologies Used
Java 17
Spring Boot
Spring Data JPA
MySQL
HTML
JavaScript
Maven
Git & GitHub

Customer File Format
File Name
CUSTOMER_MASTER_YYYYMMDD.txt

Header
customer_id,customer_name,email,status

Sample Data

customer_id,customer_name,email,status
1001,Bharti,bharti@gmail.com,ACTIVE
1002,Rahul,rahul@gmail.com,ACTIVE

Transaction File Format

File Name
TTRA_MASTER_YYYYMMDD.csv

Header
transaction_id,account_number,amount,currency,status

Sample Data
transaction_id,account_number,amount,currency,status
TXN1001,ACC001,5000,INR,SUCCESS
TXN1002,ACC002,7500,USD,PENDING


Upload File
    ↓
File Name Validation
    ↓
File Size Validation
    ↓
Content Validation
    ↓
Store File On Disk
    ↓
Save Metadata In Database
    ↓
Return Response



Author
Bharti Mewada
Java Backend Developer
