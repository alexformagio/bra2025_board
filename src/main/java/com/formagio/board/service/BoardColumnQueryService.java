package com.formagio.board.service;

import com.formagio.board.persistence.dao.BoardColumnDao;
import com.formagio.board.persistence.entity.BoardColumnEntity;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class BoardColumnQueryService {

    private final Connection conn;
    
    

    public BoardColumnQueryService(Connection conn) {
		super();
		this.conn = conn;
	}



	public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDao(conn);
        return dao.findById(id);
    }

}