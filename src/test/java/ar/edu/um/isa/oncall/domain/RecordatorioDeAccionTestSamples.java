package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecordatorioDeAccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static RecordatorioDeAccion getRecordatorioDeAccionSample1() {
        return new RecordatorioDeAccion().id(1L).mensaje("mensaje1").nivelEscalamiento(1);
    }

    public static RecordatorioDeAccion getRecordatorioDeAccionSample2() {
        return new RecordatorioDeAccion().id(2L).mensaje("mensaje2").nivelEscalamiento(2);
    }

    public static RecordatorioDeAccion getRecordatorioDeAccionRandomSampleGenerator() {
        return new RecordatorioDeAccion()
            .id(longCount.incrementAndGet())
            .mensaje(UUID.randomUUID().toString())
            .nivelEscalamiento(intCount.incrementAndGet());
    }
}
