package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.AccionCorrectiva;
import ar.edu.um.isa.oncall.domain.AsignacionDeAccion;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.service.dto.AccionCorrectivaDTO;
import ar.edu.um.isa.oncall.service.dto.AsignacionDeAccionDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsignacionDeAccion} and its DTO {@link AsignacionDeAccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AsignacionDeAccionMapper extends EntityMapper<AsignacionDeAccionDTO, AsignacionDeAccion> {
    @Mapping(target = "accionCorrectiva", source = "accionCorrectiva", qualifiedByName = "accionCorrectivaId")
    @Mapping(target = "responsable", source = "responsable", qualifiedByName = "userLogin")
    AsignacionDeAccionDTO toDto(AsignacionDeAccion s);

    @Named("accionCorrectivaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccionCorrectivaDTO toDtoAccionCorrectivaId(AccionCorrectiva accionCorrectiva);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
