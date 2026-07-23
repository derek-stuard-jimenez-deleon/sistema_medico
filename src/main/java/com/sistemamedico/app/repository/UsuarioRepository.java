package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByDpi(String dpi);

    // Busqueda con paginacion (RN-CU01-02: 20 registros/pagina)
    Page<Usuario> findByNombreCompletoContainingIgnoreCase(String nombre, Pageable pageable);
}