package com.formagio.board.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionConfig {
  public static Connection getConnection() throws SQLException, ClassNotFoundException {
	  String jdbcUrl = "jdbc:postgresql://localhost:5432/board";
	  String user= "board";
	  String pass ="board";
	  Class.forName("org.postgresql.Driver");
	  Connection conn = DriverManager.getConnection(jdbcUrl, user, pass);
	  conn.setAutoCommit(false);
	  return conn;
  }
}
