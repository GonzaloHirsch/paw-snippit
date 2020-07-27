package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtLoginProcessingFilter;
import ar.edu.itba.paw.webapp.auth.JwtTokenHandlerService;
import ar.edu.itba.paw.webapp.auth.RefererRedirectionAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan({ "ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    // Api prefix for all requests
    private static final String API_PREFIX = "/api/v1/";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetails;
    @Autowired
    private JwtTokenHandlerService tokenHandlerService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // CSRF is not needed
        http.csrf().disable();

        // Setting the session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Setting authorized requests
        http.authorizeRequests()
                // Adding USERS controller authorizations
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/*/deleted_snippets").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, API_PREFIX + "users/*/profile_photo").hasRole("USER")
                .antMatchers(HttpMethod.POST, API_PREFIX + "users/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, API_PREFIX + "users/**").permitAll()
                // Adding TAGS controller authorizations
                .antMatchers(HttpMethod.POST, API_PREFIX + "tags/*/").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, API_PREFIX + "tags/*/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_PREFIX + "tags/**").permitAll()
                // Adding default policy, must be authenticated
                .antMatchers(API_PREFIX + "/**").authenticated();

        http
                .addFilterBefore(new JwtLoginProcessingFilter(new AntPathRequestMatcher(API_PREFIX + "login"), this.tokenHandlerService, this.userDetails), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore()
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/")
            .and().authorizeRequests()
//                .antMatchers("/goodbye", "/login", "/login_error", "/signup").anonymous()
//                .antMatchers("/recover-password", "/reset-password").anonymous()
//                .antMatchers("/verify-email", "/resend-email-verification").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/admin/add").hasRole("ADMIN")
//                .antMatchers("/flagged/**", "/snippet/**/flag").hasRole("ADMIN")
//                .antMatchers("/favorites/**", "/following/**", "/upvoted/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/snippet/**/vote/positive", "/snippet/**/vote/negative", "/snippet/**/fav").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/snippet/create", "/snippet/**/delete", "/snippet/**/report", "/snippet/**/report/dismiss"). hasRole("USER")
//                .antMatchers("/user/**/active", "/user/**/deleted", "user/**/active/edit", "user/**/deleted/edit"). hasRole("USER")
//                .antMatchers("/tags/**/follow").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/tags/**/delete", "/languages/**/delete").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
            .and().formLogin()
                .loginPage("/login")
                .failureUrl("/login_error")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new RefererRedirectionAuthenticationSuccessHandler())
                //.defaultSuccessUrl("/", false)
            .and().rememberMe()
                .rememberMeParameter("rememberme")
                .userDetailsService(userDetails)
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .key(getRememberMeKey())
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/goodbye")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/src/main/resources/css/**", "/src/main/resources/js/**", "/src/main/resources/img/**", "/favicon.ico", "/403");
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private String getRememberMeKey() {
        ClassPathResource keyResource = new ClassPathResource("authKey.key");
        try {
            return StreamUtils.copyToString(keyResource.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Remember me key threw IOException");
        }
    }

}
