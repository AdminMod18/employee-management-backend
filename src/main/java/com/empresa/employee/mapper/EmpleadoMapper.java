package com.empresa.employee.mapper;

import com.empresa.employee.dto.DepartamentoResponse;
import com.empresa.employee.dto.EmpleadoRequest;
import com.empresa.employee.dto.EmpleadoResponse;
import com.empresa.employee.entity.Departamento;
import com.empresa.employee.entity.Empleado;

public final class EmpleadoMapper {

    private EmpleadoMapper() {
    }

    public static EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getEmail(),
                empleado.getFechaIngreso(),
                empleado.getSalario(),
                empleado.getDepartamento().getId(),
                empleado.getDepartamento().getNombre()
        );
    }

    public static DepartamentoResponse toResponse(Departamento departamento) {
        return new DepartamentoResponse(departamento.getId(), departamento.getNombre());
    }

    public static void applyRequest(Empleado empleado, EmpleadoRequest request, Departamento departamento) {
        empleado.setNombre(request.getNombre().trim());
        empleado.setApellido(request.getApellido().trim());
        empleado.setEmail(request.getEmail().trim().toLowerCase());
        empleado.setFechaIngreso(request.getFechaIngreso());
        empleado.setSalario(request.getSalario());
        empleado.setDepartamento(departamento);
    }
}
