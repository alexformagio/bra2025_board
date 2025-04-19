package com.formagio.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.formagio.board.dto.CardDetailsDTO;
import com.formagio.board.persistence.dao.CardDAO;

public class CardQueryService {

    private final Connection conn;
    
    

    public CardQueryService(Connection conn) {
		super();
		this.conn = conn;
	}



	public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(conn);
        return dao.findById(id);
    }

}