Online shop implemented in Java.
- 

Built With:
- 
-  Java Servlets
- Web-server: Tomcat
- JSP
- JDBS
- MySQL
- Packaging: Apache Maven
- The inner structure was created according to a N-Tier architecture and SOLID principles

A customer can:
-
- view a list of all products
- add a product to the shopping cart
- delete product from the shopping cart
- complete the order
- view his order history


An admin can:
- 
- view a list of all products
- add new product
- delete product from the product list
- delete customer (except himself)
- view order history of all customers
- delete any order

Configuration:
-
- Tomcat (Local):
Deployment - war_exploded, context address - "/"
- Database:
Run the configuration code from the init_db.sql file in your RDBMS
- Configure your connection in the /com/internet/shop/util/ConnectionUtil.java
- Register new customer. Than log-in to the shop. On the main page press "Inject test data into the DB" button. Admin will be added to your data base.
        `Username Admin01`
        `Password 1557`


