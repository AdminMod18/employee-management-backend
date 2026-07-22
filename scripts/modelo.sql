-- Script de referencia del modelo (Oracle / H2)
-- En la aplicación las tablas se crean con ddl-auto=update
-- y los datos se cargan con DataLoader (CommandLineRunner).

-- DEPARTAMENTO
-- id (PK), nombre

-- EMPLEADO
-- id (PK), nombre, apellido, email (unique), fecha_ingreso, salario, departamento_id (FK)

-- Datos sugeridos (equivalentes a los del enunciado):
-- Departamentos: Tecnología, Recursos Humanos, Ventas
-- Empleados: Camila, Juan, María, Andrés, Laura
