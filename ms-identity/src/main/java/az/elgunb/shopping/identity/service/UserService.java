package az.elgunb.shopping.identity.service;

import az.elgunb.shopping.common.enums.UserType;
import az.elgunb.shopping.common.error.exception.InvalidInputException;
import az.elgunb.shopping.common.security.util.SecurityUtil;
import az.elgunb.shopping.identity.error.validation.ValidationMessage;
import az.elgunb.shopping.identity.repository.UserRepository;
import az.elgunb.shopping.identity.domain.User;
import az.elgunb.shopping.identity.dto.UserDto;
import az.elgunb.shopping.identity.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static az.elgunb.shopping.common.security.util.SecurityUtil.getCurrentUserLogin;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllWithEagerRelationships().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllCustomers() {
        return userRepository.findAllByType(UserType.CUSTOMER).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findOne(Long id) {
        Optional<User> user = SecurityUtil.hasAdminRole() ? userRepository.findOneWithEagerRelationships(id) :
                userRepository.findByIdAndUsername(id, getCurrentUserLogin());
        return user.map(userMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.USER_NOT_FOUND, List.of(id)));
    }

    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
