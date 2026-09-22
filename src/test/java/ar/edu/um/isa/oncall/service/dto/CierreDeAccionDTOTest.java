package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CierreDeAccionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CierreDeAccionDTO.class);
        CierreDeAccionDTO cierreDeAccionDTO1 = new CierreDeAccionDTO();
        cierreDeAccionDTO1.setId(1L);
        CierreDeAccionDTO cierreDeAccionDTO2 = new CierreDeAccionDTO();
        assertThat(cierreDeAccionDTO1).isNotEqualTo(cierreDeAccionDTO2);
        cierreDeAccionDTO2.setId(cierreDeAccionDTO1.getId());
        assertThat(cierreDeAccionDTO1).isEqualTo(cierreDeAccionDTO2);
        cierreDeAccionDTO2.setId(2L);
        assertThat(cierreDeAccionDTO1).isNotEqualTo(cierreDeAccionDTO2);
        cierreDeAccionDTO1.setId(null);
        assertThat(cierreDeAccionDTO1).isNotEqualTo(cierreDeAccionDTO2);
    }
}
