package az.elgunb.shopping.identity.security;

import az.elgunb.shopping.common.security.model.CustomUserPrincipal;
import az.elgunb.shopping.identity.error.exception.DefaultUserRoleNotExistException;
import az.elgunb.shopping.identity.error.exception.UserNotEnabledException;
import az.elgunb.shopping.identity.error.exception.UserNotFoundException;
import az.elgunb.shopping.identity.repository.UserRepository;
import az.elgunb.shopping.identity.domain.Role;
import az.elgunb.shopping.identity.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public CustomUserPrincipal loadUserByUsername(String username) {
        User user = userRepository.findByUsernameEqualsIgnoreCase(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.getEnabled())
            throw new UserNotEnabledException();

        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRoles());
        if (authorities.isEmpty())
            throw new DefaultUserRoleNotExistException();

        return new CustomUserPrincipal(username,
                user.getPassword(),
                getFullName(user),
                user.getEnabled(),
                user.getType(),
                authorities);
    }

    private String getFullName(User user) {
        return String.join(" ", user.getFirstName(), user.getLastName());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    .forEach(authorities::add);
        }
        return authorities;
    }

}
