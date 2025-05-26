package com.ticket.terminal.security;

import com.ticket.terminal.entity.UserPermissionEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для загрузки пользователя и его прав доступа
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        // Создаем список прав доступа
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // Добавляем роль пользователя
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        
        // Если пользователь root, добавляем специальное право
        if (user.getIsRoot() != null && user.getIsRoot()) {
            authorities.add(new SimpleGrantedAuthority("ROOT"));
        }
        
        // Добавляем права доступа из таблицы permissions
        UserPermissionEntity permissions = user.getPermissions();
        if (permissions != null) {
            if (permissions.getCanManageUsers()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_USERS"));
            }
            if (permissions.getCanManageServices()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_SERVICES"));
            }
            if (permissions.getCanManageCategories()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_CATEGORIES"));
            }
            if (permissions.getCanManageVisitObjects()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_VISIT_OBJECTS"));
            }
            if (permissions.getCanViewReports()) {
                authorities.add(new SimpleGrantedAuthority("CAN_VIEW_REPORTS"));
            }
            if (permissions.getCanManageSettings()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_SETTINGS"));
            }
            if (permissions.getCanManageOrders()) {
                authorities.add(new SimpleGrantedAuthority("CAN_MANAGE_ORDERS"));
            }
            if (permissions.getCanExportData()) {
                authorities.add(new SimpleGrantedAuthority("CAN_EXPORT_DATA"));
            }
            if (permissions.getCanImportData()) {
                authorities.add(new SimpleGrantedAuthority("CAN_IMPORT_DATA"));
            }
        }

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
