package com.formagio.board.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.formagio.board.persistence.entity.BoardColumnKindEnum.CANCEL;
import static com.formagio.board.persistence.entity.BoardColumnKindEnum.INITIAL;

public class BoardEntity {
	private Long id;
	private String name;
	private List<BoardColumnEntity> boardColumn = new ArrayList<>();
	
    public BoardColumnEntity getInitialColumn(){
        return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
    }

    public BoardColumnEntity getCancelColumn(){
        return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
    }

    private BoardColumnEntity getFilteredColumn(Predicate<BoardColumnEntity> filter){
        return boardColumn.stream()
                .filter(filter)
                .findFirst().orElseThrow();
    }

	
	public BoardEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoardEntity(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	

	public List<BoardColumnEntity> getBoardColumns() {
		return boardColumn;
	}

	public void setBoardColumn(List<BoardColumnEntity> boardColumn) {
		this.boardColumn = boardColumn;
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
		BoardEntity other = (BoardEntity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BoardEntity [id=" + id + ", name=" + name + "]";
	}
	

	
}
