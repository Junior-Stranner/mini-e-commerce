package br.com.judev.backend.repositories;

import br.com.judev.backend.dto.requests.ProductListDTO;
import br.com.judev.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new br.com.judev.backend.dto.requests.ProductListDTO(p.id, p.name, p.description, p.price, p.quantity, p.image) FROM Product p")
    Page<ProductListDTO> findAllWithoutComments(Pageable pageable);
}
