package com.rlagus.jpaBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlagus.jpaBoard.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

}
