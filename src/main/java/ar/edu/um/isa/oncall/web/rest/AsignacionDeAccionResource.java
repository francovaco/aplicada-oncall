package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.AsignacionDeAccionRepository;
import ar.edu.um.isa.oncall.service.AsignacionDeAccionQueryService;
import ar.edu.um.isa.oncall.service.AsignacionDeAccionService;
import ar.edu.um.isa.oncall.service.criteria.AsignacionDeAccionCriteria;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.AsignacionDeAccion}.
 */
@RestController
@RequestMapping("/api/asignacion-de-accions")
public class AsignacionDeAccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsignacionDeAccionResource.class);

    private static final String ENTITY_NAME = "asignacionDeAccion";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final AsignacionDeAccionService asignacionDeAccionService;

    private final AsignacionDeAccionRepository asignacionDeAccionRepository;

    private final AsignacionDeAccionQueryService asignacionDeAccionQueryService;

    public AsignacionDeAccionResource(
        AsignacionDeAccionService asignacionDeAccionService,
        AsignacionDeAccionRepository asignacionDeAccionRepository,
        AsignacionDeAccionQueryService asignacionDeAccionQueryService
    ) {
        this.asignacionDeAccionService = asignacionDeAccionService;
        this.asignacionDeAccionRepository = asignacionDeAccionRepository;
        this.asignacionDeAccionQueryService = asignacionDeAccionQueryService;
    }

    /**
     * {@code POST  /asignacion-de-accions} : Create a new asignacionDeAccion.
     *
     * @param asignacionDeAccionDTO the asignacionDeAccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asignacionDeAccionDTO, or with status {@code 400 (Bad Request)} if the asignacionDeAccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AsignacionDeAccionDTO> createAsignacionDeAccion(@Valid @RequestBody AsignacionDeAccionDTO asignacionDeAccionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AsignacionDeAccion : {}", asignacionDeAccionDTO);
        if (asignacionDeAccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new asignacionDeAccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        asignacionDeAccionDTO = asignacionDeAccionService.save(asignacionDeAccionDTO);
        return ResponseEntity.created(new URI("/api/asignacion-de-accions/" + asignacionDeAccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, asignacionDeAccionDTO.getId().toString()))
            .body(asignacionDeAccionDTO);
    }

    /**
     * {@code PUT  /asignacion-de-accions/:id} : Updates an existing asignacionDeAccion.
     *
     * @param id the id of the asignacionDeAccionDTO to save.
     * @param asignacionDeAccionDTO the asignacionDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asignacionDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the asignacionDeAccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asignacionDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDeAccionDTO> updateAsignacionDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AsignacionDeAccionDTO asignacionDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AsignacionDeAccion : {}, {}", id, asignacionDeAccionDTO);
        if (asignacionDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asignacionDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asignacionDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        asignacionDeAccionDTO = asignacionDeAccionService.update(asignacionDeAccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asignacionDeAccionDTO.getId().toString()))
            .body(asignacionDeAccionDTO);
    }

    /**
     * {@code PATCH  /asignacion-de-accions/:id} : Partial updates given fields of an existing asignacionDeAccion, field will ignore if it is null
     *
     * @param id the id of the asignacionDeAccionDTO to save.
     * @param asignacionDeAccionDTO the asignacionDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asignacionDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the asignacionDeAccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the asignacionDeAccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the asignacionDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AsignacionDeAccionDTO> partialUpdateAsignacionDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AsignacionDeAccionDTO asignacionDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AsignacionDeAccion partially : {}, {}", id, asignacionDeAccionDTO);
        if (asignacionDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asignacionDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asignacionDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AsignacionDeAccionDTO> result = asignacionDeAccionService.partialUpdate(asignacionDeAccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asignacionDeAccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asignacion-de-accions} : get all the Asignacion De Accions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Asignacion De Accions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AsignacionDeAccionDTO>> getAllAsignacionDeAccions(
        AsignacionDeAccionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AsignacionDeAccions by criteria: {}", criteria);

        Page<AsignacionDeAccionDTO> page = asignacionDeAccionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asignacion-de-accions/count} : count all the asignacionDeAccions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAsignacionDeAccions(AsignacionDeAccionCriteria criteria) {
        LOG.debug("REST request to count AsignacionDeAccions by criteria: {}", criteria);
        return ResponseEntity.ok().body(asignacionDeAccionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asignacion-de-accions/:id} : get the "id" asignacionDeAccion.
     *
     * @param id the id of the asignacionDeAccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asignacionDeAccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDeAccionDTO> getAsignacionDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AsignacionDeAccion : {}", id);
        Optional<AsignacionDeAccionDTO> asignacionDeAccionDTO = asignacionDeAccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asignacionDeAccionDTO);
    }

    /**
     * {@code DELETE  /asignacion-de-accions/:id} : delete the "id" asignacionDeAccion.
     *
     * @param id the id of the asignacionDeAccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignacionDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AsignacionDeAccion : {}", id);
        asignacionDeAccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
