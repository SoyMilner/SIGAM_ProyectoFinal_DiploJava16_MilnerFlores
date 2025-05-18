package dgtic.core.security.service;

import dgtic.core.model.Maestro;
import dgtic.core.repository.MaestroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class MaestroUserDetailsService implements UserDetailsService {

    @Autowired
    private MaestroRepository maestroRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Maestro maestro = maestroRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Maestro no encontrado: " + correo));

        return User.withUsername(maestro.getCorreo())
                .password(maestro.getContrasena()) // Encriptada con BCrypt
                .roles("MAESTRO")
                .build();
    }
}
