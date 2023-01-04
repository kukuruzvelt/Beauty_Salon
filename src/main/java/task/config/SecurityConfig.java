package task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@ComponentScan("task")
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomSimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/login/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin()
                .successHandler(customAuthenticationSuccessHandler()).permitAll()
        ;
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/user/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority("USER")
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain masterFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/master/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority("MASTER")
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority("ADMIN")
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain logoutFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/logout/**")
                .formLogin().and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/main")
        ;
        return http.build();
    }
}
