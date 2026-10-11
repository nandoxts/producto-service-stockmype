package pe.edu.cibertec.stockmype.producto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.stockmype.producto.dto.ProductoRequest;
import pe.edu.cibertec.stockmype.producto.dto.ProductoResponse;
import pe.edu.cibertec.stockmype.producto.entity.Producto;
import pe.edu.cibertec.stockmype.producto.exception.RecursoDuplicadoException;
import pe.edu.cibertec.stockmype.producto.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.stockmype.producto.repository.ProductoRepository;
import pe.edu.cibertec.stockmype.producto.service.ProductoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return toResponse(obtener(id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        String codigo = request.codigo().trim().toUpperCase();
        if (productoRepository.existsByCodigo(codigo)) {
            throw new RecursoDuplicadoException("Ya existe un producto con el código " + codigo);
        }

        Producto producto = new Producto();
        copiar(request, producto);
        return toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = obtener(id);

        String codigo = request.codigo().trim().toUpperCase();
        if (productoRepository.existsByCodigoAndIdNot(codigo, id)) {
            throw new RecursoDuplicadoException("Ya existe otro producto con el código " + codigo);
        }

        copiar(request, producto);
        return toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtener(id);
        productoRepository.delete(producto);
    }

    // ===== helpers =====

    private Producto obtener(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el producto con id " + id));
    }

    private void copiar(ProductoRequest request, Producto producto) {
        producto.setCodigo(request.codigo().trim().toUpperCase());
        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion() == null ? null : request.descripcion().trim());
        producto.setCategoria(request.categoria().trim());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getDescripcion(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.getFechaRegistro());
    }
}
