package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.CierreDeAccionRepository;
import ar.edu.um.isa.oncall.service.CierreDeAccionService;
import ar.edu.um.isa.oncall.service.dto.CierreDeAccionDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.CierreDeAccion}.
 */
@RestController
@RequestMapping("/api/cierre-de-accions")
public class CierreDeAccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CierreDeAccionResource.class);

    private static final String ENTITY_NAME = "cierreDeAccion";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final CierreDeAccionService cierreDeAccionService;

    private final CierreDeAccionRepository cierreDeAccionRepository;

    public CierreDeAccionResource(CierreDeAccionService cierreDeAccionService, CierreDeAccionRepository cierreDeAccionRepository) {
        this.cierreDeAccionService = cierreDeAccionService;
        this.cierreDeAccionRepository = cierreDeAccionRepository;
    }

    /**
     * {@code POST  /cierre-de-accions} : Create a new cierreDeAccion.
     *
     * @param cierreDeAccionDTO the cierreDeAccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cierreDeAccionDTO, or with status {@code 400 (Bad Request)} if the cierreDeAccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CierreDeAccionDTO> createCierreDeAccion(@Valid @RequestBody CierreDeAccionDTO cierreDeAccionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CierreDeAccion : {}", cierreDeAccionDTO);
        if (cierreDeAccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new cierreDeAccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cierreDeAccionDTO = cierreDeAccionService.save(cierreDeAccionDTO);
        return ResponseEntity.created(new URI("/api/cierre-de-accions/" + cierreDeAccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cierreDeAccionDTO.getId().toString()))
            .body(cierreDeAccionDTO);
    }

    /**
     * {@code PUT  /cierre-de-accions/:id} : Updates an existing cierreDeAccion.
     *
     * @param id the id of the cierreDeAccionDTO to save.
     * @param cierreDeAccionDTO the cierreDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cierreDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the cierreDeAccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cierreDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CierreDeAccionDTO> updateCierreDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CierreDeAccionDTO cierreDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CierreDeAccion : {}, {}", id, cierreDeAccionDTO);
        if (cierreDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cierreDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cierreDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cierreDeAccionDTO = cierreDeAccionService.update(cierreDeAccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cierreDeAccionDTO.getId().toString()))
            .body(cierreDeAccionDTO);
    }

    /**
     * {@code PATCH  /cierre-de-accions/:id} : Partial updates given fields of an existing cierreDeAccion, field will ignore if it is null
     *
     * @param id the id of the cierreDeAccionDTO to save.
     * @param cierreDeAccionDTO the cierreDeAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cierreDeAccionDTO,
     * or with status {@code 400 (Bad Request)} if the cierreDeAccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cierreDeAccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cierreDeAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CierreDeAccionDTO> partialUpdateCierreDeAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CierreDeAccionDTO cierreDeAccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CierreDeAccion partially : {}, {}", id, cierreDeAccionDTO);
        if (cierreDeAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cierreDeAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cierreDeAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CierreDeAccionDTO> result = cierreDeAccionService.partialUpdate(cierreDeAccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cierreDeAccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cierre-de-accions} : get all the Cierre De Accions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cierre De Accions in body.
     */
    @GetMapping("")
    public List<CierreDeAccionDTO> getAllCierreDeAccions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CierreDeAccions");
        return cierreDeAccionService.findAll();
    }

    /**
     * {@code GET  /cierre-de-accions/:id} : get the "id" cierreDeAccion.
     *
     * @param id the id of the cierreDeAccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cierreDeAccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CierreDeAccionDTO> getCierreDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CierreDeAccion : {}", id);
        Optional<CierreDeAccionDTO> cierreDeAccionDTO = cierreDeAccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cierreDeAccionDTO);
    }

    /**
     * {@code DELETE  /cierre-de-accions/:id} : delete the "id" cierreDeAccion.
     *
     * @param id the id of the cierreDeAccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCierreDeAccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CierreDeAccion : {}", id);
        cierreDeAccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
