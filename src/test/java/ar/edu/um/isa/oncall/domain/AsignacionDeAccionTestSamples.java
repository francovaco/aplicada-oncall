package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AsignacionDeAccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static AsignacionDeAccion getAsignacionDeAccionSample1() {
        return new AsignacionDeAccion().id(1L).titulo("titulo1").descripcion("descripcion1");
    }

    public static AsignacionDeAccion getAsignacionDeAccionSample2() {
        return new AsignacionDeAccion().id(2L).titulo("titulo2").descripcion("descripcion2");
    }

    public static AsignacionDeAccion getAsignacionDeAccionRandomSampleGenerator() {
        return new AsignacionDeAccion()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
