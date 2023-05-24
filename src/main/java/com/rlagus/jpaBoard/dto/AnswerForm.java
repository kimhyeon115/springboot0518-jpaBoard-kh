package com.rlagus.jpaBoard.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AnswerForm {

	@NotEmpty(message = "답변은 공란으로 등록할 수 없습니다.")
	@Size(min = 10, message = "질문 내용은 10자 이상만 입력 가능합니다.")
	private String content;
}
