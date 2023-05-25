package com.rlagus.jpaBoard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlagus.jpaBoard.entity.SiteMember;

public interface SiteMemberRepository extends JpaRepository<SiteMember, Integer>{

	public Optional<SiteMember> findByUsername(String username);	// null값이 존재할수 있음으로 Optional로 받아야함(아주 중요)
}
