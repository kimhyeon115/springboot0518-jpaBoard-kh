package com.rlagus.jpaBoard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "해당 데이터 없음")
public class DataNotFoundException extends RuntimeException{

	public DataNotFoundException(String message) {
		super(message);
	}
}
