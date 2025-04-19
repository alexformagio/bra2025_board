CREATE TABLE CARDS(
	id serial PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	board_column_id BIGINT NOT NULL,
	CONSTRAINT boards_columns_cards_fk FOREIGN KEY (board_column_id) REFERENCES BOARDS_COLUMNS(id)
);
