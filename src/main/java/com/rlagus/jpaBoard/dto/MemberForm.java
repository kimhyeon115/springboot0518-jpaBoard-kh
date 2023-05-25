package com.rlagus.jpaBoard.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MemberForm {

	@NotEmpty(message = "아이디는 필수 입력사항 입니다.")
	@Size(min = 4, message = "아이디는 4자 이상이어야 합니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 필수 입력사항 입니다.")
	@Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
	private String userpw1;
	
	@NotEmpty(message = "비밀번호는 필수 입력사항 입니다.")
	@Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
	private String userpw2;
	
	@NotEmpty(message = "이메일은 필수 입력사항 입니다.")
	private String email;
}
