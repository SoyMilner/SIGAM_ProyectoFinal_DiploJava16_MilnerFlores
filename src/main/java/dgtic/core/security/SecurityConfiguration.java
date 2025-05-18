package dgtic.core.security;

import dgtic.core.security.jwt.JWTAuthenticationFilter;
import dgtic.core.security.jwt.JwtAuthenticationSuccessHandler;
import dgtic.core.security.service.MaestroUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MaestroUserDetailsService maestroUserDetailsService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/css/**", "/bootstrap/**", "/iconos/**").permitAll() // Recursos estáticos
                        .requestMatchers("/login", "/").permitAll() // Login y raíz accesibles sin autenticación
                        .anyRequest().authenticated() // Cualquier otro endpoint requiere autenticación
                )

                .formLogin(login -> login
                        .loginPage("/login") //new
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index")
                        .successHandler(jwtAuthenticationSuccessHandler) // Genera JWT al autenticar
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Redirige al login con el parámetro "logout"
                        .deleteCookies("JWT")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login?accessDenied=true");
                        })
                )
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Validar JWT
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder(11, new SecureRandom());
        //return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 12);
    }

//    @Bean
//    UserDetailsManager inMemoryUserDetailsManager() {
//        var user1 = User.withUsername("user@correo.com").password("{noop}password").build();
//        return new InMemoryUserDetailsManager(user1);
//    }


}
