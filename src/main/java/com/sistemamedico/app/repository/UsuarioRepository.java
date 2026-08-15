package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su nombre de usuario. Correcto.
    Optional<Usuario> findByUsername(String username);

    // Busca usuarios por nombre de usuario con paginación
    Page<Usuario> findByUsernameContaining(String username, Pageable pageable);

    // Verifica si un nombre de usuario ya existe. Correcto.
    boolean existsByUsername(String username);

    // Verifica si un DPI ya existe. Correcto.
    boolean existsByDpi(String dpi);

    // Busca médicos por rol y especialidad
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rolNombre AND u.especialidad.id = :especialidadId")
    List<Usuario> findByRolNombreAndEspecialidadId(@Param("rolNombre") String rolNombre, 
                                                    @Param("especialidadId") Long especialidadId);

    // Nuevo método para buscar Usuario por ID y cargar eager la especialidad y sucursal
    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol JOIN FETCH u.sucursal LEFT JOIN FETCH u.especialidad WHERE u.id = :id")
    Optional<Usuario> findByIdWithEagerRelations(@Param("id") Long id);
}