package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.RecordatorioDeAccion;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.dto.RecordatorioDeAccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecordatorioDeAccion} and its DTO {@link RecordatorioDeAccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecordatorioDeAccionMapper extends EntityMapper<RecordatorioDeAccionDTO, RecordatorioDeAccion> {
    @Mapping(target = "asignacion", source = "asignacion", qualifiedByName = "asignacionDeAccionId")
    RecordatorioDeAccionDTO toDto(RecordatorioDeAccion s);

    @Named("asignacionDeAccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsignacionDeAccionDTO toDtoAsignacionDeAccionId(AsignacionDeAccion asignacionDeAccion);
}
