package com.sistemamedico.app.config;

import com.sistemamedico.app.model.Paciente;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            return construirDesdeUsuario(usuarioOpt.get());
        }

        Optional<Paciente> pacienteOpt = pacienteRepository.findByUsername(username);
        if (pacienteOpt.isPresent()) {
            return construirDesdePaciente(pacienteOpt.get());
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }

    private UserDetails construirDesdeUsuario(Usuario u) {
        if (u.getBloqueadoHasta() != null && u.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            throw new LockedException("La cuenta está bloqueada temporalmente. Intente de nuevo más tarde.");
        }
        if (!u.isActivo()) { // Asegurarse de que el usuario interno esté activo
            throw new LockedException("La cuenta de usuario interno está inactiva.");
        }
        return new User(u.getUsername(), u.getPasswordHash(), u.isActivo(), true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().getNombre().toUpperCase())));
    }

    private UserDetails construirDesdePaciente(Paciente p) {
        if (p.getBloqueadoHasta() != null && p.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            throw new LockedException("La cuenta está bloqueada temporalmente. Intente de nuevo más tarde.");
        }
        if (!p.isActivo()) { // Asegurarse de que el paciente esté activo
            throw new LockedException("La cuenta de paciente está inactiva.");
        }
        return new User(p.getUsername(), p.getPasswordHash(), p.isActivo(), true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")));
    }
}