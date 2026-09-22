package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordatorioDeAccionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordatorioDeAccionDTO.class);
        RecordatorioDeAccionDTO recordatorioDeAccionDTO1 = new RecordatorioDeAccionDTO();
        recordatorioDeAccionDTO1.setId(1L);
        RecordatorioDeAccionDTO recordatorioDeAccionDTO2 = new RecordatorioDeAccionDTO();
        assertThat(recordatorioDeAccionDTO1).isNotEqualTo(recordatorioDeAccionDTO2);
        recordatorioDeAccionDTO2.setId(recordatorioDeAccionDTO1.getId());
        assertThat(recordatorioDeAccionDTO1).isEqualTo(recordatorioDeAccionDTO2);
        recordatorioDeAccionDTO2.setId(2L);
        assertThat(recordatorioDeAccionDTO1).isNotEqualTo(recordatorioDeAccionDTO2);
        recordatorioDeAccionDTO1.setId(null);
        assertThat(recordatorioDeAccionDTO1).isNotEqualTo(recordatorioDeAccionDTO2);
    }
}
