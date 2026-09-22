package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.repository.AsignacionDeAccionRepository;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.AsignacionDeAccionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.AsignacionDeAccion}.
 */
@Service
@Transactional
public class AsignacionDeAccionService {

    private static final Logger LOG = LoggerFactory.getLogger(AsignacionDeAccionService.class);

    private final AsignacionDeAccionRepository asignacionDeAccionRepository;

    private final AsignacionDeAccionMapper asignacionDeAccionMapper;

    public AsignacionDeAccionService(
        AsignacionDeAccionRepository asignacionDeAccionRepository,
        AsignacionDeAccionMapper asignacionDeAccionMapper
    ) {
        this.asignacionDeAccionRepository = asignacionDeAccionRepository;
        this.asignacionDeAccionMapper = asignacionDeAccionMapper;
    }

    /**
     * Save a asignacionDeAccion.
     *
     * @param asignacionDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public AsignacionDeAccionDTO save(AsignacionDeAccionDTO asignacionDeAccionDTO) {
        LOG.debug("Request to save AsignacionDeAccion : {}", asignacionDeAccionDTO);
        AsignacionDeAccion asignacionDeAccion = asignacionDeAccionMapper.toEntity(asignacionDeAccionDTO);
        asignacionDeAccion = asignacionDeAccionRepository.save(asignacionDeAccion);
        return asignacionDeAccionMapper.toDto(asignacionDeAccion);
    }

    /**
     * Update a asignacionDeAccion.
     *
     * @param asignacionDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public AsignacionDeAccionDTO update(AsignacionDeAccionDTO asignacionDeAccionDTO) {
        LOG.debug("Request to update AsignacionDeAccion : {}", asignacionDeAccionDTO);
        AsignacionDeAccion asignacionDeAccion = asignacionDeAccionMapper.toEntity(asignacionDeAccionDTO);
        asignacionDeAccion = asignacionDeAccionRepository.save(asignacionDeAccion);
        return asignacionDeAccionMapper.toDto(asignacionDeAccion);
    }

    /**
     * Partially update a asignacionDeAccion.
     *
     * @param asignacionDeAccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AsignacionDeAccionDTO> partialUpdate(AsignacionDeAccionDTO asignacionDeAccionDTO) {
        LOG.debug("Request to partially update AsignacionDeAccion : {}", asignacionDeAccionDTO);

        return asignacionDeAccionRepository
            .findById(asignacionDeAccionDTO.getId())
            .map(existingAsignacionDeAccion -> {
                asignacionDeAccionMapper.partialUpdate(existingAsignacionDeAccion, asignacionDeAccionDTO);

                return existingAsignacionDeAccion;
            })
            .map(asignacionDeAccionRepository::save)
            .map(asignacionDeAccionMapper::toDto);
    }

    /**
     * Get all the asignacionDeAccions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AsignacionDeAccionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return asignacionDeAccionRepository.findAllWithEagerRelationships(pageable).map(asignacionDeAccionMapper::toDto);
    }

    /**
     * Get one asignacionDeAccion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AsignacionDeAccionDTO> findOne(Long id) {
        LOG.debug("Request to get AsignacionDeAccion : {}", id);
        return asignacionDeAccionRepository.findOneWithEagerRelationships(id).map(asignacionDeAccionMapper::toDto);
    }

    /**
     * Delete the asignacionDeAccion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AsignacionDeAccion : {}", id);
        asignacionDeAccionRepository.deleteById(id);
    }
}
