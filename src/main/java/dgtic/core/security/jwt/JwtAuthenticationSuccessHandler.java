package dgtic.core.security.jwt;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = jwtUtil.generateToken(authentication.getName());

        response.addHeader("Set-Cookie", "JWT=" + token + "; HttpOnly; Path=/; Max-Age=86400");
        response.sendRedirect("/index");
    }
}


