package com.empresa.employee.repository;

import com.empresa.employee.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByNombreIgnoreCase(String nombre);
}
