package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionTestSamples.*;
import static ar.edu.um.isa.oncall.domain.CierreDeAccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CierreDeAccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CierreDeAccion.class);
        CierreDeAccion cierreDeAccion1 = getCierreDeAccionSample1();
        CierreDeAccion cierreDeAccion2 = new CierreDeAccion();
        assertThat(cierreDeAccion1).isNotEqualTo(cierreDeAccion2);

        cierreDeAccion2.setId(cierreDeAccion1.getId());
        assertThat(cierreDeAccion1).isEqualTo(cierreDeAccion2);

        cierreDeAccion2 = getCierreDeAccionSample2();
        assertThat(cierreDeAccion1).isNotEqualTo(cierreDeAccion2);
    }

    @Test
    void asignacionTest() {
        CierreDeAccion cierreDeAccion = getCierreDeAccionRandomSampleGenerator();
        AsignacionDeAccion asignacionDeAccionBack = getAsignacionDeAccionRandomSampleGenerator();

        cierreDeAccion.setAsignacion(asignacionDeAccionBack);
        assertThat(cierreDeAccion.getAsignacion()).isEqualTo(asignacionDeAccionBack);

        cierreDeAccion.asignacion(null);
        assertThat(cierreDeAccion.getAsignacion()).isNull();
    }
}
