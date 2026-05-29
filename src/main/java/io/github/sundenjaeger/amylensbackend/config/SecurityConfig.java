package io.github.sundenjaeger.amylensbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final TeamLeadUserDetailsService teamLeadUserDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(teamLeadUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/api/devices/register").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/devices/auth").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/devices/*/users").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/varieties").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/sessions").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/devices/researchers").permitAll()
//                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/swagger-ui.html").permitAll()
//                        .requestMatchers("/api-docs/**").permitAll()
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .cors(cors -> {
                })
//                .formLogin(form -> form
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/api/devices", true)
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessHandler((request, response, authentication) ->
//                                response.setStatus(HttpServletResponse.SC_OK))
//                        .permitAll()
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .build();
    }
}