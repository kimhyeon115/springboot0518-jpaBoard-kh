package com.rlagus.jpaBoard.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rlagus.jpaBoard.entity.SiteMember;
import com.rlagus.jpaBoard.repository.SiteMemberRepository;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private SiteMemberRepository siteMemberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<SiteMember> optSiteMember = siteMemberRepository.findByUsername(username);
		
		if(optSiteMember.isEmpty()) {	// 참이면 아이디 조회 실패
			throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");			
		}
		
		SiteMember siteMember = optSiteMember.get();

		// admin, user 권한설정 관련
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if(username.equals("admin")) {	// 아이디가 admin이면 admin 권한 / 나머지 모두 user 권한 부여
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		
		return new User(siteMember.getUsername(), siteMember.getUserpw(), authorities);
	}

}
