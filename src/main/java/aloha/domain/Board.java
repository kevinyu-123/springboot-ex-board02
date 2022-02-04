package aloha.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/*
 * DTO ( Data transfer object)
 */
@Data
public class Board {
	
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private MultipartFile[] file;
	private String[] filePath;

}
