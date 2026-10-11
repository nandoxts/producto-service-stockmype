package pe.edu.cibertec.stockmype.producto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.stockmype.producto.client.dto.CredencialesDTO;
import pe.edu.cibertec.stockmype.producto.client.dto.UsuarioDTO;

/**
 * Cliente declarativo hacia auth-service. El name es el registrado en Eureka,
 * así que no hay URLs fijas: Spring Cloud LoadBalancer resuelve host y puerto.
 */
@FeignClient(name = "auth-service-stockmype")
public interface AuthClient {

    // 200 si las credenciales son correctas | 401 si no
    @PostMapping("/api/auth/login")
    UsuarioDTO validar(@RequestBody CredencialesDTO credenciales);
}
