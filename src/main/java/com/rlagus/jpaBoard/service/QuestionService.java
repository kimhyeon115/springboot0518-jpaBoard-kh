package com.rlagus.jpaBoard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlagus.jpaBoard.entity.Question;
import com.rlagus.jpaBoard.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;
	
	public List<Question> getQuestionList() {
		
		List<Question> questionList = questionRepository.findAll();
		
		return questionList;
	}
}
