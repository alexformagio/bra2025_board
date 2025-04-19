package com.formagio.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.formagio.board.dto.BoardColumnDTO;
import com.formagio.board.persistence.entity.BoardColumnEntity;
import com.formagio.board.persistence.entity.BoardColumnKindEnum;
import com.formagio.board.persistence.entity.CardEntity;

import static java.util.Objects.isNull;

public class BoardColumnDao {
	private Connection conn;

	public BoardColumnDao(Connection conn) {
		super();
		this.conn = conn;
	}

	public BoardColumnEntity insert(BoardColumnEntity bc) throws SQLException{
		String sql = "insert into BOARDS_COLUMNS( name, order_board, kind, board_id) values(?,?,?,?);";
		try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			statement.setString(1, bc.getName());
			statement.setInt(2, bc.getOrder());
			statement.setString(3, bc.getKind().name());
			statement.setLong(4, bc.getBoard().getId());
			int rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						long lastId = generatedKeys.getLong(1);
						bc.setId(lastId);
					}
				}        
			}
			return bc;
		}
	}

	public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException{
		List<BoardColumnEntity> entities = new ArrayList<>();
		String sql = "SELECT id, name, order_board, kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY order_board;";
		try(PreparedStatement statement = conn.prepareStatement(sql)){
			statement.setLong( 1, id);
			statement.executeQuery();
			ResultSet rs = statement.getResultSet();
			while (rs.next()){
				BoardColumnEntity entity = new BoardColumnEntity();
				entity.setId(rs.getLong("id"));
				entity.setName(rs.getString("name"));
				entity.setOrder(rs.getInt("order_board"));
				entity.setKind(findByName(rs.getString("kind")));
				entities.add(entity);
			}
			return entities;
		}
	}

	private BoardColumnKindEnum findByName(String value) {
		return BoardColumnKindEnum.findByName(value);
	}

	public void deleteByfk(int id) throws SQLException {
		String sql = "delete from boards_columns where board_id=?;";
		try(PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		
	}
	
    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long boardId) throws SQLException {
        List<BoardColumnDTO> dtos = new ArrayList<>();
        String sql =
                """
                SELECT bc.id,
                       bc.name,
                       bc.kind,
                       (SELECT COUNT(c.id)
                               FROM CARDS c
                              WHERE c.board_column_id = bc.id) cards_amount
                  FROM BOARDS_COLUMNS bc
                 WHERE board_id = ?
                 ORDER BY order_board;
                """;
        try(var statement = conn.prepareStatement(sql)){
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var dto = new BoardColumnDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        findByName(resultSet.getString("kind")),
                        resultSet.getInt("cards_amount")
                );
                dtos.add(dto);
            }
            return dtos;
        }
    }

    public Optional<BoardColumnEntity> findById(final Long boardId) throws SQLException{
        var sql =
        """
        SELECT bc.name,
               bc.kind,
               c.id,
               c.title,
               c.description
          FROM BOARDS_COLUMNS bc
          LEFT JOIN CARDS c
            ON c.board_column_id = bc.id
         WHERE bc.id = ?;
        """;
        try(var statement = conn.prepareStatement(sql)){
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                var entity = new BoardColumnEntity();
                entity.setName(resultSet.getString("name"));
                entity.setKind(findByName(resultSet.getString("kind")));
                do {
                    var card = new CardEntity();
                    if (isNull(resultSet.getString("title"))){
                        break;
                    }
                    card.setId(resultSet.getLong("id"));
                    card.setTitle(resultSet.getString("title"));
                    card.setDescription(resultSet.getString("description"));
                    entity.getCards().add(card);
                }while (resultSet.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }


}
