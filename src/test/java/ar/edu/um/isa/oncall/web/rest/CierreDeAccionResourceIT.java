package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.CierreDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.CierreDeAccion;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.repository.CierreDeAccionRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.CierreDeAccionService;
import ar.edu.um.isa.oncall.service.dto.CierreDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.CierreDeAccionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CierreDeAccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CierreDeAccionResourceIT {

    private static final String DEFAULT_COMENTARIO_CIERRE = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO_CIERRE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CERRADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CERRADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/cierre-de-accions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CierreDeAccionRepository cierreDeAccionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CierreDeAccionRepository cierreDeAccionRepositoryMock;

    @Autowired
    private CierreDeAccionMapper cierreDeAccionMapper;

    @Mock
    private CierreDeAccionService cierreDeAccionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCierreDeAccionMockMvc;

    private CierreDeAccion cierreDeAccion;

    private CierreDeAccion insertedCierreDeAccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CierreDeAccion createEntity(EntityManager em) {
        CierreDeAccion cierreDeAccion = new CierreDeAccion().comentarioCierre(DEFAULT_COMENTARIO_CIERRE).cerradoEn(DEFAULT_CERRADO_EN);
        // Add required entity
        AsignacionDeAccion asignacionDeAccion;
        if (TestUtil.findAll(em, AsignacionDeAccion.class).isEmpty()) {
            asignacionDeAccion = AsignacionDeAccionResourceIT.createEntity(em);
            em.persist(asignacionDeAccion);
            em.flush();
        } else {
            asignacionDeAccion = TestUtil.findAll(em, AsignacionDeAccion.class).get(0);
        }
        cierreDeAccion.setAsignacion(asignacionDeAccion);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        cierreDeAccion.setCerradoPor(user);
        return cierreDeAccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CierreDeAccion createUpdatedEntity(EntityManager em) {
        CierreDeAccion updatedCierreDeAccion = new CierreDeAccion()
            .comentarioCierre(UPDATED_COMENTARIO_CIERRE)
            .cerradoEn(UPDATED_CERRADO_EN);
        // Add required entity
        AsignacionDeAccion asignacionDeAccion;
        if (TestUtil.findAll(em, AsignacionDeAccion.class).isEmpty()) {
            asignacionDeAccion = AsignacionDeAccionResourceIT.createUpdatedEntity(em);
            em.persist(asignacionDeAccion);
            em.flush();
        } else {
            asignacionDeAccion = TestUtil.findAll(em, AsignacionDeAccion.class).get(0);
        }
        updatedCierreDeAccion.setAsignacion(asignacionDeAccion);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCierreDeAccion.setCerradoPor(user);
        return updatedCierreDeAccion;
    }

    @BeforeEach
    void initTest() {
        cierreDeAccion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCierreDeAccion != null) {
            cierreDeAccionRepository.delete(insertedCierreDeAccion);
            insertedCierreDeAccion = null;
        }
    }

    @Test
    @Transactional
    void createCierreDeAccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);
        var returnedCierreDeAccionDTO = om.readValue(
            restCierreDeAccionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cierreDeAccionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CierreDeAccionDTO.class
        );

        // Validate the CierreDeAccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCierreDeAccion = cierreDeAccionMapper.toEntity(returnedCierreDeAccionDTO);
        assertCierreDeAccionUpdatableFieldsEquals(returnedCierreDeAccion, getPersistedCierreDeAccion(returnedCierreDeAccion));

        insertedCierreDeAccion = returnedCierreDeAccion;
    }

    @Test
    @Transactional
    void createCierreDeAccionWithExistingId() throws Exception {
        // Create the CierreDeAccion with an existing ID
        cierreDeAccion.setId(1L);
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCierreDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cierreDeAccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCerradoEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cierreDeAccion.setCerradoEn(null);

        // Create the CierreDeAccion, which fails.
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        restCierreDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cierreDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCierreDeAccions() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        // Get all the cierreDeAccionList
        restCierreDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cierreDeAccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarioCierre").value(hasItem(DEFAULT_COMENTARIO_CIERRE)))
            .andExpect(jsonPath("$.[*].cerradoEn").value(hasItem(DEFAULT_CERRADO_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCierreDeAccionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cierreDeAccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCierreDeAccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cierreDeAccionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCierreDeAccionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cierreDeAccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCierreDeAccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cierreDeAccionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCierreDeAccion() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        // Get the cierreDeAccion
        restCierreDeAccionMockMvc
            .perform(get(ENTITY_API_URL_ID, cierreDeAccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cierreDeAccion.getId().intValue()))
            .andExpect(jsonPath("$.comentarioCierre").value(DEFAULT_COMENTARIO_CIERRE))
            .andExpect(jsonPath("$.cerradoEn").value(DEFAULT_CERRADO_EN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCierreDeAccion() throws Exception {
        // Get the cierreDeAccion
        restCierreDeAccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCierreDeAccion() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cierreDeAccion
        CierreDeAccion updatedCierreDeAccion = cierreDeAccionRepository.findById(cierreDeAccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCierreDeAccion are not directly saved in db
        em.detach(updatedCierreDeAccion);
        updatedCierreDeAccion.comentarioCierre(UPDATED_COMENTARIO_CIERRE).cerradoEn(UPDATED_CERRADO_EN);
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(updatedCierreDeAccion);

        restCierreDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cierreDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cierreDeAccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCierreDeAccionToMatchAllProperties(updatedCierreDeAccion);
    }

    @Test
    @Transactional
    void putNonExistingCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cierreDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cierreDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cierreDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cierreDeAccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCierreDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cierreDeAccion using partial update
        CierreDeAccion partialUpdatedCierreDeAccion = new CierreDeAccion();
        partialUpdatedCierreDeAccion.setId(cierreDeAccion.getId());

        partialUpdatedCierreDeAccion.comentarioCierre(UPDATED_COMENTARIO_CIERRE);

        restCierreDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCierreDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCierreDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the CierreDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCierreDeAccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCierreDeAccion, cierreDeAccion),
            getPersistedCierreDeAccion(cierreDeAccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateCierreDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cierreDeAccion using partial update
        CierreDeAccion partialUpdatedCierreDeAccion = new CierreDeAccion();
        partialUpdatedCierreDeAccion.setId(cierreDeAccion.getId());

        partialUpdatedCierreDeAccion.comentarioCierre(UPDATED_COMENTARIO_CIERRE).cerradoEn(UPDATED_CERRADO_EN);

        restCierreDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCierreDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCierreDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the CierreDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCierreDeAccionUpdatableFieldsEquals(partialUpdatedCierreDeAccion, getPersistedCierreDeAccion(partialUpdatedCierreDeAccion));
    }

    @Test
    @Transactional
    void patchNonExistingCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cierreDeAccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cierreDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cierreDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCierreDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cierreDeAccion.setId(longCount.incrementAndGet());

        // Create the CierreDeAccion
        CierreDeAccionDTO cierreDeAccionDTO = cierreDeAccionMapper.toDto(cierreDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCierreDeAccionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cierreDeAccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CierreDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCierreDeAccion() throws Exception {
        // Initialize the database
        insertedCierreDeAccion = cierreDeAccionRepository.saveAndFlush(cierreDeAccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cierreDeAccion
        restCierreDeAccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, cierreDeAccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cierreDeAccionRepository.count();
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

    protected CierreDeAccion getPersistedCierreDeAccion(CierreDeAccion cierreDeAccion) {
        return cierreDeAccionRepository.findById(cierreDeAccion.getId()).orElseThrow();
    }

    protected void assertPersistedCierreDeAccionToMatchAllProperties(CierreDeAccion expectedCierreDeAccion) {
        assertCierreDeAccionAllPropertiesEquals(expectedCierreDeAccion, getPersistedCierreDeAccion(expectedCierreDeAccion));
    }

    protected void assertPersistedCierreDeAccionToMatchUpdatableProperties(CierreDeAccion expectedCierreDeAccion) {
        assertCierreDeAccionAllUpdatablePropertiesEquals(expectedCierreDeAccion, getPersistedCierreDeAccion(expectedCierreDeAccion));
    }
}
