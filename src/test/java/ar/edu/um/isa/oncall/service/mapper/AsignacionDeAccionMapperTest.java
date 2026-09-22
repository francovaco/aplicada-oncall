package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsignacionDeAccionMapperTest {

    private AsignacionDeAccionMapper asignacionDeAccionMapper;

    @BeforeEach
    void setUp() {
        asignacionDeAccionMapper = new AsignacionDeAccionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAsignacionDeAccionSample1();
        var actual = asignacionDeAccionMapper.toEntity(asignacionDeAccionMapper.toDto(expected));
        assertAsignacionDeAccionAllPropertiesEquals(expected, actual);
    }
}
