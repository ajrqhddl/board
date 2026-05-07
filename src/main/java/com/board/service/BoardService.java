package com.board.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.board.entity.Board;
import com.board.repository.BoardRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	
	public Page<Board> getAllBoard(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	public Board getBoard(Long id) {
		Board board = boardRepository.findById(id).orElse(null);
		// 수정: 조회수 증가
		if (board != null) {
			board.setViewCount(board.getViewCount() + 1);
			boardRepository.save(board);
		}
		return board;
	}
	
	public Board saveBoard(Board board) {
		return boardRepository.save(board);
	}
	
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}

}
