package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AsignacionDeAccionCriteriaTest {

    @Test
    void newAsignacionDeAccionCriteriaHasAllFiltersNullTest() {
        var asignacionDeAccionCriteria = new AsignacionDeAccionCriteria();
        assertThat(asignacionDeAccionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void asignacionDeAccionCriteriaFluentMethodsCreatesFiltersTest() {
        var asignacionDeAccionCriteria = new AsignacionDeAccionCriteria();

        setAllFilters(asignacionDeAccionCriteria);

        assertThat(asignacionDeAccionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void asignacionDeAccionCriteriaCopyCreatesNullFilterTest() {
        var asignacionDeAccionCriteria = new AsignacionDeAccionCriteria();
        var copy = asignacionDeAccionCriteria.copy();

        assertThat(asignacionDeAccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(asignacionDeAccionCriteria)
        );
    }

    @Test
    void asignacionDeAccionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var asignacionDeAccionCriteria = new AsignacionDeAccionCriteria();
        setAllFilters(asignacionDeAccionCriteria);

        var copy = asignacionDeAccionCriteria.copy();

        assertThat(asignacionDeAccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(asignacionDeAccionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var asignacionDeAccionCriteria = new AsignacionDeAccionCriteria();

        assertThat(asignacionDeAccionCriteria).hasToString("AsignacionDeAccionCriteria{}");
    }

    private static void setAllFilters(AsignacionDeAccionCriteria asignacionDeAccionCriteria) {
        asignacionDeAccionCriteria.id();
        asignacionDeAccionCriteria.titulo();
        asignacionDeAccionCriteria.descripcion();
        asignacionDeAccionCriteria.fechaVencimiento();
        asignacionDeAccionCriteria.estado();
        asignacionDeAccionCriteria.creadaEn();
        asignacionDeAccionCriteria.accionCorrectivaId();
        asignacionDeAccionCriteria.responsableId();
        asignacionDeAccionCriteria.distinct();
    }

    private static Condition<AsignacionDeAccionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitulo()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFechaVencimiento()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getCreadaEn()) &&
                condition.apply(criteria.getAccionCorrectivaId()) &&
                condition.apply(criteria.getResponsableId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AsignacionDeAccionCriteria> copyFiltersAre(
        AsignacionDeAccionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitulo(), copy.getTitulo()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFechaVencimiento(), copy.getFechaVencimiento()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getCreadaEn(), copy.getCreadaEn()) &&
                condition.apply(criteria.getAccionCorrectivaId(), copy.getAccionCorrectivaId()) &&
                condition.apply(criteria.getResponsableId(), copy.getResponsableId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
