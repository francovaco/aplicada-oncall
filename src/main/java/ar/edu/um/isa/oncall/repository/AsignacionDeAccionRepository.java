package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AsignacionDeAccion entity.
 */
@Repository
public interface AsignacionDeAccionRepository
    extends JpaRepository<AsignacionDeAccion, Long>, JpaSpecificationExecutor<AsignacionDeAccion>
{
    @Query(
        "select asignacionDeAccion from AsignacionDeAccion asignacionDeAccion where asignacionDeAccion.responsable.login = ?#{authentication.name}"
    )
    List<AsignacionDeAccion> findByResponsableIsCurrentUser();

    default Optional<AsignacionDeAccion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AsignacionDeAccion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AsignacionDeAccion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select asignacionDeAccion from AsignacionDeAccion asignacionDeAccion left join fetch asignacionDeAccion.responsable",
        countQuery = "select count(asignacionDeAccion) from AsignacionDeAccion asignacionDeAccion"
    )
    Page<AsignacionDeAccion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select asignacionDeAccion from AsignacionDeAccion asignacionDeAccion left join fetch asignacionDeAccion.responsable")
    List<AsignacionDeAccion> findAllWithToOneRelationships();

    @Query(
        "select asignacionDeAccion from AsignacionDeAccion asignacionDeAccion left join fetch asignacionDeAccion.responsable where asignacionDeAccion.id =:id"
    )
    Optional<AsignacionDeAccion> findOneWithToOneRelationships(@Param("id") Long id);
}
