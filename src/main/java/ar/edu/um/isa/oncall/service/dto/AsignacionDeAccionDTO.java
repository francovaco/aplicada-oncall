package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoAccion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.AsignacionDeAccion} entity.
 */
@Schema(description = "Accion correctiva de postmortem con seguimiento y vencimiento.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsignacionDeAccionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String titulo;

    @Size(max = 2000)
    private String descripcion;

    @NotNull
    private LocalDate fechaVencimiento;

    @NotNull
    private EstadoAccion estado;

    @NotNull
    private Instant creadaEn;

    @NotNull
    private AccionCorrectivaDTO accionCorrectiva;

    private UserDTO responsable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoAccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAccion estado) {
        this.estado = estado;
    }

    public Instant getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(Instant creadaEn) {
        this.creadaEn = creadaEn;
    }

    public AccionCorrectivaDTO getAccionCorrectiva() {
        return accionCorrectiva;
    }

    public void setAccionCorrectiva(AccionCorrectivaDTO accionCorrectiva) {
        this.accionCorrectiva = accionCorrectiva;
    }

    public UserDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(UserDTO responsable) {
        this.responsable = responsable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsignacionDeAccionDTO)) {
            return false;
        }

        AsignacionDeAccionDTO asignacionDeAccionDTO = (AsignacionDeAccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, asignacionDeAccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsignacionDeAccionDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", estado='" + getEstado() + "'" +
            ", creadaEn='" + getCreadaEn() + "'" +
            ", accionCorrectiva=" + getAccionCorrectiva() +
            ", responsable=" + getResponsable() +
            "}";
    }
}
