-- CST 8215
-- Assignement 1
-- Students: Damir Omelic, Samuel Ebba, Jasdeep Kaur
-- Section 300
-- Professor: Sanaa Issa
-- Question: 8


-- countries with sold discounted products

CREATE VIEW DiscountedItems AS
SELECT  country_t.cntry_name AS country, 
        product_t.prod_description AS product, 
        product_t.prod_discount AS discount
  FROM country_t LEFT JOIN product_t 
    ON country_t.cntry_code = product_t.cntry_origin
      LEFT JOIN invoice_line_t USING (prod_code)
        WHERE product_t.prod_discount > 0
          AND product_t.prod_code IN (
            SELECT invoice_line_t.prod_code
              FROM invoice_line_t
          )
            ORDER BY country ASC, product ASC;


-- the top five sales from cities where at least at least one product is bought

CREATE VIEW BestSelling As
SELECT DISTINCT   city_t.city_name AS city, 
                  product_t.prod_price AS price
  FROM city_t LEFT JOIN country_t USING(cntry_code)
    LEFT JOIN product_t ON country_t.cntry_code = product_t.cntry_origin
      LEFT JOIN invoice_line_t ON product_t.prod_code = invoice_line_t.prod_code
        WHERE product_t.prod_code IN (
          SELECT invoice_line_t.prod_code
          FROM invoice_line_t
        )
          GROUP BY city_t.city_name, product_t.prod_price
            HAVING COUNT(invoice_line_t.prod_code) > 1
              ORDER BY price DESC, city ASC
                LIMIT 5;

