package com.mysite.board3.service;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.board3.entity.Board;
import com.mysite.board3.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository; 

	//작성한 게시글을 레파지토리를 통해 DB에 저장 (글작성 처리)
	public void write (Board board, MultipartFile file) throws Exception {
		
		//경로지정
		String projectPath = System.getProperty("user.dir")+"//src//main//resources//static//files";
		UUID uuid = UUID.randomUUID();	//식별자 (파일의 이름름 붙일 랜덤이름 생성)
		String fileName = uuid + "_"+file.getOriginalFilename();		//파일 이름 붙이기
		
		File saveFile = new File(projectPath , fileName);		//파일을 담을 객체생성
		file.transferTo(saveFile);	
		
		//업로드한 파일을 DB에 저장
		board.setFilename(fileName);
		board.setFilepath("/files/"+fileName);
		
		boardRepository.save(board);
	}
	
	//DB에서 리스트에 글을 불러올 메소드 (게시글 리스트 처리) -> page 처리 http://localhost:8004/board/list?page=0&size=10
	public Page<Board> boardList (Pageable pageable) {
		return boardRepository.findAll(pageable);
		
	}
	
	
	//특정 게시글 불러오기
	public Board boardView(Long id) {
		
		return boardRepository.findById(id).get();
	}
	
	//특정게시글 삭제 
	public void boardDelete (Long id) {
		
		boardRepository.deleteById(id);
	}
	
	//게시물 검색기능
		public Page<Board> boardSearchList (String searchKeyWord , Pageable pageable) {
			
			return boardRepository.findByTitleContaining(searchKeyWord , pageable);
		}
	
}
