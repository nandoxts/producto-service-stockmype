package pe.edu.cibertec.stockmype.producto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import pe.edu.cibertec.stockmype.producto.security.AuthServiceAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthServiceAuthenticationProvider authServiceProvider;

    // CORS lo resuelve el api-gateway: aquí no se configura
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)          // API REST
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authServiceProvider)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(jsonEntryPoint()))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jsonEntryPoint()));

        return http.build();
    }

    // 401 en JSON y SIN header WWW-Authenticate (evita el popup del navegador)
    private AuthenticationEntryPoint jsonEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"mensaje\":\"Debes iniciar sesión\"}");
        };
    }
}
