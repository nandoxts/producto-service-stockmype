package pe.edu.cibertec.stockmype.producto.security;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.stockmype.producto.client.AuthClient;
import pe.edu.cibertec.stockmype.producto.client.dto.CredencialesDTO;
import pe.edu.cibertec.stockmype.producto.client.dto.UsuarioDTO;

import java.util.List;

/**
 * Este servicio no tiene tabla de usuarios: cada petición con HTTP Basic
 * se valida preguntándole a auth-service por Feign.
 */
@Component
@RequiredArgsConstructor
public class AuthServiceAuthenticationProvider implements AuthenticationProvider {

    private final AuthClient authClient;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        try {
            UsuarioDTO usuario = authClient.validar(new CredencialesDTO(username, password));
            return UsernamePasswordAuthenticationToken.authenticated(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol())));
        } catch (FeignException.Unauthorized | FeignException.BadRequest e) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        } catch (FeignException e) {
            throw new AuthenticationServiceException("auth-service no está disponible", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
