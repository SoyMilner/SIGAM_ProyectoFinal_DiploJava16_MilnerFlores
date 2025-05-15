package dgtic.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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
                        //.loginProcessingUrl("/doLogin")
                        .defaultSuccessUrl("/index")
                        //.successForwardUrl("/login_success_handler")
                        //.failureForwardUrl("/login_failure_handler")
                        /*.successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                System.out.println("Logged user: " + authentication.getName());
                                response.sendRedirect("/");
                            }
                        })
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                System.out.println("Login failed");
                                System.out.println(exception);
                                response.sendRedirect("/login");
                            }
                        })*/
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Redirige al login con el parámetro "logout"
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login?accessDenied=true");
                        })
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //return new BCryptPasswordEncoder(11, new SecureRandom());
        //return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 12);
    }

    @Bean
    UserDetailsManager inMemoryUserDetailsManager() {
        var user1 = User.withUsername("user@correo.com").password("{noop}password").build();
        return new InMemoryUserDetailsManager(user1);
    }


}
