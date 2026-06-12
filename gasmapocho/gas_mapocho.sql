CREATE DATABASE IF NOT EXISTS bd_gasmapocho;
USE bd_gasmapocho;

-- =============================================
-- TABLA: roles
-- =============================================
INSERT INTO roles (nombre_rol) VALUES
('ADMINISTRADOR'),
('JEFE_ALMACEN'),
('ASISTENTE'),
('AUDITOR');

-- =============================================
-- TABLA: usuarios
-- Password de todos: admin123
-- =============================================
INSERT INTO usuarios (nombre, email, password, id_rol, activo) VALUES
('Admin Principal',    'admin@mapocho.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9q', 1, true),
('Jefe Almacen',       'jefe@mapocho.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9q', 2, true),
('Asistente Almacen',  'asistente@mapocho.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9q', 3, true),
('Auditor',            'auditor@mapocho.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9q', 4, true);

USE bd_gasmapocho;

-- Primero verificar longitud actual de la columna
ALTER TABLE usuarios MODIFY COLUMN password VARCHAR(255) NOT NULL;

-- Luego actualizar el hash correctamente
UPDATE usuarios SET password = '$2a$10$TI0SlONu2lpywpK/zRe70eMvpa.UEtcm25OGMxdkfimat9j85KyPO'
WHERE email = 'admin@mapocho.com';

UPDATE usuarios SET password = '$2a$10$TI0SlONu2lpywpK/zRe70eMvpa.UEtcm25OGMxdkfimat9j85KyPO'
WHERE email = 'jefe@mapocho.com';

UPDATE usuarios SET password = '$2a$10$TI0SlONu2lpywpK/zRe70eMvpa.UEtcm25OGMxdkfimat9j85KyPO'
WHERE email = 'asistente@mapocho.com';

UPDATE usuarios SET password = '$2a$10$TI0SlONu2lpywpK/zRe70eMvpa.UEtcm25OGMxdkfimat9j85KyPO'
WHERE email = 'auditor@mapocho.com';

-- Verificar que quedó bien
SELECT email, LENGTH(password) as largo FROM usuarios;

-- =============================================
-- TABLA: proveedores
-- =============================================
INSERT INTO proveedores (nombre, ruc, telefono, activo) VALUES
('Proveedor Repsol',  '20123456781', '987654321', true),
('Proveedor Abastible','20123456782', '987654322', true),
('Proveedor Lipigas', '20123456783', '987654323', true);

-- =============================================
-- TABLA: cilindros
-- Un registro por tipo+estado = stock agrupado
-- =============================================
INSERT INTO cilindros (tipo, estado, cantidad) VALUES
-- Cilindros 5kg
('KG_5',  'LLENO',  40),
('KG_5',  'VACIO',  10),
('KG_5',  'VENDIDO', 5),
('KG_5',  'VIEJO',   2),
-- Cilindros 11kg
('KG_11', 'LLENO',  30),
('KG_11', 'VACIO',   8),
('KG_11', 'VENDIDO', 3),
('KG_11', 'VIEJO',   1),
-- Cilindros 15kg
('KG_15', 'LLENO',  20),
('KG_15', 'VACIO',   5),
('KG_15', 'VENDIDO', 2),
('KG_15', 'VIEJO',   1);

-- =============================================
-- TABLA: movimientos (datos de ejemplo)
-- =============================================
INSERT INTO movimientos (tipo, id_cilindro, cantidad, kg_por_unidad, id_proveedor, id_usuario, fecha_hora, observacion) VALUES
-- Entradas desde proveedor (id_cilindro = llenos)
('ENTRADA', 1,  20, 5.0,  1, 1, '2026-06-01 08:00:00', 'Ingreso inicial cilindros 5kg'),
('ENTRADA', 5,  15, 11.0, 2, 1, '2026-06-01 08:30:00', 'Ingreso inicial cilindros 11kg'),
('ENTRADA', 9,  10, 15.0, 3, 1, '2026-06-01 09:00:00', 'Ingreso inicial cilindros 15kg'),
-- Salidas / ventas (id_cilindro = vendidos)
('SALIDA',  3,   5, NULL, NULL, 2, '2026-06-02 10:00:00', 'Venta cilindros 5kg'),
('SALIDA',  7,   3, NULL, NULL, 2, '2026-06-02 10:30:00', 'Venta cilindros 11kg'),
('SALIDA',  11,  2, NULL, NULL, 2, '2026-06-02 11:00:00', 'Venta cilindros 15kg'),
-- Devoluciones (id_cilindro = vacios)
('DEVOLUCION', 2, 5, NULL, NULL, 3, '2026-06-03 14:00:00', 'Devolución cilindros vacíos 5kg'),
('DEVOLUCION', 6, 3, NULL, NULL, 3, '2026-06-03 14:30:00', 'Devolución cilindros vacíos 11kg');