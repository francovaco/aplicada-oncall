package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.RecordatorioDeAccionAsserts.*;
import static ar.edu.um.isa.oncall.domain.RecordatorioDeAccionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecordatorioDeAccionMapperTest {

    private RecordatorioDeAccionMapper recordatorioDeAccionMapper;

    @BeforeEach
    void setUp() {
        recordatorioDeAccionMapper = new RecordatorioDeAccionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecordatorioDeAccionSample1();
        var actual = recordatorioDeAccionMapper.toEntity(recordatorioDeAccionMapper.toDto(expected));
        assertRecordatorioDeAccionAllPropertiesEquals(expected, actual);
    }
}
