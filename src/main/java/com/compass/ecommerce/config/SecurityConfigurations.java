package com.compass.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

	@Autowired
	SecurityFilter securityFilter;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                		
                		//permissões para usuários
                        .requestMatchers(HttpMethod.GET, "/products").hasAnyRole("ADMIN", "USER")
                        
                        .requestMatchers(HttpMethod.GET, "/sales").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/sales").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/sales").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/sale-products").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/sale-products").hasAnyRole("ADMIN", "USER")
                        
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products").hasRole("ADMIN")
                        

                        

                        .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/sale-products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
