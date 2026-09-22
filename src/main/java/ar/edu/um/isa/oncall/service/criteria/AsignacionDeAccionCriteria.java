package ar.edu.um.isa.oncall.service.criteria;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoAccion;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.AsignacionDeAccion} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.AsignacionDeAccionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asignacion-de-accions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsignacionDeAccionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoAccion
     */
    public static class EstadoAccionFilter extends Filter<EstadoAccion> {

        public EstadoAccionFilter() {}

        public EstadoAccionFilter(EstadoAccionFilter filter) {
            super(filter);
        }

        @Override
        public EstadoAccionFilter copy() {
            return new EstadoAccionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private StringFilter descripcion;

    private LocalDateFilter fechaVencimiento;

    private EstadoAccionFilter estado;

    private InstantFilter creadaEn;

    private LongFilter accionCorrectivaId;

    private LongFilter responsableId;

    private Boolean distinct;

    public AsignacionDeAccionCriteria() {}

    public AsignacionDeAccionCriteria(AsignacionDeAccionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titulo = other.optionalTitulo().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.fechaVencimiento = other.optionalFechaVencimiento().map(LocalDateFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoAccionFilter::copy).orElse(null);
        this.creadaEn = other.optionalCreadaEn().map(InstantFilter::copy).orElse(null);
        this.accionCorrectivaId = other.optionalAccionCorrectivaId().map(LongFilter::copy).orElse(null);
        this.responsableId = other.optionalResponsableId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AsignacionDeAccionCriteria copy() {
        return new AsignacionDeAccionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public Optional<StringFilter> optionalTitulo() {
        return Optional.ofNullable(titulo);
    }

    public StringFilter titulo() {
        if (titulo == null) {
            setTitulo(new StringFilter());
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateFilter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Optional<LocalDateFilter> optionalFechaVencimiento() {
        return Optional.ofNullable(fechaVencimiento);
    }

    public LocalDateFilter fechaVencimiento() {
        if (fechaVencimiento == null) {
            setFechaVencimiento(new LocalDateFilter());
        }
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateFilter fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoAccionFilter getEstado() {
        return estado;
    }

    public Optional<EstadoAccionFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoAccionFilter estado() {
        if (estado == null) {
            setEstado(new EstadoAccionFilter());
        }
        return estado;
    }

    public void setEstado(EstadoAccionFilter estado) {
        this.estado = estado;
    }

    public InstantFilter getCreadaEn() {
        return creadaEn;
    }

    public Optional<InstantFilter> optionalCreadaEn() {
        return Optional.ofNullable(creadaEn);
    }

    public InstantFilter creadaEn() {
        if (creadaEn == null) {
            setCreadaEn(new InstantFilter());
        }
        return creadaEn;
    }

    public void setCreadaEn(InstantFilter creadaEn) {
        this.creadaEn = creadaEn;
    }

    public LongFilter getAccionCorrectivaId() {
        return accionCorrectivaId;
    }

    public Optional<LongFilter> optionalAccionCorrectivaId() {
        return Optional.ofNullable(accionCorrectivaId);
    }

    public LongFilter accionCorrectivaId() {
        if (accionCorrectivaId == null) {
            setAccionCorrectivaId(new LongFilter());
        }
        return accionCorrectivaId;
    }

    public void setAccionCorrectivaId(LongFilter accionCorrectivaId) {
        this.accionCorrectivaId = accionCorrectivaId;
    }

    public LongFilter getResponsableId() {
        return responsableId;
    }

    public Optional<LongFilter> optionalResponsableId() {
        return Optional.ofNullable(responsableId);
    }

    public LongFilter responsableId() {
        if (responsableId == null) {
            setResponsableId(new LongFilter());
        }
        return responsableId;
    }

    public void setResponsableId(LongFilter responsableId) {
        this.responsableId = responsableId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AsignacionDeAccionCriteria that = (AsignacionDeAccionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(creadaEn, that.creadaEn) &&
            Objects.equals(accionCorrectivaId, that.accionCorrectivaId) &&
            Objects.equals(responsableId, that.responsableId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, fechaVencimiento, estado, creadaEn, accionCorrectivaId, responsableId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsignacionDeAccionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitulo().map(f -> "titulo=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalFechaVencimiento().map(f -> "fechaVencimiento=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalCreadaEn().map(f -> "creadaEn=" + f + ", ").orElse("") +
            optionalAccionCorrectivaId().map(f -> "accionCorrectivaId=" + f + ", ").orElse("") +
            optionalResponsableId().map(f -> "responsableId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
