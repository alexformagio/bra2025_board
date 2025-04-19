package com.formagio.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.formagio.board.persistence.entity.BoardEntity;

public class BoardDao {
	private Connection conn;

	public BoardDao(Connection conn) {
		super();
		this.conn = conn;
	}
	
	public BoardEntity insert(BoardEntity board) throws SQLException {
    	String sql = "insert into boards(name) values(?);";
		try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, board.getName());
			int rowsAffected = statement.executeUpdate();
            //System.out.println("Rows affected -> " + rowsAffected);
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long lastId = generatedKeys.getLong(1);
                        board.setId(lastId);
                    }
                }        
            }    
            return board;
		}     
	}
    
    public void delete(int id) throws SQLException {
    	String sql = "delete from boards where id = ?;";
		try(PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
				
	}
    
    public Optional<BoardEntity> findById(int id) throws SQLException {
    	String sql = "select * from boards where id = ?;";
		try(PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				BoardEntity board = new BoardEntity();
				board.setId(rs.getLong("id"));
				board.setName(rs.getString("name"));
				return Optional.of(board);
			}
			return Optional.empty();
		}
	}
    
    public boolean exists(int id) throws SQLException {
    	String sql = "select 1 from boards where id = ?;";
		try(PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeQuery();
			return statement.getResultSet().next();
		}		
	}
    
}
