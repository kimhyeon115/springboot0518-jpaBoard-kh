package com.rlagus.jpaBoard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rlagus.jpaBoard.entity.SiteMember;
import com.rlagus.jpaBoard.repository.SiteMemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private SiteMemberRepository siteMemberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public SiteMember memberJoin(String username, String userpw, String email) {
		
		SiteMember siteMember = new SiteMember();
		siteMember.setUsername(username);
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		siteMember.setUserpw(passwordEncoder.encode(userpw));
		//유저가 입력한 암호를 hash함수로 암호화하여 암호문을 DB에 저장
		
		//siteMember.setUserid(userpw);
		siteMember.setEmail(email);
		
		siteMemberRepository.save(siteMember);
		
		return siteMember;		
	}
	
	public SiteMember getMember(String username) {
		
		Optional<SiteMember> optSiteMember = siteMemberRepository.findByUsername(username);
		
		SiteMember siteMember = optSiteMember.get();
		
		return siteMember;
	}
}