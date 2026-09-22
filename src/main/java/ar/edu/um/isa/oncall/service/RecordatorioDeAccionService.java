package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.RecordatorioDeAccion;
import ar.edu.um.isa.oncall.repository.RecordatorioDeAccionRepository;
import ar.edu.um.isa.oncall.service.dto.RecordatorioDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.RecordatorioDeAccionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.RecordatorioDeAccion}.
 */
@Service
@Transactional
public class RecordatorioDeAccionService {

    private static final Logger LOG = LoggerFactory.getLogger(RecordatorioDeAccionService.class);

    private final RecordatorioDeAccionRepository recordatorioDeAccionRepository;

    private final RecordatorioDeAccionMapper recordatorioDeAccionMapper;

    public RecordatorioDeAccionService(
        RecordatorioDeAccionRepository recordatorioDeAccionRepository,
        RecordatorioDeAccionMapper recordatorioDeAccionMapper
    ) {
        this.recordatorioDeAccionRepository = recordatorioDeAccionRepository;
        this.recordatorioDeAccionMapper = recordatorioDeAccionMapper;
    }

    /**
     * Save a recordatorioDeAccion.
     *
     * @param recordatorioDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecordatorioDeAccionDTO save(RecordatorioDeAccionDTO recordatorioDeAccionDTO) {
        LOG.debug("Request to save RecordatorioDeAccion : {}", recordatorioDeAccionDTO);
        RecordatorioDeAccion recordatorioDeAccion = recordatorioDeAccionMapper.toEntity(recordatorioDeAccionDTO);
        recordatorioDeAccion = recordatorioDeAccionRepository.save(recordatorioDeAccion);
        return recordatorioDeAccionMapper.toDto(recordatorioDeAccion);
    }

    /**
     * Update a recordatorioDeAccion.
     *
     * @param recordatorioDeAccionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecordatorioDeAccionDTO update(RecordatorioDeAccionDTO recordatorioDeAccionDTO) {
        LOG.debug("Request to update RecordatorioDeAccion : {}", recordatorioDeAccionDTO);
        RecordatorioDeAccion recordatorioDeAccion = recordatorioDeAccionMapper.toEntity(recordatorioDeAccionDTO);
        recordatorioDeAccion = recordatorioDeAccionRepository.save(recordatorioDeAccion);
        return recordatorioDeAccionMapper.toDto(recordatorioDeAccion);
    }

    /**
     * Partially update a recordatorioDeAccion.
     *
     * @param recordatorioDeAccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecordatorioDeAccionDTO> partialUpdate(RecordatorioDeAccionDTO recordatorioDeAccionDTO) {
        LOG.debug("Request to partially update RecordatorioDeAccion : {}", recordatorioDeAccionDTO);

        return recordatorioDeAccionRepository
            .findById(recordatorioDeAccionDTO.getId())
            .map(existingRecordatorioDeAccion -> {
                recordatorioDeAccionMapper.partialUpdate(existingRecordatorioDeAccion, recordatorioDeAccionDTO);

                return existingRecordatorioDeAccion;
            })
            .map(recordatorioDeAccionRepository::save)
            .map(recordatorioDeAccionMapper::toDto);
    }

    /**
     * Get all the recordatorioDeAccions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecordatorioDeAccionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RecordatorioDeAccions");
        return recordatorioDeAccionRepository.findAll(pageable).map(recordatorioDeAccionMapper::toDto);
    }

    /**
     * Get one recordatorioDeAccion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecordatorioDeAccionDTO> findOne(Long id) {
        LOG.debug("Request to get RecordatorioDeAccion : {}", id);
        return recordatorioDeAccionRepository.findById(id).map(recordatorioDeAccionMapper::toDto);
    }

    /**
     * Delete the recordatorioDeAccion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecordatorioDeAccion : {}", id);
        recordatorioDeAccionRepository.deleteById(id);
    }
}
