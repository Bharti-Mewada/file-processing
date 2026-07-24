package com.fileProcessing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserFile {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer fileId;

	private String fileName;

	private String fileType;
	private Long fileSize;
	private String response;
	private String status;

}
