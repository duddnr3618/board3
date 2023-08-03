package com.mysite.board3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column (length = 100)
	private String title;
	
	@Column(length = 1000)
	private String contents;
	
	@Column(length = 50 )
	private String writer;
	
	private String filename;
	
	private String filepath;
	
	
	

	
}
