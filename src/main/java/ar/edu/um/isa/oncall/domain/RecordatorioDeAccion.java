package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Recordatorio automatico enviado cuando la accion esta por vencer o vencio.
 */
@Entity
@Table(name = "recordatorio_de_accion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecordatorioDeAccion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "mensaje", length = 500, nullable = false)
    private String mensaje;

    @Column(name = "enviado_en")
    private Instant enviadoEn;

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    @Column(name = "nivel_escalamiento", nullable = false)
    private Integer nivelEscalamiento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "accionCorrectiva", "responsable" }, allowSetters = true)
    private AsignacionDeAccion asignacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecordatorioDeAccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public RecordatorioDeAccion mensaje(String mensaje) {
        this.setMensaje(mensaje);
        return this;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Instant getEnviadoEn() {
        return this.enviadoEn;
    }

    public RecordatorioDeAccion enviadoEn(Instant enviadoEn) {
        this.setEnviadoEn(enviadoEn);
        return this;
    }

    public void setEnviadoEn(Instant enviadoEn) {
        this.enviadoEn = enviadoEn;
    }

    public Integer getNivelEscalamiento() {
        return this.nivelEscalamiento;
    }

    public RecordatorioDeAccion nivelEscalamiento(Integer nivelEscalamiento) {
        this.setNivelEscalamiento(nivelEscalamiento);
        return this;
    }

    public void setNivelEscalamiento(Integer nivelEscalamiento) {
        this.nivelEscalamiento = nivelEscalamiento;
    }

    public AsignacionDeAccion getAsignacion() {
        return this.asignacion;
    }

    public void setAsignacion(AsignacionDeAccion asignacionDeAccion) {
        this.asignacion = asignacionDeAccion;
    }

    public RecordatorioDeAccion asignacion(AsignacionDeAccion asignacionDeAccion) {
        this.setAsignacion(asignacionDeAccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordatorioDeAccion)) {
            return false;
        }
        return getId() != null && getId().equals(((RecordatorioDeAccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordatorioDeAccion{" +
            "id=" + getId() +
            ", mensaje='" + getMensaje() + "'" +
            ", enviadoEn='" + getEnviadoEn() + "'" +
            ", nivelEscalamiento=" + getNivelEscalamiento() +
            "}";
    }
}
