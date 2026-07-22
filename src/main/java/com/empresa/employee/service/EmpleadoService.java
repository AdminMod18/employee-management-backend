package com.empresa.employee.service;

import com.empresa.employee.dto.EmpleadoRequest;
import com.empresa.employee.dto.EmpleadoResponse;
import com.empresa.employee.dto.PageResponse;
import com.empresa.employee.entity.Departamento;
import com.empresa.employee.entity.Empleado;
import com.empresa.employee.exception.BusinessException;
import com.empresa.employee.exception.NoChangesException;
import com.empresa.employee.exception.ResourceNotFoundException;
import com.empresa.employee.mapper.EmpleadoMapper;
import com.empresa.employee.repository.EmpleadoRepository;
import com.empresa.employee.repository.EmpleadoSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmpleadoService {

    private static final Set<String> SORT_PERMITIDOS = Set.of(
            "id", "nombre", "apellido", "email", "fechaIngreso", "salario", "departamentoNombre"
    );

    private static final Map<String, String> SORT_MAPEO = Map.of(
            "departamentoNombre", "departamento.nombre"
    );

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoService departamentoService;

    public EmpleadoService(EmpleadoRepository empleadoRepository, DepartamentoService departamentoService) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoService = departamentoService;
    }

    public PageResponse<EmpleadoResponse> listar(int page, int size, String sort, String nombre, String departamento) {
        Pageable pageable = construirPageable(page, size, sort);
        Page<Empleado> resultado = empleadoRepository.findAll(
                EmpleadoSpecifications.conFiltros(nombre, departamento),
                pageable
        );

        List<EmpleadoResponse> content = resultado.getContent().stream()
                .map(EmpleadoMapper::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isFirst(),
                resultado.isLast(),
                pageable.getSort().toString()
        );
    }

    public EmpleadoResponse obtenerPorId(Long id) {
        Empleado empleado = empleadoRepository.findByIdWithDepartamento(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return EmpleadoMapper.toResponse(empleado);
    }

    @Transactional
    public EmpleadoResponse crear(EmpleadoRequest request) {
        validarEmailDisponible(request.getEmail(), null);
        Departamento departamento = departamentoService.obtenerEntidad(request.getDepartamentoId());

        Empleado empleado = new Empleado();
        EmpleadoMapper.applyRequest(empleado, request, departamento);

        Empleado guardado = empleadoRepository.save(empleado);
        return EmpleadoMapper.toResponse(guardado);
    }

    @Transactional
    public EmpleadoResponse actualizar(Long id, EmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findByIdWithDepartamento(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        if (sinCambios(empleado, request)) {
            throw new NoChangesException(
                    "No se detectaron cambios respecto a los datos actuales. El empleado se mantiene igual."
            );
        }

        validarEmailDisponible(request.getEmail(), id);
        Departamento departamento = departamentoService.obtenerEntidad(request.getDepartamentoId());
        EmpleadoMapper.applyRequest(empleado, request, departamento);

        return EmpleadoMapper.toResponse(empleado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado no encontrado con id: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    private Pageable construirPageable(int page, int size, String sort) {
        int pagina = Math.max(page, 0);
        int tamanio = size <= 0 ? 5 : Math.min(size, 50);
        Sort orden = parsearSort(sort);
        return PageRequest.of(pagina, tamanio, orden);
    }

    private Sort parsearSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        String[] partes = sort.split(",");
        String propiedad = partes[0].trim();
        if (!SORT_PERMITIDOS.contains(propiedad)) {
            propiedad = "id";
        }

        String propiedadEntidad = SORT_MAPEO.getOrDefault(propiedad, propiedad);
        Sort.Direction direccion = Sort.Direction.ASC;
        if (partes.length > 1 && "desc".equalsIgnoreCase(partes[1].trim())) {
            direccion = Sort.Direction.DESC;
        }

        return Sort.by(direccion, propiedadEntidad);
    }

    private void validarEmailDisponible(String email, Long idActual) {
        String emailNormalizado = email.trim().toLowerCase();
        boolean existe = idActual == null
                ? empleadoRepository.existsByEmail(emailNormalizado)
                : empleadoRepository.existsByEmailAndIdNot(emailNormalizado, idActual);

        if (existe) {
            throw new BusinessException("Ya existe un empleado con el email: " + emailNormalizado);
        }
    }

    private boolean sinCambios(Empleado actual, EmpleadoRequest request) {
        String nombre = request.getNombre() == null ? "" : request.getNombre().trim();
        String apellido = request.getApellido() == null ? "" : request.getApellido().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();

        return actual.getNombre().equals(nombre)
                && actual.getApellido().equals(apellido)
                && actual.getEmail().equalsIgnoreCase(email)
                && actual.getFechaIngreso().equals(request.getFechaIngreso())
                && actual.getSalario().compareTo(request.getSalario()) == 0
                && actual.getDepartamento().getId().equals(request.getDepartamentoId());
    }
}
