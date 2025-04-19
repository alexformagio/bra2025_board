CREATE TABLE BOARDS_COLUMNS(
	id serial PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	order_board int NOT NULL,
	kind VARCHAR(10) NOT NULL,
	board_id BIGINT NOT NULL,
	CONSTRAINT boards_boards_columns_fk FOREIGN KEY (board_id) REFERENCES BOARDS(id),
	CONSTRAINT id_order_uk UNIQUE (board_id, order_board)
);