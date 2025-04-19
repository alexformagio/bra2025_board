package com.formagio.board.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnKindEnum {
	INITIAL, FINAL, CANCEL, PENDING;
	
	public static BoardColumnKindEnum findByName(String value) {
		return Stream.of(BoardColumnKindEnum.values())
				.filter( n -> n.name().equals(value))
				.findFirst().orElseThrow();
	}

}
