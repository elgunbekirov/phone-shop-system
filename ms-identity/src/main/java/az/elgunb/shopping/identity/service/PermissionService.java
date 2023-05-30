package az.elgunb.shopping.identity.service;

import az.elgunb.shopping.common.error.exception.InvalidInputException;
import az.elgunb.shopping.identity.error.validation.ValidationMessage;
import az.elgunb.shopping.identity.mapper.PermissionMapper;
import az.elgunb.shopping.identity.repository.PermissionRepository;
import az.elgunb.shopping.identity.domain.Permission;
import az.elgunb.shopping.identity.dto.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionDto save(PermissionDto permissionDto) {
        Permission permission = permissionMapper.toEntity(permissionDto);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermissionDto findOne(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PERMISSION_NOT_FOUND, List.of(id)));
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

}
