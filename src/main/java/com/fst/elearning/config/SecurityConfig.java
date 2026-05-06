package com.fst.elearning.config;
import com.fst.elearning.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Pages publiques
                .requestMatchers("/", "/catalogue", "/cours/{id}/detail",
                                 "/auth/**", "/css/**", "/js/**", "/img/**", "/uploads/**",
                                 "/panier/**", "/paiement/offres", "/paiement/checkout", "/paiement/success",
                                 "/webjars/**").permitAll()
                // Quiz (accessible aux apprenants et formateurs)
                .requestMatchers("/quiz/**").hasAnyRole("APPRENANT", "FORMATEUR", "ADMIN")
                // API Quiz
                .requestMatchers("/api/quiz/**").hasAnyRole("APPRENANT", "FORMATEUR", "ADMIN")
                // Espace apprenant
                .requestMatchers("/apprenant/**").hasRole("APPRENANT")
                .requestMatchers("/lecons/**").hasRole("APPRENANT")
                // Espace parent
                .requestMatchers("/parent/**").hasRole("PARENT")
                // Espace formateur
                .requestMatchers("/formateur/**").hasRole("FORMATEUR")
                // Espace admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Tout le reste nécessite une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/auth/access-denied")
            );
        return http.build();
    }
}