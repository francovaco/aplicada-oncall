package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoAccion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Accion correctiva de postmortem con seguimiento y vencimiento.
 */
@Entity
@Table(name = "asignacion_de_accion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsignacionDeAccion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "titulo", length = 255, nullable = false)
    private String titulo;

    @Size(max = 2000)
    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @NotNull
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoAccion estado;

    @NotNull
    @Column(name = "creada_en", nullable = false)
    private Instant creadaEn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "postmortem", "responsable" }, allowSetters = true)
    private AccionCorrectiva accionCorrectiva;

    @ManyToOne(fetch = FetchType.LAZY)
    private User responsable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AsignacionDeAccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public AsignacionDeAccion titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public AsignacionDeAccion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public AsignacionDeAccion fechaVencimiento(LocalDate fechaVencimiento) {
        this.setFechaVencimiento(fechaVencimiento);
        return this;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoAccion getEstado() {
        return this.estado;
    }

    public AsignacionDeAccion estado(EstadoAccion estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoAccion estado) {
        this.estado = estado;
    }

    public Instant getCreadaEn() {
        return this.creadaEn;
    }

    public AsignacionDeAccion creadaEn(Instant creadaEn) {
        this.setCreadaEn(creadaEn);
        return this;
    }

    public void setCreadaEn(Instant creadaEn) {
        this.creadaEn = creadaEn;
    }

    public AccionCorrectiva getAccionCorrectiva() {
        return this.accionCorrectiva;
    }

    public void setAccionCorrectiva(AccionCorrectiva accionCorrectiva) {
        this.accionCorrectiva = accionCorrectiva;
    }

    public AsignacionDeAccion accionCorrectiva(AccionCorrectiva accionCorrectiva) {
        this.setAccionCorrectiva(accionCorrectiva);
        return this;
    }

    public User getResponsable() {
        return this.responsable;
    }

    public void setResponsable(User user) {
        this.responsable = user;
    }

    public AsignacionDeAccion responsable(User user) {
        this.setResponsable(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsignacionDeAccion)) {
            return false;
        }
        return getId() != null && getId().equals(((AsignacionDeAccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsignacionDeAccion{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", estado='" + getEstado() + "'" +
            ", creadaEn='" + getCreadaEn() + "'" +
            "}";
    }
}
