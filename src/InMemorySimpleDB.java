import java.util.Scanner;

/**
 * класс, обрабатывающий действия над записями
 */
public class InMemorySimpleDB {
    /**
     * экземпляр класса-обработчика, в котором прописана основная логика по CRED
     */
    private SelectionHandler selectionHandler;

    InMemorySimpleDB() {
        selectionHandler = new SelectionHandler();
    }

    /**
     * метод, запускающий процесс обработки записей
     */
    public void execute() {
        boolean isExit = false;
        do {
            TextHelper.displayMenu();
            Scanner scanner = new Scanner(System.in);
            int menuSelection = SelectionUtility.getValue(() -> Integer.parseInt(scanner.nextLine()));

            switch (menuSelection) {
                case 1: {
                    selectionHandler.selectRecords();
                    break;
                }
                case 2: {
                    selectionHandler.addNewRecord();
                    break;
                }
                case 3: {
                    selectionHandler.editRecord();
                    break;
                }
                case 4: {
                    selectionHandler.deleteRecord();
                    break;
                }
                case 0: {
                    isExit = true;
                    break;
                }
            }
        } while (!isExit);
    }
}
