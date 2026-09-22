package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.CierreDeAccion;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.dto.CierreDeAccionDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CierreDeAccion} and its DTO {@link CierreDeAccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CierreDeAccionMapper extends EntityMapper<CierreDeAccionDTO, CierreDeAccion> {
    @Mapping(target = "asignacion", source = "asignacion", qualifiedByName = "asignacionDeAccionId")
    @Mapping(target = "cerradoPor", source = "cerradoPor", qualifiedByName = "userLogin")
    CierreDeAccionDTO toDto(CierreDeAccion s);

    @Named("asignacionDeAccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsignacionDeAccionDTO toDtoAsignacionDeAccionId(AsignacionDeAccion asignacionDeAccion);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
