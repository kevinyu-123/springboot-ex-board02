package aloha.service;

import java.util.List;

import aloha.domain.Board;
import aloha.domain.BoardAttach;

public interface BoardService {

	// 게시글 목록 조회
	public List<Board> list() throws Exception;
	
	// 게시글 쓰기
	public void register(Board board) throws Exception;
	
	// 게시글 읽기
	public Board read(Integer boardNo) throws Exception;
	
	// 게시글 수정
	public void modify(Board board) throws Exception;
	
	// 게시글 삭제
	public void remove(Integer boardNo) throws Exception;
	
	// 게시글 검색
	public List<Board> search(String keyword) throws Exception;
	
	// 파일 업로드
	public void fileUpload(BoardAttach attach) throws Exception;
	
	// 파일 수정 업로드
	public void fileUploadModify(BoardAttach attach) throws Exception;
	
	// 파일 읽기
	public List<BoardAttach> readFile(Integer boardNo) throws Exception;
	
	//파일 삭제
	public void fileDelete(Integer fileNo) throws Exception;
	
}
