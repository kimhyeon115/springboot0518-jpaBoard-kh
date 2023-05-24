package com.rlagus.jpaBoard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlagus.jpaBoard.entity.Question;
import com.rlagus.jpaBoard.exception.DataNotFoundException;
import com.rlagus.jpaBoard.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;
	
	public List<Question> getQuestionList() {
		
		List<Question> questionList = questionRepository.findAll();
		
		return questionList;
	}
	
	public Question getQuestion(Integer id) {
				
		Optional<Question> optquestion = questionRepository.findById(id);
		// 조회 데이터가 없을수도 있음으로 null값도 받을수 있는 Optional사용해야함
		
		if(optquestion.isPresent()) {
		// isPresent() 데이터가 존재 여부확인 메소드
			Question question = optquestion.get();
			return question;
		} else {
			throw new DataNotFoundException("선택하신 질문은 없는 글입니다.");
		}		
	}
	
	public void questionCreate(String subject, String content) {
		
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());//서버의 현재시간 입력
		
		questionRepository.save(question);
	}
}
