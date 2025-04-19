package com.formagio.board.persistence.entity;

public class CardEntity {
    private Long id;
    private String title;
    private String description;
    private BoardColumnEntity boardColumn = new BoardColumnEntity();
    //private BoardColumn boardColumn; // Assuming you have a BoardColumn entity

    // Default constructor (required by JPA)
    public CardEntity() {
    }

    // Constructor with required fields
    public CardEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and setters

    
    
    public Long getId() {
        return id;
    }

    public BoardColumnEntity getBoardColumn() {
		return boardColumn;
	}

	public void setBoardColumn(BoardColumnEntity boardColumn) {
		this.boardColumn = boardColumn;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Card{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
