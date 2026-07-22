package com.empresa.employee.repository;

import com.empresa.employee.entity.Empleado;
import org.springframework.data.jpa.domain.Specification;

public final class EmpleadoSpecifications {

    private EmpleadoSpecifications() {
    }

    public static Specification<Empleado> conFiltros(String nombre, String departamento) {
        return (root, query, cb) -> {
            if (Empleado.class.equals(query.getResultType())) {
                root.fetch("departamento");
                query.distinct(true);
            }

            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (nombre != null && !nombre.isBlank()) {
                String pattern = "%" + nombre.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("nombre")), pattern, '\\'),
                        cb.like(cb.lower(root.get("apellido")), pattern, '\\')
                ));
            }

            if (departamento != null && !departamento.isBlank()) {
                String pattern = "%" + departamento.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("departamento").get("nombre")), pattern, '\\'));
            }

            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
