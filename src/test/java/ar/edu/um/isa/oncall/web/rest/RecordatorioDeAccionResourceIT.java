package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.RecordatorioDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.RecordatorioDeAccion;
import ar.edu.um.isa.oncall.repository.RecordatorioDeAccionRepository;
import ar.edu.um.isa.oncall.service.dto.RecordatorioDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.RecordatorioDeAccionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecordatorioDeAccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordatorioDeAccionResourceIT {

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ENVIADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENVIADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Integer DEFAULT_NIVEL_ESCALAMIENTO = 1;
    private static final Integer UPDATED_NIVEL_ESCALAMIENTO = 2;

    private static final String ENTITY_API_URL = "/api/recordatorio-de-accions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecordatorioDeAccionRepository recordatorioDeAccionRepository;

    @Autowired
    private RecordatorioDeAccionMapper recordatorioDeAccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecordatorioDeAccionMockMvc;

    private RecordatorioDeAccion recordatorioDeAccion;

    private RecordatorioDeAccion insertedRecordatorioDeAccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordatorioDeAccion createEntity(EntityManager em) {
        RecordatorioDeAccion recordatorioDeAccion = new RecordatorioDeAccion()
            .mensaje(DEFAULT_MENSAJE)
            .enviadoEn(DEFAULT_ENVIADO_EN)
            .nivelEscalamiento(DEFAULT_NIVEL_ESCALAMIENTO);
        // Add required entity
        AsignacionDeAccion asignacionDeAccion;
        if (TestUtil.findAll(em, AsignacionDeAccion.class).isEmpty()) {
            asignacionDeAccion = AsignacionDeAccionResourceIT.createEntity(em);
            em.persist(asignacionDeAccion);
            em.flush();
        } else {
            asignacionDeAccion = TestUtil.findAll(em, AsignacionDeAccion.class).get(0);
        }
        recordatorioDeAccion.setAsignacion(asignacionDeAccion);
        return recordatorioDeAccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordatorioDeAccion createUpdatedEntity(EntityManager em) {
        RecordatorioDeAccion updatedRecordatorioDeAccion = new RecordatorioDeAccion()
            .mensaje(UPDATED_MENSAJE)
            .enviadoEn(UPDATED_ENVIADO_EN)
            .nivelEscalamiento(UPDATED_NIVEL_ESCALAMIENTO);
        // Add required entity
        AsignacionDeAccion asignacionDeAccion;
        if (TestUtil.findAll(em, AsignacionDeAccion.class).isEmpty()) {
            asignacionDeAccion = AsignacionDeAccionResourceIT.createUpdatedEntity(em);
            em.persist(asignacionDeAccion);
            em.flush();
        } else {
            asignacionDeAccion = TestUtil.findAll(em, AsignacionDeAccion.class).get(0);
        }
        updatedRecordatorioDeAccion.setAsignacion(asignacionDeAccion);
        return updatedRecordatorioDeAccion;
    }

    @BeforeEach
    void initTest() {
        recordatorioDeAccion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRecordatorioDeAccion != null) {
            recordatorioDeAccionRepository.delete(insertedRecordatorioDeAccion);
            insertedRecordatorioDeAccion = null;
        }
    }

    @Test
    @Transactional
    void createRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);
        var returnedRecordatorioDeAccionDTO = om.readValue(
            restRecordatorioDeAccionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordatorioDeAccionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecordatorioDeAccionDTO.class
        );

        // Validate the RecordatorioDeAccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecordatorioDeAccion = recordatorioDeAccionMapper.toEntity(returnedRecordatorioDeAccionDTO);
        assertRecordatorioDeAccionUpdatableFieldsEquals(
            returnedRecordatorioDeAccion,
            getPersistedRecordatorioDeAccion(returnedRecordatorioDeAccion)
        );

        insertedRecordatorioDeAccion = returnedRecordatorioDeAccion;
    }

    @Test
    @Transactional
    void createRecordatorioDeAccionWithExistingId() throws Exception {
        // Create the RecordatorioDeAccion with an existing ID
        recordatorioDeAccion.setId(1L);
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordatorioDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordatorioDeAccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMensajeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recordatorioDeAccion.setMensaje(null);

        // Create the RecordatorioDeAccion, which fails.
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        restRecordatorioDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordatorioDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelEscalamientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recordatorioDeAccion.setNivelEscalamiento(null);

        // Create the RecordatorioDeAccion, which fails.
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        restRecordatorioDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordatorioDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecordatorioDeAccions() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        // Get all the recordatorioDeAccionList
        restRecordatorioDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recordatorioDeAccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].enviadoEn").value(hasItem(DEFAULT_ENVIADO_EN.toString())))
            .andExpect(jsonPath("$.[*].nivelEscalamiento").value(hasItem(DEFAULT_NIVEL_ESCALAMIENTO)));
    }

    @Test
    @Transactional
    void getRecordatorioDeAccion() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        // Get the recordatorioDeAccion
        restRecordatorioDeAccionMockMvc
            .perform(get(ENTITY_API_URL_ID, recordatorioDeAccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recordatorioDeAccion.getId().intValue()))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE))
            .andExpect(jsonPath("$.enviadoEn").value(DEFAULT_ENVIADO_EN.toString()))
            .andExpect(jsonPath("$.nivelEscalamiento").value(DEFAULT_NIVEL_ESCALAMIENTO));
    }

    @Test
    @Transactional
    void getNonExistingRecordatorioDeAccion() throws Exception {
        // Get the recordatorioDeAccion
        restRecordatorioDeAccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecordatorioDeAccion() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recordatorioDeAccion
        RecordatorioDeAccion updatedRecordatorioDeAccion = recordatorioDeAccionRepository
            .findById(recordatorioDeAccion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecordatorioDeAccion are not directly saved in db
        em.detach(updatedRecordatorioDeAccion);
        updatedRecordatorioDeAccion.mensaje(UPDATED_MENSAJE).enviadoEn(UPDATED_ENVIADO_EN).nivelEscalamiento(UPDATED_NIVEL_ESCALAMIENTO);
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(updatedRecordatorioDeAccion);

        restRecordatorioDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordatorioDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecordatorioDeAccionToMatchAllProperties(updatedRecordatorioDeAccion);
    }

    @Test
    @Transactional
    void putNonExistingRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordatorioDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordatorioDeAccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecordatorioDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recordatorioDeAccion using partial update
        RecordatorioDeAccion partialUpdatedRecordatorioDeAccion = new RecordatorioDeAccion();
        partialUpdatedRecordatorioDeAccion.setId(recordatorioDeAccion.getId());

        restRecordatorioDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordatorioDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecordatorioDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the RecordatorioDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecordatorioDeAccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecordatorioDeAccion, recordatorioDeAccion),
            getPersistedRecordatorioDeAccion(recordatorioDeAccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecordatorioDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recordatorioDeAccion using partial update
        RecordatorioDeAccion partialUpdatedRecordatorioDeAccion = new RecordatorioDeAccion();
        partialUpdatedRecordatorioDeAccion.setId(recordatorioDeAccion.getId());

        partialUpdatedRecordatorioDeAccion
            .mensaje(UPDATED_MENSAJE)
            .enviadoEn(UPDATED_ENVIADO_EN)
            .nivelEscalamiento(UPDATED_NIVEL_ESCALAMIENTO);

        restRecordatorioDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordatorioDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecordatorioDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the RecordatorioDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecordatorioDeAccionUpdatableFieldsEquals(
            partialUpdatedRecordatorioDeAccion,
            getPersistedRecordatorioDeAccion(partialUpdatedRecordatorioDeAccion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recordatorioDeAccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecordatorioDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordatorioDeAccion.setId(longCount.incrementAndGet());

        // Create the RecordatorioDeAccion
        RecordatorioDeAccionDTO recordatorioDeAccionDTO = recordatorioDeAccionMapper.toDto(recordatorioDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordatorioDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recordatorioDeAccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordatorioDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecordatorioDeAccion() throws Exception {
        // Initialize the database
        insertedRecordatorioDeAccion = recordatorioDeAccionRepository.saveAndFlush(recordatorioDeAccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recordatorioDeAccion
        restRecordatorioDeAccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, recordatorioDeAccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recordatorioDeAccionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected RecordatorioDeAccion getPersistedRecordatorioDeAccion(RecordatorioDeAccion recordatorioDeAccion) {
        return recordatorioDeAccionRepository.findById(recordatorioDeAccion.getId()).orElseThrow();
    }

    protected void assertPersistedRecordatorioDeAccionToMatchAllProperties(RecordatorioDeAccion expectedRecordatorioDeAccion) {
        assertRecordatorioDeAccionAllPropertiesEquals(
            expectedRecordatorioDeAccion,
            getPersistedRecordatorioDeAccion(expectedRecordatorioDeAccion)
        );
    }

    protected void assertPersistedRecordatorioDeAccionToMatchUpdatableProperties(RecordatorioDeAccion expectedRecordatorioDeAccion) {
        assertRecordatorioDeAccionAllUpdatablePropertiesEquals(
            expectedRecordatorioDeAccion,
            getPersistedRecordatorioDeAccion(expectedRecordatorioDeAccion)
        );
    }
}
