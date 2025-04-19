package com.formagio.board.persistence.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static com.formagio.board.persistence.converter.OffsetDateTimeConverter.toTimestamp;

public class BlockDAO {

    private final Connection conn;

    
    public BlockDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public void block(final String reason, final Long cardId) throws SQLException {
        var sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id, unblock_reason) VALUES (?, ?, ?,?);";
        try(var statement = conn.prepareStatement(sql)){
            statement.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            statement.setString(2, reason);
            statement.setLong(3, cardId);
            statement.setString(4, "");
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException{
        var sql = "UPDATE BLOCKS SET unblocked_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason=?;";
        try(var statement = conn.prepareStatement(sql)){
            statement.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            statement.setString(2, reason);
            statement.setLong(3, cardId);
            statement.setString(4, "");
            statement.executeUpdate();
        }
    }

}