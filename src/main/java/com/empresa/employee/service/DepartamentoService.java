package com.empresa.employee.service;

import com.empresa.employee.dto.DepartamentoResponse;
import com.empresa.employee.entity.Departamento;
import com.empresa.employee.exception.ResourceNotFoundException;
import com.empresa.employee.mapper.EmpleadoMapper;
import com.empresa.employee.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<DepartamentoResponse> listar() {
        return departamentoRepository.findAll().stream()
                .map(EmpleadoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Departamento obtenerEntidad(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento no encontrado con id: " + id));
    }
}
