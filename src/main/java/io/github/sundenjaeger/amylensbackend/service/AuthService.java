// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String username, String currentPassword, String newPassword) {
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        if (userDetailsService instanceof InMemoryUserDetailsManager manager) {
            manager.updatePassword(user, passwordEncoder.encode(newPassword));
        } else {
            throw new UnsupportedOperationException(
                    "Password reset not yet supported for this UserDetailsService implementation");
        }
    }
}
