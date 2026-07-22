package com.empresa.employee.repository;

import com.empresa.employee.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>, JpaSpecificationExecutor<Empleado> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT e FROM Empleado e JOIN FETCH e.departamento ORDER BY e.id")
    List<Empleado> findAllWithDepartamento();

    @Query("SELECT e FROM Empleado e JOIN FETCH e.departamento WHERE e.id = :id")
    Optional<Empleado> findByIdWithDepartamento(@Param("id") Long id);
}
