package com.blog.framework.config;

import com.blog.framework.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomUserDetailService customUserDetailsService;

    private final static String[] PERMIT_URL={
            "/signup",
            "/login"
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    // 스프링 시큐리티 기능 비활성화 (H2 DB 접근을 위해)
	@Bean
	public WebSecurityCustomizer configure() {
		return (web -> web.ignoring()
                .requestMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**"));
	}

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        Object AbstractHttpConfigurer;
        http	.csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                //ß.formLogin(AbstractHttpConfigurer::disable
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PERMIT_URL).permitAll()
                        .anyRequest().authenticated())
                // 폼 로그인은 현재 사용하지 않음
				.formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/login") // 서버에서 로그인 요청을 처리할 URL
                        .usernameParameter("email") // 이메일을 username으로 사용
                        .passwordParameter("password")
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 후 클라이언트 애플리케이션으로 리다이렉트
                            response.sendRedirect("http://localhost:3000/signin?error");
                        })
                        .permitAll())
                .logout((logout) -> logout
                        //.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 프론트엔드 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}