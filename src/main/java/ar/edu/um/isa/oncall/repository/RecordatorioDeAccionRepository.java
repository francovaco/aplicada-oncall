package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.RecordatorioDeAccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecordatorioDeAccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordatorioDeAccionRepository extends JpaRepository<RecordatorioDeAccion, Long> {}
