package com.rlagus.jpaBoard.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.rlagus.jpaBoard.dto.AnswerForm;
import com.rlagus.jpaBoard.dto.MemberForm;
import com.rlagus.jpaBoard.dto.QuestionForm;
import com.rlagus.jpaBoard.entity.Answer;
import com.rlagus.jpaBoard.entity.Question;
import com.rlagus.jpaBoard.entity.SiteMember;
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
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@PostMapping(value = "/questionCreate")//post만 받음
	public String create(Model model, @Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {//에러가 발생하면 참
			
			return "question_form";
		} 
		
		//principal.getName() -> 현재 로그인 중인 유저의 username 가져오기
		SiteMember siteMember = memberService.getMember(principal.getName());
		
		questionService.questionCreate(questionForm.getSubject(), questionForm.getContent(), siteMember);
		
				
		return "redirect:questionList";
	}
	
	@PreAuthorize("isAuthenticated()")
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
	public String quesitonView(@PathVariable("id") Integer id, Model model, AnswerForm answerForm) {
		
//		System.out.print(id);//질문리스트에서 유저가 클릭한 글의 번호
		
		Question question = questionService.getQuestion(id);
		
		model.addAttribute("question", question);
		
		return "question_view";
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@RequestMapping(value = "/answerCreate/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {		
		
		Question question = questionService.getQuestion(id);
		
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("question", question);
			
			return "question_view";
		}
		
		SiteMember siteMember = memberService.getMember(principal.getName());
		
		answerService.answerCreate(answerForm.getContent(), question, siteMember);
		
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
		
		if(!memberForm.getUserpw1().equals(memberForm.getUserpw2())) {//비밀번호 확인 실패
			bindingResult.rejectValue("userpw2", "passwordCheckInCorrect","비밀번호 확인란의 비밀번호가 일치하지 않습니다.");
			return "member_join";
		}		
		
		try {
			memberService.memberJoin(memberForm.getUsername(), memberForm.getUserpw1(), memberForm.getEmail());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();//콘솔창에 에러이유를 출력
			bindingResult.reject("idRegFail", "이미 등록된 아이디입니다.");
			return "member_join";
		} catch (Exception e) {
			e.printStackTrace();//콘솔창에 에러이유를 출력
//			bindingResult.reject("idRegFail", "아이디 등록 중 에러가 발생했습니다.");
			bindingResult.reject("idRegFail", e.getMessage());//해당 오류 메시지를 에러로 전송
			return "member_join";
		}
		
		
		return "redirect:index";
	}
	
	@GetMapping(value = "/login")
	public String login() {
		return "login_form";
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@GetMapping(value = "/questionModify/{id}")
	public String questionModify(@PathVariable("id") Integer id, Principal principal, QuestionForm questionForm) {
		
		Question question = questionService.getQuestion(id);//질문 글 번호로 검색해서 해당 객체 가져오기
		
		if(!question.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 질문에 대한 수정 권한이 없습니다.");
		}
		
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@PostMapping(value = "/questionModify/{id}")
	public String questionModifyOk(@PathVariable("id") Integer id, Principal principal, @Valid QuestionForm questionForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "question_form";			
		}
		
		Question question = questionService.getQuestion(id);
		
		if(!question.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 질문에 대한 수정 권한이 없습니다.");
		}
		
		questionService.questionModify(question, questionForm.getSubject(), questionForm.getContent());
		
				
		return String.format("redirect:/questionContentView/%s",id);	
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@GetMapping(value = "/answerModify/{id}")
	public String answerModify(@PathVariable("id") Integer id, Principal principal, AnswerForm answerForm) {
		
		Answer answer = answerService.getAnswer(id);
		
		if(!answer.getWriter().getUsername().equals(principal.getName())) {
			//해당 답변의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 답변에 대한 수정 권한이 없습니다.");
		}
		
		answerForm.setContent(answer.getContent());//answerForm에 기본 답변 글 내용 넣기
		
		return "answer_form";
	}
	
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@PostMapping(value = "/answerModify/{id}")
	public String answerModifyOk(@PathVariable("id") Integer id, Principal principal, @Valid AnswerForm answerForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "answer_form";			
		}
		
		Answer answer = answerService.getAnswer(id);
		
		if(!answer.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 답변에 대한 수정 권한이 없습니다.");
		}
		
		answerService.answerModify(answer, answerForm.getContent());
		
				
		return String.format("redirect:/questionContentView/%s", answer.getQuestion().getId());	
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@RequestMapping(value = "/questionDelete/{id}")
	public String questionDelete(@PathVariable("id") Integer id) {
		
		questionService.questionDelete(id);
		
		return "redirect:/index";
		
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@RequestMapping(value = "/answerDelete/{id}")
	public String answerDelete(@PathVariable("id") Integer id) {
		
		Answer answer = answerService.getAnswer(id);
		
		answerService.answerDelete(id);
		
		return String.format("redirect:/questionContentView/%s", answer.getQuestion().getId());	
		
	}
	
	@PreAuthorize("isAuthenticated()")//로그인이 안되어 있으면 login 페이지로 이동시킴
	@RequestMapping(value = "/questionLike/{id}")
	public String questionLike(@PathVariable("id") Integer id, Principal principal) {
		
		Question question = questionService.getQuestion(id);
		
		SiteMember siteMember = memberService.getMember(principal.getName());//현재 로그인 중인 유저의 정보(객체) 가져오기
		
		questionService.questionLike(question, siteMember);
		
		return String.format("redirect:/questionContentView/%s", id);	
		
	}
	
}