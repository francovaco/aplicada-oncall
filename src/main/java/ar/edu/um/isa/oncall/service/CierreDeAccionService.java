package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.CierreDeAccion;
import ar.edu.um.isa.oncall.repository.CierreDeAccionRepository;
import ar.edu.um.isa.oncall.service.dto.CierreDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.CierreDeAccionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.CierreDeAccion}.
 */
@Service
@Transactional
public class CierreDeAccionService {

    private static final Logger LOG = LoggerFactory.getLogger(CierreDeAccionService.class);

    private final CierreDeAccionRepository cierreDeAccionRepository;

    private final CierreDeAccionMapper cierreDeAccionMapper;

    public CierreDeAccionService(CierreDeAccionRepository cierreDeAccionRepository, CierreDeAccionMapper cierreDeAccionMapper) {
        this.cierreDeAccionRepository = cierreDeAccionRepository;
        this.cierreDeAccionMapper = cierreDeAccionMapper;
    }

    /**
     * Save a cierreDeAccion.
     *
     * @param cierreDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public CierreDeAccionDTO save(CierreDeAccionDTO cierreDeAccionDTO) {
        LOG.debug("Request to save CierreDeAccion : {}", cierreDeAccionDTO);
        CierreDeAccion cierreDeAccion = cierreDeAccionMapper.toEntity(cierreDeAccionDTO);
        cierreDeAccion = cierreDeAccionRepository.save(cierreDeAccion);
        return cierreDeAccionMapper.toDto(cierreDeAccion);
    }

    /**
     * Update a cierreDeAccion.
     *
     * @param cierreDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public CierreDeAccionDTO update(CierreDeAccionDTO cierreDeAccionDTO) {
        LOG.debug("Request to update CierreDeAccion : {}", cierreDeAccionDTO);
        CierreDeAccion cierreDeAccion = cierreDeAccionMapper.toEntity(cierreDeAccionDTO);
        cierreDeAccion = cierreDeAccionRepository.save(cierreDeAccion);
        return cierreDeAccionMapper.toDto(cierreDeAccion);
    }

    /**
     * Partially update a cierreDeAccion.
     *
     * @param cierreDeAccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CierreDeAccionDTO> partialUpdate(CierreDeAccionDTO cierreDeAccionDTO) {
        LOG.debug("Request to partially update CierreDeAccion : {}", cierreDeAccionDTO);

        return cierreDeAccionRepository
            .findById(cierreDeAccionDTO.getId())
            .map(existingCierreDeAccion -> {
                cierreDeAccionMapper.partialUpdate(existingCierreDeAccion, cierreDeAccionDTO);

                return existingCierreDeAccion;
            })
            .map(cierreDeAccionRepository::save)
            .map(cierreDeAccionMapper::toDto);
    }

    /**
     * Get all the cierreDeAccions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CierreDeAccionDTO> findAll() {
        LOG.debug("Request to get all CierreDeAccions");
        return cierreDeAccionRepository
            .findAll()
            .stream()
            .map(cierreDeAccionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the cierreDeAccions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CierreDeAccionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cierreDeAccionRepository.findAllWithEagerRelationships(pageable).map(cierreDeAccionMapper::toDto);
    }

    /**
     * Get one cierreDeAccion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CierreDeAccionDTO> findOne(Long id) {
        LOG.debug("Request to get CierreDeAccion : {}", id);
        return cierreDeAccionRepository.findOneWithEagerRelationships(id).map(cierreDeAccionMapper::toDto);
    }

    /**
     * Delete the cierreDeAccion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CierreDeAccion : {}", id);
        cierreDeAccionRepository.deleteById(id);
    }
}
