package aloha.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import aloha.domain.Board;
import aloha.domain.BoardAttach;
import aloha.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Value("${upload.path}")
	private String uploadPath;
	
	
	@Autowired
	private BoardService service;
	
	//게시글 목록 조회
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public void list(Model model) throws Exception{
		
		List<Board> list = service.list();
		for (Board board : list) {
			System.out.println(board);
		}
		model.addAttribute("list", service.list());
		//service 의 리스트를 호출.
		// boardservice impl 의 list가 실행됨.
	
	}
	
	//게시글 쓰기 화면
	@RequestMapping(value="register", method=RequestMethod.GET)
	public void registerForm(Model model, Board board) throws Exception{
		
	}
	
	
	//게시글 쓰기 처리 화면
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(Model model, Board board) throws Exception{
		
		MultipartFile[] files = board.getFile();
		
		// file 정보 확인
		log.info( files.length + "개의 파일이 전송됨..." );
		for (MultipartFile file : files) {
			log.info("originalName : " + file.getOriginalFilename());
			log.info("size : " + file.getSize());
			log.info("contentType " + file.getContentType());
		}
		
		// 파일 업로드 처리 - uploadFiles()
		ArrayList<BoardAttach> attachList = uploadFiles( files );
		
		// 글쓰기 등록
		service.register(board);
		
		// 첨부파일 경로 등록
		for (BoardAttach attach : attachList) {
			service.fileUpload(attach);
		}
		
		model.addAttribute("msg","등록이 완료되었습니다");
		return "/board/success";
	}
	
	// 게시글 읽기 화면
	@RequestMapping(value="read", method=RequestMethod.GET)
	public void read(Model model, Integer boardNo) throws Exception {
		model.addAttribute( service.read(boardNo) );
		
		model.addAttribute("files", service.readFile(boardNo));
		
	}
	
	// 게시글 수정 화면
	@RequestMapping(value="modify", method=RequestMethod.GET)
	public void modifyForm(Model model, Integer boardNo ) throws Exception {
		model.addAttribute( service.read(boardNo) );
		model.addAttribute("files", service.readFile(boardNo));
	}
	
	
	// 게시글 수정 처리
	@RequestMapping(value="modify", method=RequestMethod.POST)
	public String modify(Model model, Board board, Integer[] fileNoList ) throws Exception {
		
		// 동기식 삭제 파일번호
		for (Integer fileNo : fileNoList) {
			log.info("삭제할 파일번호 : " + fileNo);
			service.fileDelete(fileNo);
		}
		
		MultipartFile[] files = board.getFile();
		
		// file 정보 확인
		log.info( files.length + "개의 파일이 전송됨..." );
		for (MultipartFile file : files) {
			log.info("originalName : " + file.getOriginalFilename());
			log.info("size : " + file.getSize());
			log.info("contentType " + file.getContentType());
		}
		
		// 파일 업로드 처리 - uploadFiles()
		ArrayList<BoardAttach> attachList = uploadFiles( files );
		
		// 첨부파일 경로 등록
		for (BoardAttach attach : attachList) {
			attach.setBoardNo(board.getBoardNo());
			service.fileUploadModify(attach);
		}
		
		service.modify(board);
		
		
		model.addAttribute("msg","수정이 완료되었습니다");
		return "/board/success";
	}
	
	
	// 게시글 삭제 처리
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public String remove(Model model, Integer boardNo) throws Exception {
		
		List<BoardAttach> attachList = service.readFile(boardNo);
		
		// 실제 파일 삭제
		for (BoardAttach attach : attachList) {
			String fullName = attach.getFullName();
			//실제 파일 삭제
			File file = new File(fullName);
			if(file.exists()) {
				if(file.delete()) {
					log.info("파일 삭제 성공");
					log.info("file : "+fullName);
				}else {
					log.info("파일 삭제 실패");
				}
			}else {
				log.info("파일 삭제 실패");
				log.info("파일이 존재하지 않습니다");
				log.info("file : "+fullName);
			}
		}
		
		service.remove(boardNo);
		
		
		model.addAttribute("msg", "삭제를 완료하였습니다.");
		
		return "board/success";
	}
	
	// 게시글 목록에서 삭제 처리
	@RequestMapping(value = "removeList", method = RequestMethod.POST)
	public String removeList(Model model, Integer boardNo) throws Exception {
		List<BoardAttach> attachList = service.readFile(boardNo);
		
		// 실제 파일 삭제
		for (BoardAttach attach : attachList) {
			String fullName = attach.getFullName();
			//실제 파일 삭제
			File file = new File(fullName);
			if(file.exists()) {
				if(file.delete()) {
					log.info("파일 삭제 성공");
					log.info("file : "+fullName);
				}else {
					log.info("파일 삭제 실패");
				}
			}else {
				log.info("파일 삭제 실패");
				log.info("파일이 존재하지 않습니다");
				log.info("file : "+fullName);
			}
		}
		
		
		service.remove(boardNo);
		model.addAttribute("list", service.list());
		
		return "board/list";
	}
	
	// 게시글 목록 검색
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model, String keyword) throws Exception {
		
		model.addAttribute("list", service.search(keyword));
		
		return "board/list";
	}
	
		
	// 파일 업로드
	public ArrayList<BoardAttach> uploadFiles(MultipartFile[] files) throws IOException {
		
		ArrayList<BoardAttach> attachList = new ArrayList<BoardAttach>();
		
		// UUID
		
		for (MultipartFile file : files) {
			if(file.isEmpty())
				continue;
			
			UUID uid = UUID.randomUUID();
			
			String originalName = file.getOriginalFilename();
			// [UID]_이미지.jpg
			String uploadFileName = uid.toString() + "_" + originalName;
			
			File target = new File(uploadPath, uploadFileName);
			byte[] fileData = file.getBytes();
			FileCopyUtils.copy(fileData, target);
			
			BoardAttach attach = new BoardAttach();
			attach.setFullName(uploadPath + "/" + uploadFileName); 	// full_name
			attach.setFileName(originalName);						// file_name
			
			attachList.add(attach);
		}
		return attachList;
	}
	
		
}























