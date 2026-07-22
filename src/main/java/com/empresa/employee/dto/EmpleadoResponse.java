package com.empresa.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpleadoResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaIngreso;
    private BigDecimal salario;
    private Long departamentoId;
    private String departamentoNombre;

    public EmpleadoResponse() {
    }

    public EmpleadoResponse(Long id, String nombre, String apellido, String email,
                            LocalDate fechaIngreso, BigDecimal salario,
                            Long departamentoId, String departamentoNombre) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.departamentoId = departamentoId;
        this.departamentoNombre = departamentoNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getDepartamentoNombre() {
        return departamentoNombre;
    }

    public void setDepartamentoNombre(String departamentoNombre) {
        this.departamentoNombre = departamentoNombre;
    }
}
