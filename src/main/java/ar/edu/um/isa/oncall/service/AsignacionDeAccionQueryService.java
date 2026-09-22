package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.repository.AsignacionDeAccionRepository;
import ar.edu.um.isa.oncall.service.criteria.AsignacionDeAccionCriteria;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.AsignacionDeAccionMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AsignacionDeAccion} entities in the database.
 * The main input is a {@link AsignacionDeAccionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AsignacionDeAccionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AsignacionDeAccionQueryService extends QueryService<AsignacionDeAccion> {

    private static final Logger LOG = LoggerFactory.getLogger(AsignacionDeAccionQueryService.class);

    private final AsignacionDeAccionRepository asignacionDeAccionRepository;

    private final AsignacionDeAccionMapper asignacionDeAccionMapper;

    public AsignacionDeAccionQueryService(
        AsignacionDeAccionRepository asignacionDeAccionRepository,
        AsignacionDeAccionMapper asignacionDeAccionMapper
    ) {
        this.asignacionDeAccionRepository = asignacionDeAccionRepository;
        this.asignacionDeAccionMapper = asignacionDeAccionMapper;
    }

    /**
     * Return a {@link Page} of {@link AsignacionDeAccionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AsignacionDeAccionDTO> findByCriteria(AsignacionDeAccionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AsignacionDeAccion> specification = createSpecification(criteria);
        return asignacionDeAccionRepository.findAll(specification, page).map(asignacionDeAccionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AsignacionDeAccionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AsignacionDeAccion> specification = createSpecification(criteria);
        return asignacionDeAccionRepository.count(specification);
    }

    /**
     * Function to convert {@link AsignacionDeAccionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AsignacionDeAccion> createSpecification(AsignacionDeAccionCriteria criteria) {
        Specification<AsignacionDeAccion> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AsignacionDeAccion_.accionCorrectiva, JoinType.LEFT);
                root.fetch(AsignacionDeAccion_.responsable, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AsignacionDeAccion_.id),
                    buildStringSpecification(criteria.getTitulo(), AsignacionDeAccion_.titulo),
                    buildStringSpecification(criteria.getDescripcion(), AsignacionDeAccion_.descripcion),
                    buildRangeSpecification(criteria.getFechaVencimiento(), AsignacionDeAccion_.fechaVencimiento),
                    buildSpecification(criteria.getEstado(), AsignacionDeAccion_.estado),
                    buildRangeSpecification(criteria.getCreadaEn(), AsignacionDeAccion_.creadaEn),
                    buildSpecification(criteria.getAccionCorrectivaId(), root ->
                        root.join(AsignacionDeAccion_.accionCorrectiva, JoinType.LEFT).get(AccionCorrectiva_.id)
                    ),
                    buildSpecification(criteria.getResponsableId(), root ->
                        root.join(AsignacionDeAccion_.responsable, JoinType.LEFT).get(User_.id)
                    )
                )
            );
        }
        return specification;
    }
}
