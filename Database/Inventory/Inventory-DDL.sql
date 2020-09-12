-- Inventory-DDL.sql
-- script to create Inventory database, create TABLEs

-- CST 8215
-- Assignement 1
-- Students: Damir Omelic, Samuel Ebba, Jasdeep Kaur
-- Section 300
-- Professor: Sanaa Issa
-- Questions: 1, 2, 3, 4, 7

DROP TABLE IF EXISTS Invoice_Line_T;
DROP TABLE IF EXISTS Product_T;
DROP TABLE IF EXISTS Invoice_T;
DROP TABLE IF EXISTS Customer_T;
DROP TABLE IF EXISTS City_T; -- 7
DROP TABLE IF EXISTS Country_T; -- 7

-- drop table order follows first in, last out rule (i.e., dependent tables dropped first)

-- 1
CREATE TABLE Country_T (
Cntry_Code CHAR( 3 ) NOT NULL,
Cntry_Name VARCHAR( 30 ),
Cntry_Population INTEGER NOT NULL,
CONSTRAINT PK_Country PRIMARY KEY( Cntry_Code )
);

-- 2
CREATE TABLE City_T (
City_ID INTEGER NOT NULL,
City_Name VARCHAR ( 30 ),
Cntry_Code CHAR( 3 ),
City_Population INTEGER,
CONSTRAINT PK_City PRIMARY KEY( City_ID ),
CONSTRAINT FK_Cntry_Code FOREIGN KEY( Cntry_Code ) REFERENCES Country_T( Cntry_Code )
);

CREATE TABLE Customer_T (
Cust_Id CHAR( 4 ),
Cust_Fname VARCHAR( 30 ) NULL,
Cust_Lname VARCHAR( 30 ) NOT NULL,
Cust_Phone VARCHAR( 15 ) NOT NULL,
Cust_Address VARCHAR( 20 ) NOT NULL,
Cust_City VARCHAR( 15 ) NOT NULL,
Cust_Prov CHAR( 2 ) NULL,
Cust_PostCode CHAR( 6 ) NOT NULL,
Cust_Balance DECIMAL( 9,2 ), 
Cust_Country CHAR ( 3 ) NOT NULL, -- 3
CONSTRAINT PK_Customer PRIMARY KEY( Cust_Id ),
CONSTRAINT FK_Cust_Country FOREIGN KEY( Cust_Country ) REFERENCES Country_T( Cntry_Code ) -- 3
);

CREATE TABLE Invoice_T (
Invoice_Number CHAR(6),
Cust_Id CHAR(4) NOT NULL,
Invoice_Date DATE,
CONSTRAINT PK_Invoice PRIMARY KEY( Invoice_Number ),
CONSTRAINT FK_Cust_ID FOREIGN KEY( Cust_Id ) REFERENCES Customer_T( Cust_ID )
);

CREATE TABLE Product_T (
Prod_Code CHAR(5),
Prod_Description VARCHAR(60) NOT NULL,
Prod_Indate DATE NOT NULL,
Prod_QOH INTEGER NOT NULL,
Prod_Min INTEGER,
Prod_Price DECIMAL (5,2) NOT NULL,
Prod_Discount INTEGER,
Cntry_Origin CHAR( 3 ) NOT NULL, -- 4
CONSTRAINT PK_Product PRIMARY KEY( Prod_Code ),
CONSTRAINT FK_Cntry_Origin FOREIGN KEY( Cntry_Origin ) REFERENCES Country_T( Cntry_Code ) -- 4
);

CREATE TABLE Invoice_Line_T (
Invoice_Number CHAR(6),
Invoice_Line INTEGER,
Prod_Code CHAR(5) NOT NULL,
Line_units INTEGER NOT NULL,
Line_Price DECIMAL (5,2) NOT NULL,
CONSTRAINT PK_Invoice_Line PRIMARY KEY( Invoice_Number, Invoice_Line ),
CONSTRAINT FK1_Invoice_Line FOREIGN KEY( Invoice_Number ) REFERENCES Invoice_T( Invoice_Number ),
CONSTRAINT FK2_Invoice_Line FOREIGN KEY( Prod_Code ) REFERENCES Product_T( Prod_Code )
);


-- code to insert if not respecting conventional table creation order as defined by constraints

-- 2
--ALTER TABLE City_T
--ADD CONSTRAINT FK_Cntry_Code FOREIGN KEY ( Cntry_Code ) REFERENCES Country_T (Cntry_Code );

-- 3
--ALTER TABLE Customer_T
--ADD Cust_Country CHAR ( 3 ) NOT NULL,
--ADD CONSTRAINT FK_Cust_Country FOREIGN KEY ( Cntry_Code ) REFERENCES Country_T( Cntry_Code );

-- 4
--ALTER TABLE Product_T
--ADD Cntry_Origin CHAR ( 3 ) NOT NULL,
--ADD CONSTRAINT FK_Cntry_Origin FOREIGN KEY( Cntry_Origin ) REFERENCES Country_T( Cntry_Code );

--eof: Inventory-DDL.sql
