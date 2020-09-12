-- CST 8215
-- Assignement 1
-- Students: Damir Omelic, Samuel Ebba, Jasdeep Kaur
-- Section 300
-- Professor: Sanaa Issa
-- Question: 10

-- 10.1
SELECT DISTINCT country_t.cntry_name AS name
  FROM country_t LEFT JOIN customer_t
    ON country_t.cntry_code = customer_t.cust_country
      WHERE cust_id IS NULL
        ORDER BY name ASC;

-- 10.2
SELECT DISTINCT country_t.cntry_name AS name
  FROM country_t LEFT JOIN product_t 
    ON country_t.cntry_code = product_t.cntry_origin
      LEFT JOIN invoice_line_t USING (prod_code)
        WHERE product_t.prod_code NOT IN (
          SELECT invoice_line_t.prod_code
            FROM invoice_line_t
          )
            ORDER BY name ASC;
