package io.github.sundenjaeger.amylensbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public HttpSessionSecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .securityContext(ctx -> ctx
                        .securityContextRepository(securityContextRepository()))
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
//                                .requestMatchers("/api/auth/login").permitAll()
//                                .requestMatchers("/api/auth/register").permitAll()
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