package com.sistemamedico.app.config;

import com.sistemamedico.app.model.Paciente;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return usuarioRepository.findByUsername(username)
                .map(this::construirDesdeUsuario)
                .or(() -> pacienteRepository.findByUsername(username).map(this::construirDesdePaciente))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    private UserDetails construirDesdeUsuario(Usuario u) {
        return new User(u.getUsername(), u.getPasswordHash(), u.isActivo(), true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().getNombre().toUpperCase())));
    }

    private UserDetails construirDesdePaciente(Paciente p) {
        return new User(p.getUsername(), p.getPasswordHash(), p.isActivo(), true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")));
    }
}