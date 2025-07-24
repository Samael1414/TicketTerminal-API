package com.ticket.terminal.security;

import com.ticket.terminal.entity.UserPermissionEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.enums.PermissionType;
import com.ticket.terminal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 * Сервис для загрузки пользователя и его прав доступа
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_ROOT = "ROLE_ROOT";

    @Override
    public UserDetails loadUserByUsername(String username) {
        UsersEntity user = userRepository
                .findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        Set<SimpleGrantedAuthority> authorities = mapAuthorities(user);

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    private Set<SimpleGrantedAuthority> mapAuthorities(UsersEntity user) {
        Set<SimpleGrantedAuthority> auths = new HashSet<>();

        // 1) базовая роль
        auths.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        // 2) root
        if (Boolean.TRUE.equals(user.getIsRoot())) {
            auths.add(new SimpleGrantedAuthority("ROLE_ROOT"));
        }

        // 3) бизнесс-права
        Optional.ofNullable(user.getPermissions())
                .ifPresent(permission -> {
                    Stream.of(PermissionType.values())
                            .filter(permissionType -> permissionType.isEnabled(permission))
                            .map(PermissionType::grantedAuthority)
                            .forEach(auths::add);
                });

        return auths;
    }

}
