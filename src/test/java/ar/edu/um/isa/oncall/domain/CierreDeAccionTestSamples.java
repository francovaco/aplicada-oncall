package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CierreDeAccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CierreDeAccion getCierreDeAccionSample1() {
        return new CierreDeAccion().id(1L).comentarioCierre("comentarioCierre1");
    }

    public static CierreDeAccion getCierreDeAccionSample2() {
        return new CierreDeAccion().id(2L).comentarioCierre("comentarioCierre2");
    }

    public static CierreDeAccion getCierreDeAccionRandomSampleGenerator() {
        return new CierreDeAccion().id(longCount.incrementAndGet()).comentarioCierre(UUID.randomUUID().toString());
    }
}
