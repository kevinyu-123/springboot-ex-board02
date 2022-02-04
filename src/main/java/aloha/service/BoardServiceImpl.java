package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Board;
import aloha.domain.BoardAttach;
import aloha.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService  {

	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<Board> list() throws Exception {
		// TODO Auto-generated method stub
		return mapper.list();
	}

	@Override
	public void register(Board board) throws Exception {
		mapper.create(board);
	}

	@Override
	public Board read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public void modify(Board board) throws Exception {
		mapper.update(board);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
	}

	@Override
	public List<Board> search(String keyword) throws Exception {
		return mapper.search(keyword);
	}

	@Override
	public void fileUpload(BoardAttach attach) throws Exception {
		mapper.fileUpload(attach);
	}

	@Override
	public List<BoardAttach> readFile(Integer boardNo) throws Exception {
		return mapper.readFile(boardNo);
	}

	@Override
	public void fileDelete(Integer fileNo) throws Exception {
		mapper.fileDelete(fileNo);
	}

	@Override
	public void fileUploadModify(BoardAttach attach) throws Exception {
		mapper.fileUploadUpdate(attach);
	}
	

}
