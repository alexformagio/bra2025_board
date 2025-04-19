package com.formagio.board.persistence.migration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import static com.formagio.board.persistence.config.ConnectionConfig.getConnection;

public class MigrationStrategy {
	
	private final Connection connection;

	public MigrationStrategy(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public void executeMigration() {
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;

		try (FileOutputStream fos = new FileOutputStream("liquibase.log")) {
			System.setOut(new PrintStream(fos));
			System.setErr(new PrintStream(fos));
			try (Connection connection = getConnection();
					JdbcConnection jdbcConnection = new JdbcConnection(connection);
					Liquibase liquibase = new Liquibase("/db/changelog/db.changelog-master.yml",
							new ClassLoaderResourceAccessor(), jdbcConnection);) {
				liquibase.update();
			} catch (SQLException | LiquibaseException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.setOut(originalOut);
			System.setErr(originalErr);
		}

	}

}
