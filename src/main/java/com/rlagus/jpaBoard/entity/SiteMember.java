package com.rlagus.jpaBoard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
		name = "SITEMEMBER_SEQ_GENERATOR",
		sequenceName = "sitemember_seq",
		initialValue = 1,
		allocationSize = 1		
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteMember {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SITEMEMBER_SEQ_GENERATOR")
	private Integer id;			// 회원번호(기본키)
	
	@Column(unique = true)		// 기본키는 아니지만 유니크 속성 부여(중복값 X)
	private String userid;		// 아이디
	
	private String userpw;		// 비밀번호
	
	private String email;		// 이메일
}
