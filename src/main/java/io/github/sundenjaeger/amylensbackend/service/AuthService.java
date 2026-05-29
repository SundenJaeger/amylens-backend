// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.config.CustomUserDetails;
import io.github.sundenjaeger.amylensbackend.dto.RegisterRequest;
import io.github.sundenjaeger.amylensbackend.dto.RegisterResponse;
import io.github.sundenjaeger.amylensbackend.enums.RoleType;
import io.github.sundenjaeger.amylensbackend.exception.UserAlreadyExistException;
import io.github.sundenjaeger.amylensbackend.model.User;
import io.github.sundenjaeger.amylensbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist: " + username));

        return new CustomUserDetails(user);
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("This user already exist: " + user.getUsername());
                });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(RoleType.ADMIN);

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.getRole());
    }

//    @Transactional
//    public void resetPassword(String username, String currentPassword, String newPassword) {
//        UserDetails user = userDetailsService.loadUserByUsername(username);
//
//        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
//            throw new BadCredentialsException("Current password is incorrect");
//        }
//
//        User lead = userRepository.findByUsername(username)
//                .orElseThrow(() -> new BadCredentialsException("User not found"));
//
//        lead.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(lead);
//    }
}