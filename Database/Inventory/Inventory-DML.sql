-- Inventory-DML.sql
-- script to populate Inventory Database

-- CST 8215
-- Assignement 1
-- Students: Damir Omelic, Samuel Ebba, Jasdeep Kaur
-- Section 300
-- Professor: Sanaa Issa
-- Questions: 5, 6

-- inserting values follows table creation order

-- 5
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('SRB', 'Serbia', 7344847);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('RUS', 'Russian Federation', 144192450);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('MEX', 'Mexico', 119530753);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('DZA', 'Algeria', 40400000);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('CHN', 'China', 1376049000);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('CHL', 'Chile', 18006407);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('CAN', 'Canada', 36155487);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('NOR', 'Norway', 4478500);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('IND', 'India', 1013662000);
INSERT INTO Country_T (Cntry_Code, Cntry_Name, Cntry_Population) VALUES ('IRQ', 'Iraq', 29671605);

-- 5
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (001, 'Belgrade', 'SRB', 1204000);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (002, 'Moscow', 'RUS', 8389200);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (003, 'Toronto', 'CAN', 688275);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (004, 'Mexico City', 'MEX', 8851080);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (005, 'Ecatepec', 'MEX', 1655015);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (006, 'Hong Kong', 'CHN', 7409800);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (007, 'Beijing', 'CHN', 21707000);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (008, 'Mumbai', 'IND', 11978450);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (009, 'Delhi', 'IND', 1103455);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (010, 'Arendal', 'NOR', 39826);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (011, 'Basra', 'IRQ', 1225793);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (012, 'Baghdad', 'IRQ', 1211934);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (013, 'Santiago', 'CHL', 4657000);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (014, 'Puente Alto', 'CHL', 492603);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (015, 'El Bosque', 'CHL', 175594);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (017, 'Algiers', 'DZA', 2988145);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (018, 'Oran', 'DZA', 1560329);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (019, 'Novi Sad', 'SRB', 277522);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (020, 'Balgalore', 'IND', 8443675);
INSERT INTO City_T (City_ID, City_Name, Cntry_Code, City_Population) VALUES (021, 'Hyderabad', 'IND', 6993262);

--6
INSERT INTO Customer_T VALUES ('C001', 'Damir', 'Omelic', '613-727-4723', '1385 Woodroffe Ave', 'Ottawa'   ,'ON','K2G1V8', 0, 'SRB');
INSERT INTO Customer_T VALUES ('C002', 'Samuel', 'Ebba', '613-727-4723', '1385 Woodroffe Ave', 'Ottawa'   ,'ON','K2G1V8', 0, 'NOR');
INSERT INTO Customer_T VALUES ('C003', 'Kaur', 'Jasdeep', '613-727-4723', '1385 Woodroffe Ave', 'Ottawa'   ,'ON','K2G1V8', 0, 'IND');
INSERT INTO Customer_T VALUES ('C004', 'Katherine', 'McKinley', '613-727-4723', '854 Younge Street' , 'Toronto'  ,'ON','K2G1V8', 0, 'CAN');
INSERT INTO Customer_T VALUES ('C005', 'Poncho', 'Garcias', '613-727-4723', '264 Main Street'   , 'Kitchener','ON','K2G1V8', 0, 'MEX');
INSERT INTO Customer_T VALUES ('C006', 'Xuan', 'Young', '613-727-4723', '357 Rue Catherine' , 'Montreal' ,'ON','K2G1V8', 0, 'CHN');
INSERT INTO Customer_T VALUES ('C007', 'Ono', 'Deloro', '613-727-4723', '101 Lebreton Street', 'Ottawa', 'ON', 'K2G1V8', 0, 'DZA');


INSERT INTO Invoice_T VALUES ('I23001', 'C001', '2011-02-15');
INSERT INTO Invoice_T VALUES ('I23002', 'C001', '2011-04-25');
INSERT INTO Invoice_T VALUES ('I23003', 'C004', '2011-06-12');
INSERT INTO Invoice_T VALUES ('I23004', 'C002', '2011-07-08');
INSERT INTO Invoice_T VALUES ('I23005', 'C005', '2011-08-24');
INSERT INTO Invoice_T VALUES ('I23006', 'C006', '2011-09-07');
INSERT INTO Invoice_T VALUES ('I23007', 'C006', '2010-12-28');
INSERT INTO Invoice_T VALUES ('I23008', 'C006', '2011-12-15');

--6
INSERT INTO Product_T VALUES ('P2011', 'Compac Presario', '2011-02-14', 20, 5, 499.99, 0, 'RUS');
INSERT INTO Product_T VALUES ('P2012', 'HP laptop', '2010-09-25', 40, 5, 529.99, 0, 'CHL');
INSERT INTO Product_T VALUES ('P2013', 'Samsung LCD', '2010-02-15', 22, 8, 329.99, 0, 'MEX');
INSERT INTO Product_T VALUES ('P2014', 'Brother Network All-In-One Laser Printer', '2010-10-10', 50, 10, 159.99, 0, 'MEX');
INSERT INTO Product_T VALUES ('P2015', 'Western Digital External Hard drive', '2010-04-08', 30, 10, 149.99, NULL, 'IND');
INSERT INTO Product_T VALUES ('P2016', 'Apple iPad 2 with Wi-Fi + 3G', '2011-02-23', 90, 200, 849.00, 0, 'NOR');
INSERT INTO Product_T VALUES ('P2017', 'iPAD 2 Smart Cover', '2011-02-14', 70, 10, 45.00, 10, 'CAN');
INSERT INTO Product_T VALUES ('P2018', 'ThinkPad X270', '2018-04-18', 10, 2, 119.0, 10, 'SRB');


INSERT INTO Invoice_Line_T VALUES ('I23001', 1, 'P2011', 1, 650.75); 
INSERT INTO Invoice_Line_T VALUES ('I23001', 2, 'P2014', 3, 159.99);
INSERT INTO Invoice_Line_T VALUES ('I23002', 1, 'P2012', 1, 529.99);
INSERT INTO Invoice_Line_T VALUES ('I23003', 1, 'P2015', 3, 140.75);
INSERT INTO Invoice_Line_T VALUES ('I23004', 1, 'P2014', 1, 159.99);
INSERT INTO Invoice_Line_T VALUES ('I23005', 1, 'P2016', 1, 798.99);
INSERT INTO Invoice_Line_T VALUES ('I23006', 1, 'P2011', 1, 499.99);
INSERT INTO Invoice_Line_T VALUES ('I23007', 1, 'P2012', 1, 529.99);
INSERT INTO Invoice_Line_T VALUES ('I23008', 1, 'P2016', 3, 689.00);
INSERT INTO Invoice_Line_T VALUES ('I23008', 2, 'P2017', 3, 35.99);

-- eof: Inventory-DML.sql
