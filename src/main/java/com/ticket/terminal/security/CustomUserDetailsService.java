package com.ticket.terminal.security;

import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                .build();

    }
}
