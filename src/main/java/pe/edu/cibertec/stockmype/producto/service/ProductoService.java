package pe.edu.cibertec.stockmype.producto.service;

import pe.edu.cibertec.stockmype.producto.dto.ProductoRequest;
import pe.edu.cibertec.stockmype.producto.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar();

    ProductoResponse buscarPorId(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);
}
