package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    //자동 로그인 위해 주입 필요
    private final DataSource dataSource;//쿠키와 관련된 정보를 테이블로 보관하도록 지정
    private final CustomUserDetailsService userDetailsService;//userdetailservice타입의 객체가 필요


    @Bean//PasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("----------configure----------");

        //커스텀 로그인 페이지
        http.formLogin().loginPage("/member/login");

        //CSRF 토큰 비활성화//보안상 좀 더 안전하나 기존의 post/put/delete방식 이용하려면 모든 코드 수정해야되니까 번거로우니까 여기서는 비활성화
        http.csrf().disable();

        //자동로그인
        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60*60*24*30);

        //403 에러 처리
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        //OAuth2 로그인 사용
        http.oauth2Login()
                .loginPage("/member/login")
                .successHandler(authenticationSuccessHandler());

        return http.build();
    }

    //403에러 났을 떄, Custom403Handler 클래스에서 403에러 처리하기 위함.
    // <form>방식으로 데이터가 처리되는 경우 로그인 페이지로 리다이렉트 됨.
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }


    @Bean//css나 js파일 같은 정적 파일들은 굳이 시큐리티 적용하지 않아도 됨.
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("---------------web configure---------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //자동로그인//remember-me 쿠키 생성 시, 쿠키의 값을 인코딩하기 위한 키값과 필요한 정보를 저장하는 tokenrepository 지정
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }





}
