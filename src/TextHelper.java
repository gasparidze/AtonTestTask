/**
 * класс, отвечающий за вывод текста в консоль для пользователя
 */
public class TextHelper {
    /**
     * основное, приветственное меню программы
     */
    public static void displayMenu() {
        System.out.println("\nВыберите один из нижеприведенных пунктов:\n");
        System.out.println("1. Найти записи");
        System.out.println("2. Добавить новую запись");
        System.out.println("3. Редактировать запись");
        System.out.println("4. Удалить запись");
        System.out.println("0. Выход\n");
        System.out.print("Выберите пункт и нажмите Enter: ");
    }

    /**
     * метод, отображающий текст при поиске записей в системе
     */
    public static void displaySelectionText() {
        System.out.println("По какому полю хотели бы искать значения?");
        System.out.println("1.account\n2.name\n3.value");
        System.out.print("Введите номер поля: ");
    }
}