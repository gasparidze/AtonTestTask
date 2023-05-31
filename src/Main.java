/**
 * основной класс, запускающий процесс
 */
public class Main {
    /**
     * основной метод, точка входа в программу
     *
     * @param args
     */
    public static void main(String[] args) {
        InMemorySimpleDB inMemorySimpleDB = new InMemorySimpleDB();
        inMemorySimpleDB.execute();
    }
}
