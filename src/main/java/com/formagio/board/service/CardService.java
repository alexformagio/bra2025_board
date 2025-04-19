package com.formagio.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.formagio.board.dto.BoardColumnInfoDTO;
import com.formagio.board.exceptions.CardBlockedException;
import com.formagio.board.exceptions.CardFinishedException;
import com.formagio.board.exceptions.EntityNotFoundException;
import com.formagio.board.persistence.dao.BlockDAO;
import com.formagio.board.persistence.dao.CardDAO;
import com.formagio.board.persistence.entity.CardEntity;

import static com.formagio.board.persistence.entity.BoardColumnKindEnum.CANCEL;
import static com.formagio.board.persistence.entity.BoardColumnKindEnum.FINAL;

public class CardService {

    private final Connection conn;

    public CardService(Connection conn) {
		super();
		this.conn = conn;
	}

	public CardEntity create(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(conn);
            dao.insert(entity);
            conn.commit();
            return entity;
        } catch (SQLException ex){
            conn.rollback();
            throw ex;
        }
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        try{
            var dao = new CardDAO(conn);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
            );
            if (dto.blocked()){
                var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
            if (currentColumn.kind().equals(FINAL)){
                throw new CardFinishedException("O card já foi finalizado");
            }
            var nextColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(nextColumn.id(), cardId);
            conn.commit();
        }catch (SQLException ex){
            conn.rollback();
            throw ex;
        }
    }

    public void cancel(final Long cardId, final Long cancelColumnId ,
                       final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        try{
            var dao = new CardDAO(conn);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
            );
            if (dto.blocked()){
                var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
            if (currentColumn.kind().equals(FINAL)){
                throw new CardFinishedException("O card já foi finalizado");
            }
            boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(cancelColumnId, cardId);
            conn.commit();
        }catch (SQLException ex){
            conn.rollback();
            throw ex;
        }
    }

    public void block(final Long id, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try{
            var dao = new CardDAO(conn);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
            );
            if (dto.blocked()){
                var message = "O card %s já está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow();
            if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL)){
                var message = "O card está em uma coluna do tipo %s e não pode ser bloqueado"
                        .formatted(currentColumn.kind());
                throw new IllegalStateException(message);
            }
            var blockDAO = new BlockDAO(conn);
            blockDAO.block(reason, id);
            conn.commit();
        }catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }

    public void unblock(final Long id, final String reason) throws SQLException {
        try{
            var dao = new CardDAO(conn);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
            );
            if (!dto.blocked()){
                var message = "O card %s não está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }
            var blockDAO = new BlockDAO(conn);
            blockDAO.unblock(reason, id);
            conn.commit();
        }catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }

}