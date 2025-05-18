package com.inventariopro.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.inventariopro.crud.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
                  "/auth/login",
                  "/auth/register",
                  "/error",
                  "/swagger-ui.html",
                  "/swagger-ui/**",
                  "/v3/api-docs/**",
                    "/uploads/**"
              ).permitAll()
              .requestMatchers("/proveedores/**").authenticated()
              .requestMatchers("/categorias").permitAll()
              .anyRequest().authenticated()
          )
          .sessionManagement(sessionManager -> sessionManager
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authProvider)
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .build();
  }



  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:3000");  // Asegúrate de usar el origen correcto
    configuration.addAllowedMethod("GET");
    configuration.addAllowedMethod("POST");
    configuration.addAllowedMethod("PUT");
    configuration.addAllowedMethod("DELETE");
    configuration.addAllowedMethod("OPTIONS");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);

    // Aquí configuramos que se permita el acceso desde cualquier ruta.
    org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
