package com.empresa.employee.controller;

import com.empresa.employee.dto.EmpleadoRequest;
import com.empresa.employee.dto.EmpleadoResponse;
import com.empresa.employee.dto.PageResponse;
import com.empresa.employee.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmpleadoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento) {
        return ResponseEntity.ok(empleadoService.listar(page, size, sort, nombre, departamento));
    }

    @GetMapping("/buscar")
    public ResponseEntity<PageResponse<EmpleadoResponse>> buscar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento) {
        return ResponseEntity.ok(empleadoService.listar(page, size, sort, nombre, departamento));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EmpleadoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse creado = empleadoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<EmpleadoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequest request) {
        return ResponseEntity.ok(empleadoService.actualizar(id, request));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
