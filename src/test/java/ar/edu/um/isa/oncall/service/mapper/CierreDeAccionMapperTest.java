package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.CierreDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.domain.CierreDeAccionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CierreDeAccionMapperTest {

    private CierreDeAccionMapper cierreDeAccionMapper;

    @BeforeEach
    void setUp() {
        cierreDeAccionMapper = new CierreDeAccionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCierreDeAccionSample1();
        var actual = cierreDeAccionMapper.toEntity(cierreDeAccionMapper.toDto(expected));
        assertCierreDeAccionAllPropertiesEquals(expected, actual);
    }
}
