package com.mysite.board3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.board3.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	
	Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

}
