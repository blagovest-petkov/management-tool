package com.example.managementtool.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()
            ?.withUser("user")?.password("{noop}12345678")?.roles("USER")
    }

    override fun configure(http: HttpSecurity?) {
        http
            ?.httpBasic()
            ?.and()
            ?.authorizeRequests()
            ?.antMatchers(HttpMethod.GET, "/employee")?.hasRole("USER")
            ?.antMatchers(HttpMethod.POST, "/employee")?.hasRole("USER")
            ?.and()
            ?.csrf()?.disable()
            ?.formLogin()?.disable();
    }

}