package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AsignacionDeAccionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsignacionDeAccionDTO.class);
        AsignacionDeAccionDTO asignacionDeAccionDTO1 = new AsignacionDeAccionDTO();
        asignacionDeAccionDTO1.setId(1L);
        AsignacionDeAccionDTO asignacionDeAccionDTO2 = new AsignacionDeAccionDTO();
        assertThat(asignacionDeAccionDTO1).isNotEqualTo(asignacionDeAccionDTO2);
        asignacionDeAccionDTO2.setId(asignacionDeAccionDTO1.getId());
        assertThat(asignacionDeAccionDTO1).isEqualTo(asignacionDeAccionDTO2);
        asignacionDeAccionDTO2.setId(2L);
        assertThat(asignacionDeAccionDTO1).isNotEqualTo(asignacionDeAccionDTO2);
        asignacionDeAccionDTO1.setId(null);
        assertThat(asignacionDeAccionDTO1).isNotEqualTo(asignacionDeAccionDTO2);
    }
}
