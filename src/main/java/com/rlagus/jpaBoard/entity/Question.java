package com.rlagus.jpaBoard.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
		name = "QUESTION_SEQ_GENERATOR",
		sequenceName = "question_seq",
		initialValue = 1,
		allocationSize = 1		
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GENERATOR")
	private Integer id;					// 질문 게시판 번호(기본키)<int 용량이 부족하다>
	
	@Column(length = 100)
	private String subject;				// 질문 게시판 제목
	
	@Column(length = 1000)
	private String content;				// 질문 게시판 내용
	
	private LocalDateTime createDate;	// 글 등록일시
	
	private LocalDateTime modifyDate;	// 글 수정 일시
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	@ManyToOne
	private SiteMember writer;
	
	@ManyToMany
	// 다 대 다 관계가 되면 question_liker 이름의 테이블 자동으로 생성됨
	private Set<SiteMember> liker;		// 좋아요 를 클릭한 사람들의 아이디 sitemember 객체들
	// 중복 방지를 위해 List(컬렉션)가 아닌 Set(컬렉션)으로 자료구조 설정(좋아요는 한 질문에 아이디 당 한번만 가능)
}
