package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.auth.CorsFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    // Api prefix for all requests
    private static final String API_PREFIX = "/api/v1/";

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetails;
    @Autowired
    private JwtTokenExtractor tokenExtractor;
    @Autowired
    private JwtLoginAuthenticationSuccessHandler successHandler;
    @Autowired
    private JwtLoginAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // Adding a CORS filter
        http.addFilterBefore(this.corsFilter, ChannelProcessingFilter.class);

        // CSRF is not needed
        http.csrf().disable();

        // Setting the session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Setting authorized requests
        http.authorizeRequests()
                // Adding USERS controller authorizations
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/deleted_snippets").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/favorite_snippets").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/following_snippets").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/following_tags").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/upvoted_snippets").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, API_PREFIX + "users/" + Constants.RECOVER_PASSWORD).anonymous()
                .antMatchers(HttpMethod.PUT, API_PREFIX + "users/*/profile_photo").hasRole("USER")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "users/*/description").hasRole("USER")
                .antMatchers(HttpMethod.POST, API_PREFIX + "users/*/send_verify_email").hasRole("USER")
                .antMatchers(HttpMethod.POST, API_PREFIX + "users/*/verify_email").hasRole("USER")
                .antMatchers(HttpMethod.POST, API_PREFIX + "users/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/**").permitAll()
                // Adding TAGS controller authorizations
                .antMatchers(HttpMethod.POST, API_PREFIX + "tags/*/").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "tags/*/").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "tags/*/follow").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "tags/*/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "tags/exists").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "tags/*/users/*/follows").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "tags/**").permitAll()
                // Adding LANGUAGES controller authorizations
                .antMatchers(HttpMethod.GET, API_PREFIX + "languages/**").permitAll()
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "languages/*/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "languages/exists").hasRole("ADMIN")
                // Adding LOGIN policy
                .antMatchers(HttpMethod.POST, API_PREFIX + "login").permitAll()
                // Adding SNIPPETS policy
                .antMatchers(HttpMethod.GET, API_PREFIX + "snippets/flagged").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/flag").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "snippets/*/flag").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "snippets/**").permitAll()
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "snippets/*/delete").hasRole("USER")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/restore").hasRole("USER")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/vote_positive").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "snippets/*/vote_positive").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/vote_negative").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "snippets/*/vote_negative").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/fav").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "snippets/*/fav").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/report").hasRole("USER")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "snippets/*/report/dismiss").hasRole("USER")
                .antMatchers(HttpMethod.POST, API_PREFIX + "snippets").hasRole("USER")
                // Adding default policy, must be authenticated
                .antMatchers(API_PREFIX + "/**").authenticated();

        http
                .addFilterBefore(new JwtLoginProcessingFilter(API_PREFIX + "auth/login", this.successHandler, this.failureHandler, this.objectMapper, this.authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRefreshProcessingFilter(API_PREFIX + "auth/refresh", this.successHandler, this.failureHandler, this.objectMapper, this.authenticationManager(), this.tokenExtractor, this.userDetails), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenAuthenticationProcessingFilter(this.tokenExtractor, this.userDetails), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new PlainTextBasicAuthenticationEntryPoint());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/static/**",
                "/assets/**",
                "/asset-manifest.json",
                "/favicon.ico",
                "/index.html",
                "/manifest.json",
                "/service-worker.js",
                "/precache**"
        );
    }

    /**
     * Bean created based on https://stackoverflow.com/questions/51986766/spring-security-getauthenticationmanager-returns-null-within-custom-filter/51988966
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }
}
