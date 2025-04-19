package com.formagio.board.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import com.formagio.board.persistence.entity.BoardColumnEntity;
import com.formagio.board.persistence.entity.BoardColumnKindEnum;
import com.formagio.board.persistence.entity.BoardEntity;
import com.formagio.board.service.BoardQueryService;
import com.formagio.board.service.BoardService;

import static com.formagio.board.persistence.entity.BoardColumnKindEnum.CANCEL;
import static com.formagio.board.persistence.entity.BoardColumnKindEnum.FINAL;
import static com.formagio.board.persistence.entity.BoardColumnKindEnum.INITIAL;
import static com.formagio.board.persistence.entity.BoardColumnKindEnum.PENDING;

public class MainMenu {

	public final Scanner scanner = new Scanner(System.in);//.useDelimiter("\n");
	public final Scanner scannerString = new Scanner(System.in).useDelimiter("\n");
	private Connection conn;
	
	public MainMenu(Connection conn) {
		super();
		this.conn = conn;
	}


	public void execute() throws SQLException {
		System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
		int option = -1;
		while(true) {
			System.out.println("1 - Criar um novo board");
			System.out.println("2 - Selecionar um board existente" );
			System.out.println("3 - Excluir um board");
			System.out.println("4 - Sair");
			option = scanner.nextInt();
			switch (option){
			case 1 : createBoard();
			         break;
			case 2 : selectBoard();
			         break;
			case 3 : deleteBoard();
			         break;
			case 4 : System.exit(0);
			default : System.out.println("opção inválida, informe uma opção do menu");
			}
		}
	}


	private void deleteBoard() throws SQLException {
		System.out.println("Informe o id do board que será excluido");
		int id = scanner.nextInt();
		BoardService service = new BoardService(conn);
		if (service.delete(id)){
		System.out.printf("O board %s foi excluido\n", id);
		} else {
		System.out.printf("Nao foi encontrado um board com id %s \n", id);
		}
	}

	private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");
        int id = scanner.nextInt();
        BoardQueryService queryService = new BoardQueryService(conn);
        Optional<BoardEntity> optional = queryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b,conn).execute(),
                    () -> System.out.printf("Não foi encontrado um board com id %s\n", id)
            );
        
	}

	private void createBoard() throws SQLException {
		BoardEntity entity = new BoardEntity();
        System.out.println("Informe o nome do seu board");
        entity.setName(scannerString.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'");
        int additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board");
        String initialColumnName = scannerString.next();
        BoardColumnEntity initialColumn = createColumn(initialColumnName, INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            String pendingColumnName = scannerString.next();
            BoardColumnEntity pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        String finalColumnName = scannerString.next();
        BoardColumnEntity finalColumn = createColumn(finalColumnName, FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do baord");
        String cancelColumnName = scannerString.next();
        BoardColumnEntity cancelColumn = createColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumn(columns);
        BoardService service = new BoardService(conn);
        service.insert(entity);
	}
	
    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order){
        BoardColumnEntity boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }


}