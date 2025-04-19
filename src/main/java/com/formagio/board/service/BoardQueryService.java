package com.formagio.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.formagio.board.dto.BoardDetailsDTO;
import com.formagio.board.persistence.dao.BoardColumnDao;
import com.formagio.board.persistence.dao.BoardDao;
import com.formagio.board.persistence.entity.BoardEntity;

public class BoardQueryService {
	private Connection conn;

	public BoardQueryService(Connection conn) {
		super();
		this.conn = conn;
	}
	
	public Optional<BoardEntity> findById(int id) throws SQLException{
		BoardDao dao = new BoardDao(conn);
		BoardColumnDao bcDao = new BoardColumnDao(conn);
		Optional<BoardEntity> board = dao.findById(id);
		if(board.isPresent()) {
			BoardEntity be = board.get();
			be.setBoardColumn(bcDao.findByBoardId(be.getId()));
			return Optional.of(be);
		}else {
			return board;
		}
	}
	
    public Optional<BoardDetailsDTO> showBoardDetails(int id) throws SQLException {
        var dao = new BoardDao(conn);
        var boardColumnDAO = new BoardColumnDao(conn);
        var optional = dao.findById(id);
        if (optional.isPresent()){
            var entity = optional.get();
            var columns = boardColumnDAO.findByBoardIdWithDetails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }
        return Optional.empty();
    }


}
