package com.jdragon.apex.config;

import com.jdragon.apex.fliter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(createAntPathReqMatcher()).permitAll()
                        .anyRequest().authenticated()
                ).formLogin(AbstractAuthenticationFilterConfigurer::permitAll)  // 允许所有用户访问登录页面
                .logout(LogoutConfigurer::permitAll)  // 允许所有用户执行注销操作
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    /**
     * 可匿名访问的静态资源 非Spring MVC端点请求， 需要特别指明，否则springboot无法启动
     *
     * @return 非Spring MVC端点请求集合
     */
    private AntPathRequestMatcher[] createAntPathReqMatcher() {
        return new AntPathRequestMatcher[]{
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/*.html"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/**/*.html"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/**/*.css"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/**/*.js"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/profile/**"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/cqhttp/**"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/ag/machineBindKeys"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/ag/validate"),
                AntPathRequestMatcher.antMatcher( "/test/**"),
                AntPathRequestMatcher.antMatcher("/ban/today"),

                AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/*/api-docs"),
                AntPathRequestMatcher.antMatcher("/druid/**"),
                AntPathRequestMatcher.antMatcher("/resources/**")
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // 身份认证接口
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
