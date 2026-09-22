package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.CierreDeAccion} entity.
 */
@Schema(description = "Registro del cierre efectivo de una accion correctiva.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CierreDeAccionDTO implements Serializable {

    private Long id;

    @Size(max = 2000)
    private String comentarioCierre;

    @NotNull
    private Instant cerradoEn;

    @NotNull
    private AsignacionDeAccionDTO asignacion;

    @NotNull
    private UserDTO cerradoPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarioCierre() {
        return comentarioCierre;
    }

    public void setComentarioCierre(String comentarioCierre) {
        this.comentarioCierre = comentarioCierre;
    }

    public Instant getCerradoEn() {
        return cerradoEn;
    }

    public void setCerradoEn(Instant cerradoEn) {
        this.cerradoEn = cerradoEn;
    }

    public AsignacionDeAccionDTO getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(AsignacionDeAccionDTO asignacion) {
        this.asignacion = asignacion;
    }

    public UserDTO getCerradoPor() {
        return cerradoPor;
    }

    public void setCerradoPor(UserDTO cerradoPor) {
        this.cerradoPor = cerradoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CierreDeAccionDTO)) {
            return false;
        }

        CierreDeAccionDTO cierreDeAccionDTO = (CierreDeAccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cierreDeAccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CierreDeAccionDTO{" +
            "id=" + getId() +
            ", comentarioCierre='" + getComentarioCierre() + "'" +
            ", cerradoEn='" + getCerradoEn() + "'" +
            ", asignacion=" + getAsignacion() +
            ", cerradoPor=" + getCerradoPor() +
            "}";
    }
}
