package com.board.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.board.entity.Board;
import com.board.service.BoardService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping
	public Page <Board> getAllBoard(Pageable pageable) {
		return boardService.getAllBoard(pageable);
	}
	
	@GetMapping("/{id}")
	public Board getBoard(@PathVariable Long id) {
		return boardService.getBoard(id);
	}
	
	@PostMapping
	public Board saveBoard(
	        @RequestParam String title,
	        @RequestParam String writer,
	        @RequestParam String content,
	        @RequestParam(required = false) MultipartFile file
	) throws Exception {

	    String fileName = null;

	    if (file != null && !file.isEmpty()) {
	        String uploadDir = System.getProperty("user.dir") + "/uploads/";
	        File dir = new File(uploadDir);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }
	        
	        fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	        file.transferTo(new File(uploadDir, fileName));
	    }

	    Board board = new Board();
	    board.setTitle(title);
	    board.setWriter(writer);
	    board.setContent(content);
	    board.setFileName(fileName);

	    return boardService.saveBoard(board);
	}
	
	@PutMapping("/{id}")
	public Board updateBoard(
	        @PathVariable Long id,
	        @RequestParam String title,
	        @RequestParam String writer,
	        @RequestParam String content,
	        @RequestParam(required = false) MultipartFile file
	) throws Exception {

	    Board existing = boardService.getBoard(id);

	    existing.setTitle(title);
	    existing.setWriter(writer);
	    existing.setContent(content);

	    if (file != null && !file.isEmpty()) {
	        String uploadDir = System.getProperty("user.dir") + "/uploads/";
	        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	        file.transferTo(new File(uploadDir, fileName));
	        existing.setFileName(fileName);
	    }

	    return boardService.saveBoard(existing);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBoard(@PathVariable Long id) {
		boardService.deleteBoard(id);
	}
	
}
