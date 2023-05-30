package az.elgunb.shopping.identity.service;

import az.elgunb.shopping.common.error.exception.InvalidInputException;
import az.elgunb.shopping.identity.error.validation.ValidationMessage;
import az.elgunb.shopping.identity.repository.RoleRepository;
import az.elgunb.shopping.identity.domain.Role;
import az.elgunb.shopping.identity.dto.RoleDto;
import az.elgunb.shopping.identity.dto.RoleSmallDto;
import az.elgunb.shopping.identity.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto save(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        return roleRepository.findAllWithEagerRelationships().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> findAllWithEagerRelationships(Pageable pageable) {
        return roleRepository.findAllWithEagerRelationships(pageable).map(roleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public RoleDto findOne(Long id) {
        return roleRepository.findOneWithEagerRelationships(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.ROLE_NOT_FOUND, List.of(id)));
    }

    @Transactional(readOnly = true)
    public RoleSmallDto findByName(String name) {
        return roleRepository.findByNameEqualsIgnoreCase(name)
                .map(roleMapper::toSmallDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.ROLE_NOT_FOUND, List.of(name)));
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

}
