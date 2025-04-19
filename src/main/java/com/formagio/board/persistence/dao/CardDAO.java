package com.formagio.board.persistence.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.formagio.board.dto.CardDetailsDTO;
import com.formagio.board.persistence.entity.CardEntity;

import static com.formagio.board.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

public class CardDAO {

    private Connection conn;
    
    

    public CardDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) values (?, ?, ?);";
        try(var statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getBoardColumn().getId());
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long lastId = generatedKeys.getLong(1);
                        entity.setId(lastId);
                    }
                }
            }
            return entity;    
        }
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException{
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try(var statement = conn.prepareStatement(sql)){
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                               FROM BLOCKS sub_b
                              WHERE sub_b.card_id = c.id) blocks_amount
                  FROM CARDS c
                  LEFT JOIN BLOCKS b
                    ON c.id = b.card_id
                   AND b.unblocked_at IS NULL
                 INNER JOIN BOARDS_COLUMNS bc
                    ON bc.id = c.board_column_id
                  WHERE c.id = ?;
                """;
        try(var statement = conn.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                var dto = new CardDetailsDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        nonNull(resultSet.getString("block_reason")),
                        toOffsetDateTime(resultSet.getTimestamp("blocked_at")),
                        resultSet.getString("block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("board_column_id"),
                        resultSet.getString("name")
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

}