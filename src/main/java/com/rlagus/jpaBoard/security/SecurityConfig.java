package com.rlagus.jpaBoard.security;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration				// 해당 클래스가 스프링 환경설정임을 알림
@EnableWebSecurity			// 모든 웹에 대한 요청이 스프링 시큐리티의 컨트롤 하에 있음을 알림
@EnableMethodSecurity(prePostEnabled = true)	// @PreAuthorize 애너테이션을 동작할수 있도록 함
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers(
			new AntPathRequestMatcher("/**")).permitAll()	// 모든 접근을 허용하게 한다

			// 로그인과 로그아웃 관련설정
			.and()	// 로그인 설정
				.formLogin()
				.loginPage("/login")	// 로그인 페이지가 보이게하는 요청(기본 파라미터 값이름 username, password)
//				.usernameParameter("userid")	// 기본 파라미터 값이름을 새로 정함
				.passwordParameter("userpw")	// 기본 파라미터 값이름을 새로 정함
				.defaultSuccessUrl("/index")	// 로그인 성공시 이동할 페이지 요청
			.and()	// 로그아웃 설정
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))	// 로그아웃 요청
				.logoutSuccessUrl("/index")	// 로그아웃 성공시 이동할 페이지 요청
				.invalidateHttpSession(true)	// 세션삭제 -> 로그아웃		
			;

		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
	throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
