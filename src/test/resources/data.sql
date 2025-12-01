INSERT INTO users (name, email, age) VALUES
('Alice', 'alice@example.com', 25),
('Bob', 'bob@example.com', 30),
('Charlie', 'charlie@example.com', 28);

INSERT INTO products (name, description, price, stock) VALUES 
('Laptop', 'High-performance laptop', 1200.00, 50),
('Mouse', 'Wireless mouse', 25.00, 200),
('Keyboard', 'Mechanical keyboard', 80.00, 100);

INSERT INTO orders (user_id, product_name, price, quantity) VALUES 
(1, 'Laptop', 1200.00, 1),
(2, 'Mouse', 25.00, 2),
(3, 'Keyboard', 80.00, 1);