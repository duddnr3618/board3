package com.mysite.board3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.board3.entity.Board;
import com.mysite.board3.service.BoardService;

@Controller				//웹(뷰페이지)에 보내거나 받는 역할
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	@GetMapping("/board/wirte")					//localhost:8004/board/wirte
	//게시글 작성폼
	public String boardWriteForm () {
		
		return "boardwrite";
	}
	
	//웹단에서 작성한 게시글이 서버로 넘어오는 메소드 --> 작성 완료되면 메시지 알림 설정
	@PostMapping("/board/writepro")
	public String boardWritepro(Board board, Model model , MultipartFile file) throws Exception {		//Board 엔티티클래스를 만들면 board로 데이터를 받아줄수 있다.
	    try {
	        boardService.write(board , file);
	        model.addAttribute("message", "글 작성이 완료되었습니다.");		//글 작성 이후 알림을 보여준다.
	        model.addAttribute("searchUrl", "/board/list");
	    } catch (Exception e) {
	        model.addAttribute("message", "글 작성이 실패했습니다.");
	        model.addAttribute("searchUrl", "/board/write");		//알림 이후 리스트로 보내준다
	    }
	    return "message";
	}
	
	//리스트를 보여주는 메소드
	@GetMapping("/board/list")
	public String boardlist (Model model, 
			@PageableDefault(page=0 , size = 10 , sort = "id" ,direction = Direction.DESC)Pageable pageable,
			String searchKeyword
			) {
		
		Page<Board> list = null;
		
		if(searchKeyword == null) {
			list =  boardService.boardList(pageable);
		}else {
			list = boardService.boardSearchList(searchKeyword,pageable);
			
		}
		
		//페이지 리스트를 만들어서 넣어줌
		//Page<Board> list = boardService.boardList(pageable); -> 위에서 변수 선언
		//http://localhost:8004/board/list?searchKeyword=11
		int nowPage = list.getPageable().getPageNumber() + 1;	//현재 페이지를 가져오기
		int startPage = Math.max( nowPage - 4,1);
		int endPage = Math.min(nowPage + 5,list.getTotalPages());
		
		model.addAttribute("list",list);
		model.addAttribute("nowPage",nowPage);
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
	
		
		return "boardlist";
	}
	
	//상세페이지 보여주는 메소드
	@GetMapping("/board/view")		//http://localhost:8004/board/view?id=1
	public String boaordView(Model model , Long id) {
		
		model.addAttribute("board" , boardService.boardView(id));
		return "boardview";
	}
	
	//상세페이지 및 url에서 글 삭제 
	@GetMapping("/board/delete") 	//http://localhost:8004/board/delete?id=1
	public String boardDelete (Long id) {
		boardService.boardDelete(id);
		
		return "redirect:/board/list";
	}
	
	//게시글 수정페이지를 보여주는 폼
	@GetMapping("/board/modify/{id}")
	public String boardModify (@PathVariable("id") Long id ,Model model) {
	
		model.addAttribute("board" , boardService.boardView(id));
	
		return "boardmodify";
	}
	
	@PostMapping("/board/update/{id}")
	public String boardUpdate(@PathVariable("id") Long id, Board board , MultipartFile file) throws Exception {
	    Board boardTemp = boardService.boardView(id);
	    
	    boardTemp.setTitle(board.getTitle());
	    boardTemp.setContents(board.getContents());
	    boardTemp.setWriter(board.getWriter());
	    boardService.write(boardTemp , file); // 수정한 게시글 저장
	    return "redirect:/board/list"; // 리스트 페이지로 리다이렉트
	}
	
	
}
