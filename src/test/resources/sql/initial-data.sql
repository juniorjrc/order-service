INSERT INTO supplier (supplier_id, name)
VALUES
(1, 'AMBEV'),
(2, 'COCA_COLA'),
(3, 'HEINEKEN');

INSERT INTO customer (customer_id, customer_name, financial_manager, address, private_key)
VALUES
(1, 'Adega da Luz', 'João Bosco', 'Rua 123', '27678aff-e93b-4047-9e18-431a78688bf3'),
(2, 'Adega Boa Bebida', 'Guilherme Vaz', 'Rua 345', 'e689f427-e827-4e13-bee1-31f5e38707f8');

INSERT INTO product (product_id, name, description, product_value, final_product_value, supplier_id) VALUES
(1, 'Skol', 'Cerveja Pilsen Lata 350ml', 5.00, 5.00 - 10.00, 1),
(2, 'Brahma', 'Cerveja Pilsen Lata 350ml', 5.50, 5.50 - 10.00, 1),
(3, 'Guaraná Antarctica', 'Refrigerante Guaraná 2L', 8.00, 8.00 - 10.00, 1),
(4, 'Pepsi', 'Refrigerante Cola 2L', 7.50, 7.50 - 10.00, 1),
(5, 'H2OH!', 'Refrigerante de Limão 500ml', 4.00, 4.00 - 10.00, 1),

(6, 'Coca-Cola', 'Refrigerante Cola 2L', 9.00, 9.00 - 15.00, 2),
(7, 'Fanta Laranja', 'Refrigerante Laranja 2L', 8.50, 8.50 - 15.00, 2),
(8, 'Sprite', 'Refrigerante Limão 2L', 8.00, 8.00 - 15.00, 2),
(9, 'Del Valle', 'Suco de Uva Integral 1L', 10.00, 10.00 - 15.00, 2),
(10, 'Powerade', 'Bebida Isotônica 500ml', 6.50, 6.50 - 15.00, 2),

(11, 'Heineken', 'Cerveja Puro Malte Long Neck 330ml', 8.00, 8.00 - 15.50, 3),
(12, 'Amstel', 'Cerveja Puro Malte Lata 350ml', 6.00, 6.00 - 15.50, 3),
(13, 'Eisenbahn', 'Cerveja Puro Malte 600ml', 12.00, 12.00 - 15.50, 3),
(14, 'Tiger', 'Cerveja Lager Lata 350ml', 7.00, 7.00 - 15.50, 3),
(15, 'Desperados', 'Cerveja com toque de tequila 330ml', 10.00, 10.00 - 15.50, 3);

INSERT INTO orders (order_id, customer_id, order_value, status, order_final_value)
VALUES (1000, 1, 5, 'PROCESSED', 10.00);

INSERT INTO product_order (product_id, order_id)
VALUES
(1, 1000);