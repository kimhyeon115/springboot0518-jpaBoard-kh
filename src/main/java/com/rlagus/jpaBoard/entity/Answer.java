package com.rlagus.jpaBoard.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
		name = "ANSWER_SEQ_GENERATOR",
		sequenceName = "answer_seq",
		initialValue = 1,
		allocationSize = 1		
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWER_SEQ_GENERATOR")
	private Integer id;						// 답변 게시판 번호
	
	@Column(length = 1000)
	private String content;					// 답변 게시판 내용
	
	private LocalDateTime createDate;		// 답변 등록일시
	
	private LocalDateTime modifyDate;		// 답변 수정일시
	
	@ManyToOne
	// n:1(다 대 1) 답변 여러개가 1개의 질문에 달리는 구조
	private Question question;
	
	@ManyToOne
	private SiteMember writer;				// 글쓴이
	
	@ManyToMany
	// 다 대 다 관계가 되면 answer_liker 이름의 테이블 자동으로 생성됨
	private Set<SiteMember> liker;//좋아요를 누른 사람의 아이디
	//중복방지를 위해서 set 컬렉션으로 자료구조를 설정(좋아요는 한 답변에 아이디당 1번의 좋아요만 가능)
}
