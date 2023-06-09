package az.elgunb.shopping.identity.mapper;

import az.elgunb.shopping.identity.domain.Permission;
import az.elgunb.shopping.identity.dto.PermissionDto;
import az.elgunb.shopping.identity.dto.PermissionSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDto, Permission> {

    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mapping(target = "roles", ignore = true)
    Permission toEntity(PermissionDto permissionDto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Permission toEntity(PermissionSmallDto permissionSmallDto);

    PermissionSmallDto toSmallDto(Permission permission);

    default Permission fromId(Long id) {
        if (id == null) return null;
        Permission permission = new Permission();
        permission.setId(id);
        return permission;
    }

}
