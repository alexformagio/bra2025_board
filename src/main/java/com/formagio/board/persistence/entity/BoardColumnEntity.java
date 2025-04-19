package com.formagio.board.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardColumnEntity {
	private Long id;
	private String name;
	private int orderBoard;
	private BoardColumnKindEnum	kind;
	private BoardEntity board = new BoardEntity();
	private List<CardEntity> cards = new ArrayList<>();
	
	public BoardColumnEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BoardColumnEntity(Long id, String name, int order, BoardColumnKindEnum kind, BoardEntity board) {
		super();
		this.id = id;
		this.name = name;
		this.orderBoard = order;
		this.kind = kind;
		this.board = board;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return orderBoard;
	}

	public void setOrder(int order) {
		this.orderBoard = order;
	}

	public BoardColumnKindEnum getKind() {
		return kind;
	}

	public void setKind(BoardColumnKindEnum kind) {
		this.kind = kind;
	}

	
	public BoardEntity getBoard() {
		return board;
	}


	public void setBoard(BoardEntity board) {
		this.board = board;
	}
	
	


	public List<CardEntity> getCards() {
		return cards;
	}


	public void setCards(List<CardEntity> cards) {
		this.cards = cards;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardColumnEntity other = (BoardColumnEntity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BoardColumnEntity [id=" + id + ", name=" + name + ", order_board=" + orderBoard + ", kind=" + kind + "]";
	}
	

}
