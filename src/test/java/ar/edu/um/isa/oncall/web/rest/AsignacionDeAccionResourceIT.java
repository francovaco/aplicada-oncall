package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.AccionCorrectiva;
import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoAccion;
import ar.edu.um.isa.oncall.repository.AsignacionDeAccionRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.AsignacionDeAccionService;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.mapper.AsignacionDeAccionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link AsignacionDeAccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AsignacionDeAccionResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.parse("2023-12-04");
    private static final LocalDate SMALLER_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final EstadoAccion DEFAULT_ESTADO = EstadoAccion.PENDIENTE;
    private static final EstadoAccion UPDATED_ESTADO = EstadoAccion.EN_CURSO;

    private static final Instant DEFAULT_CREADA_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADA_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/asignacion-de-accions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AsignacionDeAccionRepository asignacionDeAccionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AsignacionDeAccionRepository asignacionDeAccionRepositoryMock;

    @Autowired
    private AsignacionDeAccionMapper asignacionDeAccionMapper;

    @Mock
    private AsignacionDeAccionService asignacionDeAccionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAsignacionDeAccionMockMvc;

    private AsignacionDeAccion asignacionDeAccion;

    private AsignacionDeAccion insertedAsignacionDeAccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsignacionDeAccion createEntity(EntityManager em) {
        AsignacionDeAccion asignacionDeAccion = new AsignacionDeAccion()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .estado(DEFAULT_ESTADO)
            .creadaEn(DEFAULT_CREADA_EN);
        // Add required entity
        AccionCorrectiva accionCorrectiva;
        if (TestUtil.findAll(em, AccionCorrectiva.class).isEmpty()) {
            accionCorrectiva = AccionCorrectivaResourceIT.createEntity(em);
            em.persist(accionCorrectiva);
            em.flush();
        } else {
            accionCorrectiva = TestUtil.findAll(em, AccionCorrectiva.class).get(0);
        }
        asignacionDeAccion.setAccionCorrectiva(accionCorrectiva);
        return asignacionDeAccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsignacionDeAccion createUpdatedEntity(EntityManager em) {
        AsignacionDeAccion updatedAsignacionDeAccion = new AsignacionDeAccion()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .estado(UPDATED_ESTADO)
            .creadaEn(UPDATED_CREADA_EN);
        // Add required entity
        AccionCorrectiva accionCorrectiva;
        if (TestUtil.findAll(em, AccionCorrectiva.class).isEmpty()) {
            accionCorrectiva = AccionCorrectivaResourceIT.createUpdatedEntity(em);
            em.persist(accionCorrectiva);
            em.flush();
        } else {
            accionCorrectiva = TestUtil.findAll(em, AccionCorrectiva.class).get(0);
        }
        updatedAsignacionDeAccion.setAccionCorrectiva(accionCorrectiva);
        return updatedAsignacionDeAccion;
    }

    @BeforeEach
    void initTest() {
        asignacionDeAccion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAsignacionDeAccion != null) {
            asignacionDeAccionRepository.delete(insertedAsignacionDeAccion);
            insertedAsignacionDeAccion = null;
        }
    }

    @Test
    @Transactional
    void createAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);
        var returnedAsignacionDeAccionDTO = om.readValue(
            restAsignacionDeAccionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AsignacionDeAccionDTO.class
        );

        // Validate the AsignacionDeAccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAsignacionDeAccion = asignacionDeAccionMapper.toEntity(returnedAsignacionDeAccionDTO);
        assertAsignacionDeAccionUpdatableFieldsEquals(
            returnedAsignacionDeAccion,
            getPersistedAsignacionDeAccion(returnedAsignacionDeAccion)
        );

        insertedAsignacionDeAccion = returnedAsignacionDeAccion;
    }

    @Test
    @Transactional
    void createAsignacionDeAccionWithExistingId() throws Exception {
        // Create the AsignacionDeAccion with an existing ID
        asignacionDeAccion.setId(1L);
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAsignacionDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        asignacionDeAccion.setTitulo(null);

        // Create the AsignacionDeAccion, which fails.
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        restAsignacionDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaVencimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        asignacionDeAccion.setFechaVencimiento(null);

        // Create the AsignacionDeAccion, which fails.
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        restAsignacionDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        asignacionDeAccion.setEstado(null);

        // Create the AsignacionDeAccion, which fails.
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        restAsignacionDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreadaEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        asignacionDeAccion.setCreadaEn(null);

        // Create the AsignacionDeAccion, which fails.
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        restAsignacionDeAccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccions() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asignacionDeAccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].creadaEn").value(hasItem(DEFAULT_CREADA_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAsignacionDeAccionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(asignacionDeAccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAsignacionDeAccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(asignacionDeAccionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAsignacionDeAccionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(asignacionDeAccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAsignacionDeAccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(asignacionDeAccionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAsignacionDeAccion() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get the asignacionDeAccion
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL_ID, asignacionDeAccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asignacionDeAccion.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.creadaEn").value(DEFAULT_CREADA_EN.toString()));
    }

    @Test
    @Transactional
    void getAsignacionDeAccionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        Long id = asignacionDeAccion.getId();

        defaultAsignacionDeAccionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAsignacionDeAccionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAsignacionDeAccionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where titulo equals to
        defaultAsignacionDeAccionFiltering("titulo.equals=" + DEFAULT_TITULO, "titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where titulo in
        defaultAsignacionDeAccionFiltering("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO, "titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where titulo is not null
        defaultAsignacionDeAccionFiltering("titulo.specified=true", "titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByTituloContainsSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where titulo contains
        defaultAsignacionDeAccionFiltering("titulo.contains=" + DEFAULT_TITULO, "titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where titulo does not contain
        defaultAsignacionDeAccionFiltering("titulo.doesNotContain=" + UPDATED_TITULO, "titulo.doesNotContain=" + DEFAULT_TITULO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where descripcion equals to
        defaultAsignacionDeAccionFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where descripcion in
        defaultAsignacionDeAccionFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where descripcion is not null
        defaultAsignacionDeAccionFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where descripcion contains
        defaultAsignacionDeAccionFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where descripcion does not contain
        defaultAsignacionDeAccionFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento equals to
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.equals=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.equals=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento in
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.in=" + DEFAULT_FECHA_VENCIMIENTO + "," + UPDATED_FECHA_VENCIMIENTO,
            "fechaVencimiento.in=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento is not null
        defaultAsignacionDeAccionFiltering("fechaVencimiento.specified=true", "fechaVencimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento is greater than or equal to
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.greaterThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.greaterThanOrEqual=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento is less than or equal to
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.lessThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.lessThanOrEqual=" + SMALLER_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento is less than
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.lessThan=" + UPDATED_FECHA_VENCIMIENTO,
            "fechaVencimiento.lessThan=" + DEFAULT_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByFechaVencimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where fechaVencimiento is greater than
        defaultAsignacionDeAccionFiltering(
            "fechaVencimiento.greaterThan=" + SMALLER_FECHA_VENCIMIENTO,
            "fechaVencimiento.greaterThan=" + DEFAULT_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where estado equals to
        defaultAsignacionDeAccionFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where estado in
        defaultAsignacionDeAccionFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where estado is not null
        defaultAsignacionDeAccionFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByCreadaEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where creadaEn equals to
        defaultAsignacionDeAccionFiltering("creadaEn.equals=" + DEFAULT_CREADA_EN, "creadaEn.equals=" + UPDATED_CREADA_EN);
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByCreadaEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where creadaEn in
        defaultAsignacionDeAccionFiltering(
            "creadaEn.in=" + DEFAULT_CREADA_EN + "," + UPDATED_CREADA_EN,
            "creadaEn.in=" + UPDATED_CREADA_EN
        );
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByCreadaEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        // Get all the asignacionDeAccionList where creadaEn is not null
        defaultAsignacionDeAccionFiltering("creadaEn.specified=true", "creadaEn.specified=false");
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByAccionCorrectivaIsEqualToSomething() throws Exception {
        AccionCorrectiva accionCorrectiva;
        if (TestUtil.findAll(em, AccionCorrectiva.class).isEmpty()) {
            asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);
            accionCorrectiva = AccionCorrectivaResourceIT.createEntity(em);
        } else {
            accionCorrectiva = TestUtil.findAll(em, AccionCorrectiva.class).get(0);
        }
        em.persist(accionCorrectiva);
        em.flush();
        asignacionDeAccion.setAccionCorrectiva(accionCorrectiva);
        asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);
        Long accionCorrectivaId = accionCorrectiva.getId();
        // Get all the asignacionDeAccionList where accionCorrectiva equals to accionCorrectivaId
        defaultAsignacionDeAccionShouldBeFound("accionCorrectivaId.equals=" + accionCorrectivaId);

        // Get all the asignacionDeAccionList where accionCorrectiva equals to (accionCorrectivaId + 1)
        defaultAsignacionDeAccionShouldNotBeFound("accionCorrectivaId.equals=" + (accionCorrectivaId + 1));
    }

    @Test
    @Transactional
    void getAllAsignacionDeAccionsByResponsableIsEqualToSomething() throws Exception {
        User responsable;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);
            responsable = UserResourceIT.createEntity();
        } else {
            responsable = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(responsable);
        em.flush();
        asignacionDeAccion.setResponsable(responsable);
        asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);
        Long responsableId = responsable.getId();
        // Get all the asignacionDeAccionList where responsable equals to responsableId
        defaultAsignacionDeAccionShouldBeFound("responsableId.equals=" + responsableId);

        // Get all the asignacionDeAccionList where responsable equals to (responsableId + 1)
        defaultAsignacionDeAccionShouldNotBeFound("responsableId.equals=" + (responsableId + 1));
    }

    private void defaultAsignacionDeAccionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAsignacionDeAccionShouldBeFound(shouldBeFound);
        defaultAsignacionDeAccionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAsignacionDeAccionShouldBeFound(String filter) throws Exception {
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asignacionDeAccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].creadaEn").value(hasItem(DEFAULT_CREADA_EN.toString())));

        // Check, that the count call also returns 1
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAsignacionDeAccionShouldNotBeFound(String filter) throws Exception {
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAsignacionDeAccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAsignacionDeAccion() throws Exception {
        // Get the asignacionDeAccion
        restAsignacionDeAccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAsignacionDeAccion() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asignacionDeAccion
        AsignacionDeAccion updatedAsignacionDeAccion = asignacionDeAccionRepository.findById(asignacionDeAccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAsignacionDeAccion are not directly saved in db
        em.detach(updatedAsignacionDeAccion);
        updatedAsignacionDeAccion
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .estado(UPDATED_ESTADO)
            .creadaEn(UPDATED_CREADA_EN);
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(updatedAsignacionDeAccion);

        restAsignacionDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, asignacionDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(asignacionDeAccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAsignacionDeAccionToMatchAllProperties(updatedAsignacionDeAccion);
    }

    @Test
    @Transactional
    void putNonExistingAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, asignacionDeAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(asignacionDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(asignacionDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAsignacionDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asignacionDeAccion using partial update
        AsignacionDeAccion partialUpdatedAsignacionDeAccion = new AsignacionDeAccion();
        partialUpdatedAsignacionDeAccion.setId(asignacionDeAccion.getId());

        restAsignacionDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsignacionDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAsignacionDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the AsignacionDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAsignacionDeAccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAsignacionDeAccion, asignacionDeAccion),
            getPersistedAsignacionDeAccion(asignacionDeAccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateAsignacionDeAccionWithPatch() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asignacionDeAccion using partial update
        AsignacionDeAccion partialUpdatedAsignacionDeAccion = new AsignacionDeAccion();
        partialUpdatedAsignacionDeAccion.setId(asignacionDeAccion.getId());

        partialUpdatedAsignacionDeAccion
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .estado(UPDATED_ESTADO)
            .creadaEn(UPDATED_CREADA_EN);

        restAsignacionDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsignacionDeAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAsignacionDeAccion))
            )
            .andExpect(status().isOk());

        // Validate the AsignacionDeAccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAsignacionDeAccionUpdatableFieldsEquals(
            partialUpdatedAsignacionDeAccion,
            getPersistedAsignacionDeAccion(partialUpdatedAsignacionDeAccion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, asignacionDeAccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(asignacionDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(asignacionDeAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsignacionDeAccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asignacionDeAccion.setId(longCount.incrementAndGet());

        // Create the AsignacionDeAccion
        AsignacionDeAccionDTO asignacionDeAccionDTO = asignacionDeAccionMapper.toDto(asignacionDeAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsignacionDeAccionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(asignacionDeAccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AsignacionDeAccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsignacionDeAccion() throws Exception {
        // Initialize the database
        insertedAsignacionDeAccion = asignacionDeAccionRepository.saveAndFlush(asignacionDeAccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the asignacionDeAccion
        restAsignacionDeAccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, asignacionDeAccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return asignacionDeAccionRepository.count();
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

    protected AsignacionDeAccion getPersistedAsignacionDeAccion(AsignacionDeAccion asignacionDeAccion) {
        return asignacionDeAccionRepository.findById(asignacionDeAccion.getId()).orElseThrow();
    }

    protected void assertPersistedAsignacionDeAccionToMatchAllProperties(AsignacionDeAccion expectedAsignacionDeAccion) {
        assertAsignacionDeAccionAllPropertiesEquals(expectedAsignacionDeAccion, getPersistedAsignacionDeAccion(expectedAsignacionDeAccion));
    }

    protected void assertPersistedAsignacionDeAccionToMatchUpdatableProperties(AsignacionDeAccion expectedAsignacionDeAccion) {
        assertAsignacionDeAccionAllUpdatablePropertiesEquals(
            expectedAsignacionDeAccion,
            getPersistedAsignacionDeAccion(expectedAsignacionDeAccion)
        );
    }
}
