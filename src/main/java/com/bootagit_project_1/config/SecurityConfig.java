package com.bootagit_project_1.config;

import com.bootagit_project_1.config.security.CustomUserDetailsService;
import com.bootagit_project_1.jwt.JwtAuthenticationFilter;
import com.bootagit_project_1.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .securityContext(securityContext -> securityContext.requireExplicitSave(false))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 해당 API에 대해서는 모든 요청을 허가
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/socket.io/**").permitAll()
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .requestMatchers("/api/v1/task").authenticated()
                        .requestMatchers("/api/v1/task/**").authenticated()
                        // USER 권한이 있어야 요청할 수 있음
                        // hasRole 의 경우 앞에 접두사 "ROLE_"을 자동적으로 붙여서 체크하기 때문에 DB의 데이터가 ROLE_USER 이여야함
                        // 문자열 그대로 USER을 체크하고싶으면 hasAuthority()을 사용해야함
//                        .requestMatchers("/api/user/test").hasAuthority("USER") // Checks for USER
                        .requestMatchers("/api/user/test").hasRole("USER") // Checks for ROLE_USER

                        // 이 밖에 모든 요청에 대해서는 인증을 필요로 한다는 설정
                        .anyRequest().permitAll()
                )
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .exposedHeaders("location")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }



}
