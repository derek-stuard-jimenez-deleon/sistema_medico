package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.UsuarioRequest;
import com.sistemamedico.app.dto.UsuarioResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SucursalRepository sucursalRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          SucursalRepository sucursalRepository,
                          EspecialidadRepository especialidadRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.sucursalRepository = sucursalRepository;
        this.especialidadRepository = especialidadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "El nombre de usuario " + request.getUsername() + " ya se encuentra registrado. Por favor, elija otro.");
        }
        if (request.getDpi() != null && usuarioRepository.existsByDpi(request.getDpi())) {
            throw new IllegalArgumentException("El DPI ingresado ya se encuentra registrado.");
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        // RN-CU01-14: especialidad obligatoria solo si el rol es Medico
        Especialidad especialidad = null;
        boolean esMedico = "Medico".equalsIgnoreCase(rol.getNombre());
        if (esMedico) {
            if (request.getEspecialidadId() == null) {
                throw new IllegalArgumentException("Debe seleccionar una especialidad para el médico.");
            }
            especialidad = especialidadRepository.findById(request.getEspecialidadId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setDpi(request.getDpi());
        usuario.setNit(request.getNit());
        usuario.setTelefono(request.getTelefono());
        usuario.setNumeroSeguro(request.getNumeroSeguro());
        usuario.setRol(rol);
        usuario.setSucursal(sucursal);
        usuario.setEspecialidad(especialidad);
        usuario.setActivo(request.isActivo());

        Usuario guardado = usuarioRepository.save(usuario);
        return mapearAResponse(guardado);
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));
        return mapearAResponse(usuario);
    }

    // RN-CU01-01 / RN-CU01-02: busqueda por nombre con paginacion (20/pagina se configura en el Controller)
    public Page<UsuarioResponse> buscarPorNombre(String nombre, Pageable pageable) {
        return usuarioRepository.findByNombreCompletoContainingIgnoreCase(nombre, pageable)
                .map(this::mapearAResponse);
    }

    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::mapearAResponse);
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setTelefono(request.getTelefono());
        usuario.setNumeroSeguro(request.getNumeroSeguro());
        usuario.setRol(rol);
        usuario.setSucursal(sucursal);
        usuario.setActivo(request.isActivo());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapearAResponse(actualizado);
    }

    // Borrado logico: NUNCA se hace delete fisico
    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));
        usuario.marcarEliminado(usuarioQueElimina);
        usuarioRepository.save(usuario);
    }

    private UsuarioResponse mapearAResponse(Usuario usuario) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setDpi(usuario.getDpi());
        dto.setNit(usuario.getNit());
        dto.setTelefono(usuario.getTelefono());
        dto.setNumeroSeguro(usuario.getNumeroSeguro());
        dto.setRolNombre(usuario.getRol().getNombre());
        dto.setSucursalNombre(usuario.getSucursal().getNombre());
        dto.setEspecialidadNombre(usuario.getEspecialidad() != null ? usuario.getEspecialidad().getNombre() : null);
        dto.setActivo(usuario.isActivo());
        return dto;
    }
}