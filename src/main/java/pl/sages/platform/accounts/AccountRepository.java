package pl.sages.platform.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> getById(Long id);

    Optional<Account> getByEmail(String username);

    @Query("select count(a) from Account a join a.products p where p.id = :productId")
    long countByProduct(@Param("productId") Long productId);

}
