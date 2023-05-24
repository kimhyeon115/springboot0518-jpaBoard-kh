package com.rlagus.jpaBoard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rlagus.jpaBoard.entity.SiteMember;
import com.rlagus.jpaBoard.repository.SiteMemberRepository;

@Service
public class MemberService {

	@Autowired
	private SiteMemberRepository siteMemberRepository;
	
	public SiteMember memberJoin(String userid, String userpw, String email) {

		SiteMember siteMember = new SiteMember();
		siteMember.setUserid(userid);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();	// 암호화 객체
		siteMember.setUserpw(passwordEncoder.encode(userpw));
		// 입력한 암호를 hash() 함수로 암호화하여 암호문을 DB에 저장
		siteMember.setEmail(email);
		
		siteMemberRepository.save(siteMember);
		
		return siteMember;
	}
}
