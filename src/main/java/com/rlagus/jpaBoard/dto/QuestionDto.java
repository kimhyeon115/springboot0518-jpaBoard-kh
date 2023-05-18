package com.rlagus.jpaBoard.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rlagus.jpaBoard.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

	private Integer id;
	private String subject;
	private String content;
	private LocalDateTime createDate;
	
	private List<Answer> answers;
}
