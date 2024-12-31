package br.com.judev.backend.repositories;

import br.com.judev.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart , Long> {
    Optional<Cart> findByUserId(Long userId);

}
