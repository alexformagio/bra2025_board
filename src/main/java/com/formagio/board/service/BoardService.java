package com.formagio.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.formagio.board.persistence.dao.BoardColumnDao;
import com.formagio.board.persistence.dao.BoardDao;
import com.formagio.board.persistence.entity.BoardColumnEntity;
import com.formagio.board.persistence.entity.BoardEntity;

public class BoardService {
	private Connection conn;

	public BoardService(Connection conn) {
		super();
		this.conn = conn;
	}
	
	
	public BoardEntity insert(BoardEntity board) throws SQLException {
		BoardDao dao = new BoardDao(conn);
		BoardColumnDao boardColumnDAO = new BoardColumnDao(conn);
		try{
			BoardEntity b= dao.insert(board);
			//System.out.println("geted id -> " + b.getId());
			List<BoardColumnEntity> columns = b.getBoardColumns(). stream().map(c -> {
				c.setBoard(b);
				return c;
				}).toList();
				for (BoardColumnEntity column : columns){
				  boardColumnDAO.insert(column);
				}  
			this.conn.commit();
			return b;
		} catch (SQLException e) {
			this.conn.rollback();
			throw e;
		}
	}		

	public boolean delete(int id) throws SQLException {
		BoardDao dao = new BoardDao(conn);
		
		try{
			if (!dao.exists(id)) {
				return false;
			}

			BoardColumnDao bcDao = new BoardColumnDao(conn);
			bcDao.deleteByfk(id);
			dao.delete(id);
			this.conn.commit();
			return true;
		} catch (SQLException e) {
			this.conn.rollback();
			throw e;
		}
	}	
}
