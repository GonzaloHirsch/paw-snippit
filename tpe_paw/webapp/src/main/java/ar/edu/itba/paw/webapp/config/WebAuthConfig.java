package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan({ "ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetails;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/login") // donde redirigir el usuario ante una session invalida
            .and().authorizeRequests()       // como vamos a autorizar o no las solicitudes de HTTP --> que roles debe tener para poder hacer la solicitud
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/login").hasAnyRole()
                .antMatchers("/post/vote").hasRole("VOTER")
                .antMatchers("/**").authenticated()     // definimos un patron que va a ser matcheado contra la url, interpretado como un pattern de Ant
            .and().formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
            .and().rememberMe()
                .rememberMeParameter("rememberme")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

}
