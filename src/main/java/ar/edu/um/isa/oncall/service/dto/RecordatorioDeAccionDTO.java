package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.RecordatorioDeAccion} entity.
 */
@Schema(description = "Recordatorio automatico enviado cuando la accion esta por vencer o vencio.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecordatorioDeAccionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String mensaje;

    private Instant enviadoEn;

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    private Integer nivelEscalamiento;

    @NotNull
    private AsignacionDeAccionDTO asignacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Instant getEnviadoEn() {
        return enviadoEn;
    }

    public void setEnviadoEn(Instant enviadoEn) {
        this.enviadoEn = enviadoEn;
    }

    public Integer getNivelEscalamiento() {
        return nivelEscalamiento;
    }

    public void setNivelEscalamiento(Integer nivelEscalamiento) {
        this.nivelEscalamiento = nivelEscalamiento;
    }

    public AsignacionDeAccionDTO getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(AsignacionDeAccionDTO asignacion) {
        this.asignacion = asignacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordatorioDeAccionDTO)) {
            return false;
        }

        RecordatorioDeAccionDTO recordatorioDeAccionDTO = (RecordatorioDeAccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recordatorioDeAccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordatorioDeAccionDTO{" +
            "id=" + getId() +
            ", mensaje='" + getMensaje() + "'" +
            ", enviadoEn='" + getEnviadoEn() + "'" +
            ", nivelEscalamiento=" + getNivelEscalamiento() +
            ", asignacion=" + getAsignacion() +
            "}";
    }
}
