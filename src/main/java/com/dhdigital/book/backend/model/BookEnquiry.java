package com.dhdigital.book.backend.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class BookEnquiry {

	@JsonProperty("isbn")
	private int isbn;

//	Parameterized Constructor

	public BookEnquiry(int isbn) {
		super();
		this.isbn = isbn;
	}

//	Default Constructor

	public BookEnquiry() {
	}

//	Getters and Setters

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

//	toString:

	@Override
	public String toString() {
		return "BookEnquiry [isbn=" + isbn + "]";
	}

}
