package sn.esmt.gestionprojets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sn.esmt.gestionprojets.security.UserDetailsServiceImpl;

/**
 * Configuration Spring Security.
 * Gère l'authentification, les autorisations et OAuth 2.0.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configuration principale de la sécurité.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuration des autorisations
                .authorizeHttpRequests(auth -> auth
                        // Pages publiques (accessibles sans authentification)
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()


                        
                        // API REST - Endpoints par rôle
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/projets/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/statistics/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/domaines/**").hasRole("ADMIN")

                        // Pages Web - Accès par rôle
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/profil", "/profil/**").authenticated()
                        .requestMatchers("/projets/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/statistics/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/v3/api-docs/**"   // ← OBLIGATOIRE
                        ).permitAll()
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // Configuration du formulaire de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")  // On utilise l'email comme identifiant
                        .passwordParameter("password")
                        .permitAll()
                )

                // Configuration OAuth 2.0 (Google)
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                )

                // Configuration de la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // Gestion des exceptions
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                )

                // Configuration CSRF (désactivé pour l'API REST, actif pour le web)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // Désactiver CSRF pour l'API
                        .ignoringRequestMatchers("/v3/api-docs/**")
                );

        return http.build();
    }

    /**
     * Encodeur de mot de passe BCrypt.
     * Utilisé pour hasher les mots de passe avant de les stocker.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}