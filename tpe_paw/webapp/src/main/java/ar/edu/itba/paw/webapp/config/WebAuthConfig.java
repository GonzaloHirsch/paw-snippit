package ar.edu.itba.paw.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/login") // donde redirigir el usuario ante una session invalida
            .and().authorizeRequests()       // como vamos a autorizar o no las solicitudes de HTTP --> que roles debe tener para poder hacer la solicitud
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/login", "/create").hasAnyRole()
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
}
