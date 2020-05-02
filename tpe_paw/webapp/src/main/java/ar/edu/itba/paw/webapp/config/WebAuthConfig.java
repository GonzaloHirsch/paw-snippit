package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan({ "ar.edu.itba.paw.webapp.auth"})
@PropertySource("classpath:authKey.properties")
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetails;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/")
            .and().authorizeRequests()
                .antMatchers("/goodbye", "/login", "/login_error", "/signup").anonymous()
                .antMatchers("/admin/add").hasRole("ADMIN")
                .antMatchers("/favorites/**", "/following/**", "/upvoted/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/snippet/create", "/snippet/**/vote", "/snippet/**/fav"). hasAnyRole("USER", "ADMIN")
                .antMatchers("/user/**/save-image"). hasRole("USER")
                .antMatchers("/tags/**/follow", "/tags/**/unfollow").hasAnyRole("USER", "ADMIN")
                .antMatchers("/languages/**/delete").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .failureUrl("/login_error")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
            .and().rememberMe()
                .rememberMeParameter("rememberme")
                .userDetailsService(userDetails)
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
                .key("${key}")
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

}
