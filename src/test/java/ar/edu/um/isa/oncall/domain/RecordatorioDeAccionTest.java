package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.AsignacionDeAccionTestSamples.*;
import static ar.edu.um.isa.oncall.domain.RecordatorioDeAccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordatorioDeAccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordatorioDeAccion.class);
        RecordatorioDeAccion recordatorioDeAccion1 = getRecordatorioDeAccionSample1();
        RecordatorioDeAccion recordatorioDeAccion2 = new RecordatorioDeAccion();
        assertThat(recordatorioDeAccion1).isNotEqualTo(recordatorioDeAccion2);

        recordatorioDeAccion2.setId(recordatorioDeAccion1.getId());
        assertThat(recordatorioDeAccion1).isEqualTo(recordatorioDeAccion2);

        recordatorioDeAccion2 = getRecordatorioDeAccionSample2();
        assertThat(recordatorioDeAccion1).isNotEqualTo(recordatorioDeAccion2);
    }

    @Test
    void asignacionTest() {
        RecordatorioDeAccion recordatorioDeAccion = getRecordatorioDeAccionRandomSampleGenerator();
        AsignacionDeAccion asignacionDeAccionBack = getAsignacionDeAccionRandomSampleGenerator();

        recordatorioDeAccion.setAsignacion(asignacionDeAccionBack);
        assertThat(recordatorioDeAccion.getAsignacion()).isEqualTo(asignacionDeAccionBack);

        recordatorioDeAccion.asignacion(null);
        assertThat(recordatorioDeAccion.getAsignacion()).isNull();
    }
}
