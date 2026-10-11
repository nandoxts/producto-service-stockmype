package pe.edu.cibertec.stockmype.producto.client.dto;

// Respuesta del login de auth-service
public record UsuarioDTO(String username, String nombre, String rol, String mensaje) {
}
