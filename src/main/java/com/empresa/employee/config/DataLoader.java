package com.empresa.employee.config;

import com.empresa.employee.entity.Departamento;
import com.empresa.employee.entity.Empleado;
import com.empresa.employee.repository.DepartamentoRepository;
import com.empresa.employee.repository.EmpleadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadInitialData(DepartamentoRepository departamentoRepository,
                                      EmpleadoRepository empleadoRepository) {
        return args -> {
            if (departamentoRepository.count() > 0) {
                return;
            }

            Departamento tecnologia = new Departamento("Tecnología");
            Departamento rrhh = new Departamento("Recursos Humanos");
            Departamento ventas = new Departamento("Ventas");
            departamentoRepository.saveAll(List.of(tecnologia, rrhh, ventas));

            Empleado camila = crearEmpleado("Camila", "Rodríguez", "camila.rodriguez@empresa.com",
                    LocalDate.of(2022, 3, 15), new BigDecimal("3200000"), tecnologia);
            Empleado juan = crearEmpleado("Juan", "Pérez", "juan.perez@empresa.com",
                    LocalDate.of(2021, 7, 1), new BigDecimal("2900000"), tecnologia);
            Empleado maria = crearEmpleado("María", "Gómez", "maria.gomez@empresa.com",
                    LocalDate.of(2023, 1, 10), new BigDecimal("3500000"), rrhh);
            Empleado andres = crearEmpleado("Andrés", "López", "andres.lopez@empresa.com",
                    LocalDate.of(2020, 11, 20), new BigDecimal("4100000"), ventas);
            Empleado laura = crearEmpleado("Laura", "Martínez", "laura.martinez@empresa.com",
                    LocalDate.of(2023, 6, 5), new BigDecimal("3000000"), ventas);

            empleadoRepository.saveAll(List.of(camila, juan, maria, andres, laura));
        };
    }

    private Empleado crearEmpleado(String nombre, String apellido, String email,
                                   LocalDate fechaIngreso, BigDecimal salario, Departamento departamento) {
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setEmail(email);
        empleado.setFechaIngreso(fechaIngreso);
        empleado.setSalario(salario);
        empleado.setDepartamento(departamento);
        return empleado;
    }
}
