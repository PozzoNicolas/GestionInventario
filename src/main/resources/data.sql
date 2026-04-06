INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO CategoriaProducto(id, nombre) VALUES(1,'Caja');
INSERT INTO CategoriaProducto(id, nombre) VALUES(2,'Baño');
INSERT INTO CategoriaProducto(id, nombre) VALUES(3,'Hermetico');
INSERT INTO CategoriaProducto(id, nombre) VALUES(4,'Cocina');
INSERT INTO CategoriaProducto(id, nombre) VALUES(5,'Mobiliario');

INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '9242', 'Col box 42lts', 1, 20000.0, 10,'9242-za.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '9418', 'Mega Col 68lts', 1, 27000.0, 5, '9418-z.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '9338', 'Col box largo 38lts', 1, 17500.0, 8, '9338-1.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '2066', 'Cepillo con base', 2, 9800.0, 2, '2065-1.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '70', 'Contenedor ultra rect.', 3, 2800.0, 24, '70-z.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '225', 'Colador flor', 4, 2400.0, 12,'225-z.jpg');
INSERT INTO Producto (id, sku, nombre, id_categoria, precio, stockActual, imagen) VALUES(null, '9334', 'Armario util. clasico', 5, 800000.0, 1,'9334.jpg');



