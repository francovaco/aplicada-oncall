package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.AccionCorrectivaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AsignacionDeAccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsignacionDeAccion.class);
        AsignacionDeAccion asignacionDeAccion1 = getAsignacionDeAccionSample1();
        AsignacionDeAccion asignacionDeAccion2 = new AsignacionDeAccion();
        assertThat(asignacionDeAccion1).isNotEqualTo(asignacionDeAccion2);

        asignacionDeAccion2.setId(asignacionDeAccion1.getId());
        assertThat(asignacionDeAccion1).isEqualTo(asignacionDeAccion2);

        asignacionDeAccion2 = getAsignacionDeAccionSample2();
        assertThat(asignacionDeAccion1).isNotEqualTo(asignacionDeAccion2);
    }

    @Test
    void accionCorrectivaTest() {
        AsignacionDeAccion asignacionDeAccion = getAsignacionDeAccionRandomSampleGenerator();
        AccionCorrectiva accionCorrectivaBack = getAccionCorrectivaRandomSampleGenerator();

        asignacionDeAccion.setAccionCorrectiva(accionCorrectivaBack);
        assertThat(asignacionDeAccion.getAccionCorrectiva()).isEqualTo(accionCorrectivaBack);

        asignacionDeAccion.accionCorrectiva(null);
        assertThat(asignacionDeAccion.getAccionCorrectiva()).isNull();
    }
}
