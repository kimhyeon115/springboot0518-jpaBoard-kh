package com.rlagus.jpaBoard.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.rlagus.jpaBoard.dto.AnswerForm;
import com.rlagus.jpaBoard.dto.MemberForm;
import com.rlagus.jpaBoard.dto.QuestionForm;
import com.rlagus.jpaBoard.entity.Question;
import com.rlagus.jpaBoard.repository.QuestionRepository;
import com.rlagus.jpaBoard.repository.SiteMemberRepository;
import com.rlagus.jpaBoard.service.AnswerService;
import com.rlagus.jpaBoard.service.MemberService;
import com.rlagus.jpaBoard.service.QuestionService;

import oracle.net.aso.m;

@Controller
public class BoardController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/")
	public String home() {
		return "redirect:questionList";
	}
	
	@RequestMapping(value = "/index")
	public String index() {
		return "redirect:questionList";
	}
	
	@RequestMapping(value = "/question_form")
	public String question_form() {
		return "question_form";
	}
	
	@PostMapping(value = "/questionCreate")	// post 방식만 받음
	public String create(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {	// 에러가 발생하면 TRUE
			return "question_form";
		} else {
			questionService.questionCreate(questionForm.getSubject(),questionForm.getContent());
		}
		
		return "redirect:questionList";
	}
	
	@GetMapping(value = "/questionCreate")
	public String questionCreate(QuestionForm questionForm) {
		
		return "question_form";
	}
	
	@RequestMapping(value = "/questionList")
	public String questionList(Model model) {
		
//		List<Question> questionList = questionRepository.findAll();
		//SELECT * FROM question
		
		List<Question> questionList = questionService.getQuestionList();
		
		model.addAttribute("questionList", questionList);
		
		return "question_list";
	}
	
	@RequestMapping(value = "/questionContentView/{id}")
	public String questionView(@PathVariable("id") Integer id, Model model, AnswerForm answerForm) {
		
		Question question = questionService.getQuestion(id);
		
		model.addAttribute("question",question);
		
		return "question_view";
	}
	
	@RequestMapping(value = "/answerCreate/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
		
		Question question = questionService.getQuestion(id);
		
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("question", question);
			
			return "question_view";
		}
		
		answerService.answerCreate(answerForm.getContent(), question);
		
		return String.format("redirect:/questionContentView/%s",id);
	}
	
	@GetMapping(value = "/memberJoin")
	public String memberJoinForm(MemberForm memberForm) {

		return "member_join";
	}
	
	@PostMapping(value = "/memberJoin")
	public String memberJoin(@Valid MemberForm memberForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "member_join";
		}
		
		if(!memberForm.getUserpw1().equals(memberForm.getUserpw2())) { // pw1,pw2 불일치면
			bindingResult.rejectValue("userpw2", "passwordCheckInCorrect","입력하신 비밀번호가 일치하지 않습니다");
			// 인위적으로 에러 타입과 메세지 설정(추가하기)
			return "member_join";
		}
		
		memberService.memberJoin(memberForm.getUserid(), memberForm.getUserpw1(), memberForm.getEmail());
		
		return "redirect:index";
	}
}
