package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.RecordatorioDeAccionRepository;
import ar.edu.um.isa.oncall.service.RecordatorioDeAccionService;
import ar.edu.um.isa.oncall.service.dto.RecordatorioDeAccionDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.RecordatorioDeAccion}.
 */
@RestController
@RequestMapping("/api/recordatorio-de-accions")
public class RecordatorioDeAccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecordatorioDeAccionResource.class);

    private static final String ENTITY_NAME = "recordatorioDeAccion";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final RecordatorioDeAccionService recordatorioDeAccionService;

    private final RecordatorioDeAccionRepository recordatorioDeAccionRepository;

    public RecordatorioDeAccionResource(
        RecordatorioDeAccionService recordatorioDeAccionService,
        RecordatorioDeAccionRepository recordatorioDeAccionRepository
    ) {
        this.recordatorioDeAccionService = recordatorioDeAccionService;
        this.recordatorioDeAccionRepository = recordatorioDeAccionRepository;
    }

    /**
     * {@code POST  /recordatorio-de-accions} : Create a new recordatorioDeAccion.
     *
     * @param recordatorioDeAccionDTO the recordatorioDeAccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recordatorioDeAccionDTO, or with status {@code 400 (Bad Request)} if the recordatorioDeAccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecordatorioDeAccionDTO> createRecordatorioDeAccion(
        @Valid @RequestBody RecordatorioDeAccionDTO recordatorioDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecordatorioDeAccion : {}", recordatorioDeAccionDTO);
        if (recordatorioDeAccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new recordatorioDeAccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recordatorioDeAccionDTO = recordatorioDeAccionService.save(recordatorioDeAccionDTO);
        return ResponseEntity.created(new URI("/api/recordatorio-de-accions/" + recordatorioDeAccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recordatorioDeAccionDTO.getId().toString()))
            .body(recordatorioDeAccionDTO);
    }

    /**
     * {@code PUT  /recordatorio-de-accions/:id} : Updates an existing recordatorioDeAccion.
     *
     * @param id the id of the recordatorioDeAccionDTO to save.
     * @param recordatorioDeAccionDTO the recordatorioDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordatorioDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the recordatorioDeAccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recordatorioDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecordatorioDeAccionDTO> updateRecordatorioDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecordatorioDeAccionDTO recordatorioDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecordatorioDeAccion : {}, {}", id, recordatorioDeAccionDTO);
        if (recordatorioDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordatorioDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordatorioDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recordatorioDeAccionDTO = recordatorioDeAccionService.update(recordatorioDeAccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordatorioDeAccionDTO.getId().toString()))
            .body(recordatorioDeAccionDTO);
    }

    /**
     * {@code PATCH  /recordatorio-de-accions/:id} : Partial updates given fields of an existing recordatorioDeAccion, field will ignore if it is null
     *
     * @param id the id of the recordatorioDeAccionDTO to save.
     * @param recordatorioDeAccionDTO the recordatorioDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordatorioDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the recordatorioDeAccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recordatorioDeAccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recordatorioDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecordatorioDeAccionDTO> partialUpdateRecordatorioDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecordatorioDeAccionDTO recordatorioDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecordatorioDeAccion partially : {}, {}", id, recordatorioDeAccionDTO);
        if (recordatorioDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordatorioDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordatorioDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecordatorioDeAccionDTO> result = recordatorioDeAccionService.partialUpdate(recordatorioDeAccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordatorioDeAccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recordatorio-de-accions} : get all the Recordatorio De Accions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recordatorio De Accions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecordatorioDeAccionDTO>> getAllRecordatorioDeAccions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RecordatorioDeAccions");
        Page<RecordatorioDeAccionDTO> page = recordatorioDeAccionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recordatorio-de-accions/:id} : get the "id" recordatorioDeAccion.
     *
     * @param id the id of the recordatorioDeAccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recordatorioDeAccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecordatorioDeAccionDTO> getRecordatorioDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecordatorioDeAccion : {}", id);
        Optional<RecordatorioDeAccionDTO> recordatorioDeAccionDTO = recordatorioDeAccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recordatorioDeAccionDTO);
    }

    /**
     * {@code DELETE  /recordatorio-de-accions/:id} : delete the "id" recordatorioDeAccion.
     *
     * @param id the id of the recordatorioDeAccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecordatorioDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecordatorioDeAccion : {}", id);
        recordatorioDeAccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
