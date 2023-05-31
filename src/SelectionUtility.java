import java.util.function.Supplier;

/**
 * utility класс, отвечаеющий за считывания информации от пользователя
 */
public class SelectionUtility {
    /**
     * generic метод, обрабатывающий входные данные с консоли от пользователя
     *
     * @param supplier - экземпляр класса, реализующий метод get() функциоанльного интерфейса Supplier
     * @return введенное пользователем значение
     */
    public static <T> T getValue(Supplier<T> supplier) {
        T formattedValue = null;
        boolean isCorrectInput = false;
        do {
            try {
                formattedValue = supplier.get();
                isCorrectInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Некорректный тип данных\nВведите повторно: ");
            }
        } while (!isCorrectInput);

        return formattedValue;
    }
}
