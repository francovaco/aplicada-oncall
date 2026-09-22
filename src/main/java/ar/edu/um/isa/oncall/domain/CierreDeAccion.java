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
 * Registro del cierre efectivo de una accion correctiva.
 */
@Entity
@Table(name = "cierre_de_accion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CierreDeAccion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 2000)
    @Column(name = "comentario_cierre", length = 2000)
    private String comentarioCierre;

    @NotNull
    @Column(name = "cerrado_en", nullable = false)
    private Instant cerradoEn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "accionCorrectiva", "responsable" }, allowSetters = true)
    private AsignacionDeAccion asignacion;

    @ManyToOne(optional = false)
    @NotNull
    private User cerradoPor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CierreDeAccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarioCierre() {
        return this.comentarioCierre;
    }

    public CierreDeAccion comentarioCierre(String comentarioCierre) {
        this.setComentarioCierre(comentarioCierre);
        return this;
    }

    public void setComentarioCierre(String comentarioCierre) {
        this.comentarioCierre = comentarioCierre;
    }

    public Instant getCerradoEn() {
        return this.cerradoEn;
    }

    public CierreDeAccion cerradoEn(Instant cerradoEn) {
        this.setCerradoEn(cerradoEn);
        return this;
    }

    public void setCerradoEn(Instant cerradoEn) {
        this.cerradoEn = cerradoEn;
    }

    public AsignacionDeAccion getAsignacion() {
        return this.asignacion;
    }

    public void setAsignacion(AsignacionDeAccion asignacionDeAccion) {
        this.asignacion = asignacionDeAccion;
    }

    public CierreDeAccion asignacion(AsignacionDeAccion asignacionDeAccion) {
        this.setAsignacion(asignacionDeAccion);
        return this;
    }

    public User getCerradoPor() {
        return this.cerradoPor;
    }

    public void setCerradoPor(User user) {
        this.cerradoPor = user;
    }

    public CierreDeAccion cerradoPor(User user) {
        this.setCerradoPor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CierreDeAccion)) {
            return false;
        }
        return getId() != null && getId().equals(((CierreDeAccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CierreDeAccion{" +
            "id=" + getId() +
            ", comentarioCierre='" + getComentarioCierre() + "'" +
            ", cerradoEn='" + getCerradoEn() + "'" +
            "}";
    }
}
