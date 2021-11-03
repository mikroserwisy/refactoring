package pl.sages.platform.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProductElementRepository extends JpaRepository<ProductElement, Long> {

    Optional<ProductElement> findByElementId(Long elementId);

    @Transactional
    @Modifying
    @Query("update ProductElement pe set pe.elementId = :elementId where pe.elementId = :oldElementId")
    void updateElementId(@Param("oldElementId") Long oldElementId, @Param("elementId") Long elementId);

}
