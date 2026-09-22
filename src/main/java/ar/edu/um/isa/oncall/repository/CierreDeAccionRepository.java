package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.CierreDeAccion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CierreDeAccion entity.
 */
@Repository
public interface CierreDeAccionRepository extends JpaRepository<CierreDeAccion, Long> {
    @Query("select cierreDeAccion from CierreDeAccion cierreDeAccion where cierreDeAccion.cerradoPor.login = ?#{authentication.name}")
    List<CierreDeAccion> findByCerradoPorIsCurrentUser();

    default Optional<CierreDeAccion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CierreDeAccion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CierreDeAccion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cierreDeAccion from CierreDeAccion cierreDeAccion left join fetch cierreDeAccion.cerradoPor",
        countQuery = "select count(cierreDeAccion) from CierreDeAccion cierreDeAccion"
    )
    Page<CierreDeAccion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cierreDeAccion from CierreDeAccion cierreDeAccion left join fetch cierreDeAccion.cerradoPor")
    List<CierreDeAccion> findAllWithToOneRelationships();

    @Query(
        "select cierreDeAccion from CierreDeAccion cierreDeAccion left join fetch cierreDeAccion.cerradoPor where cierreDeAccion.id =:id"
    )
    Optional<CierreDeAccion> findOneWithToOneRelationships(@Param("id") Long id);
}
