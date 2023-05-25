package com.rlagus.jpaBoard.security;

import lombok.Getter;

@Getter	// 상수 자료형이므로 setter는 필요없음
public enum UserRole {	// 열거형
	
	ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
	
	private UserRole(String value) {
		
		this.value = value;		
	}
	
	private String value;
}
