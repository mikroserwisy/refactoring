package pl.sages.platform.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> getById(Long id);

    @Query("select a.products from Account a where a.id = :accountId")
    List<Product> getProducts(Long accountId);

    Optional<Product> getFirstBy();

}
