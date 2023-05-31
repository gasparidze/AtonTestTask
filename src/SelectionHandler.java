import java.util.*;
import java.util.stream.Collectors;

/**
 * 1) Такая реализация позволяет получать записи по любому поля почти всегда за константное время, так как мы можем
 * быть уверены, что коллизий не будет, hashCode() для классов-оберток (Long, String, Double) реализован самой Java.
 * <p>
 * 2) Конечно, данный способ нельзя назвать экономичным, мы используем мало того, что 3 коллекции, так еще в каждой
 * Map'е есть еще и List, но это сделано потому, что при поиске по каждому ключу может находиться не один элемент, а
 * сколько угодно, поэтому необходим List.
 * <p>
 * 3) Здесь я не использовал алгоритмы бинарного/интерполяционного и др. поиска (которые работают за логарифмическое время),
 * так как все эти алгоритмы работают для отсортированных коллекций, в нашем же случае коллекция меняется динамически.
 * Даже, если применять какой-либо алгоритм, работающий за logN, дополнительное время будет занимать сортировка коллекции,
 * самая быстрая из которых будет работать за N*logN, а также алгоритм будет находить индекс первого найденного эл-та,
 * но элементов по какому-либо ключу может быть сколько угодно. Конечно, и эту проблему можно обойти, но это плюс время
 * обработки. Таким образом, при помощи известных алгоритмов поиска элементов, нашу динамически меняющуюся коллекцию
 * не запроцессить за logN.
 */
public class SelectionHandler {
    /**
     * записи ассоциированные с ключом account
     */
    private Map<Long, List<Model>> accountMap;

    /**
     * записи ассоциированные с ключом name
     */
    private Map<String, List<Model>> nameMap;

    /**
     * записи ассоциированные с ключом value
     */
    private Map<Double, List<Model>> valueMap;

    /**
     * экземпляр класса Scanner для считывания введенных пользователем данных
     */
    private Scanner scanner;

    /**
     * конструктор класса
     */
    public SelectionHandler() {
        accountMap = new HashMap<>();
        nameMap = new HashMap<>();
        valueMap = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    /**
     * метод, осуществляющий поиск записей по любому полю
     */
    public void selectRecords() {
        if (areCollectionsEmpty()) return;

        TextHelper.displaySelectionText();
        List<Model> selectedRecords = new ArrayList<>();

        int menuSelection = SelectionUtility.getValue(() -> Integer.parseInt(scanner.nextLine()));

        switch (menuSelection) {
            case 1: {
                System.out.print("account (long)= ");
                long account = SelectionUtility.getValue(() -> Long.parseLong(scanner.nextLine()));
                selectedRecords = accountMap.get(account);
                break;
            }
            case 2: {
                System.out.print("name (String)= ");
                String name = SelectionUtility.getValue(() -> scanner.nextLine());
                selectedRecords = nameMap.get(name);
                break;
            }
            case 3: {
                System.out.print("value (double)= ");
                double value = SelectionUtility.getValue(() -> Double.parseDouble(scanner.nextLine()));
                selectedRecords = valueMap.get(value);
                break;
            }
        }

        if (selectedRecords != null && !selectedRecords.isEmpty()) {
            System.out.println("Найденные записи: \n" + selectedRecords);
        } else {
            System.out.println("По данному полю записи не найдены");
        }

    }

    /**
     * метод, осуществляющий добавление записей в коллекции
     */
    public void addNewRecord() {
        Model model = getModelFromUser();

        long account = model.getAccount();
        String name = model.getName();
        double value = model.getValue();

        fillMap(model, account);
        fillMap(model, name);
        fillMap(model, value);
    }

    /**
     * метод, осуществляющий изменение записей в коллекциях
     */
    public void editRecord() {
        if (areCollectionsEmpty()) return;

        List<Model> allValues = getAllValues();
        displayAllRecords(allValues);

        System.out.print("Введите запись, которую хотите изменить: ");
        int recordSelection = SelectionUtility.getValue(() -> Integer.parseInt(scanner.nextLine()));
        Model editedRecord = allValues.get(recordSelection - 1);
        System.out.println("1.account\n2.name\n3.value");
        System.out.print("Введите номер поля, которое хотели бы изменить: ");
        int fieldSelection = SelectionUtility.getValue(() -> Integer.parseInt(scanner.nextLine()));
        System.out.println("Введите новое значение");

        switch (fieldSelection) {
            case 1: {
                System.out.print("account (long)= ");
                long account = SelectionUtility.getValue(() -> Long.parseLong(scanner.nextLine()));
                if (editedRecord.getAccount() != account) {
                    accountMap.get(editedRecord.getAccount()).remove(editedRecord);
                    editedRecord.setAccount(account);
                    fillMap(editedRecord, account);
                }
                break;
            }
            case 2: {
                System.out.print("name (String)= ");
                String name = SelectionUtility.getValue(() -> scanner.nextLine());
                if (editedRecord.getName() != name) {
                    nameMap.get(editedRecord.getName()).remove(editedRecord);
                    editedRecord.setName(name);
                    fillMap(editedRecord, name);
                }
                break;
            }
            case 3: {
                System.out.print("value (double)= ");
                double value = SelectionUtility.getValue(() -> Double.parseDouble(scanner.nextLine()));
                if (editedRecord.getValue() != value) {
                    valueMap.get(editedRecord.getValue()).remove(editedRecord);
                    editedRecord.setValue(value);
                    fillMap(editedRecord, value);
                }
                break;
            }
        }

        System.out.println("\nЗапись успешно изменена!");
    }

    /**
     * метод, осуществляющий удаление записей из коллекций
     */
    public void deleteRecord() {
        if (areCollectionsEmpty()) return;

        List<Model> allValues = getAllValues();
        displayAllRecords(allValues);

        System.out.print("Введите запись, которую хотите удалить: ");
        int selection = SelectionUtility.getValue(() -> Integer.parseInt(scanner.nextLine()));
        Model removedRecord = allValues.get(selection - 1);
        accountMap.get(removedRecord.getAccount()).remove(removedRecord);
        nameMap.get(removedRecord.getName()).remove(removedRecord);
        valueMap.get(removedRecord.getValue()).remove(removedRecord);

        if (accountMap.get(removedRecord.getAccount()).isEmpty()) {
            accountMap.remove(removedRecord.getAccount());
        }
        if (nameMap.get(removedRecord.getName()).isEmpty()) {
            nameMap.remove(removedRecord.getName());
        }
        if (valueMap.get(removedRecord.getValue()).isEmpty()) {
            valueMap.remove(removedRecord.getValue());
        }

        System.out.println("\nЗапись успешно удалена!");
    }

    /**
     * метод, проверяющий наличие элементов в коллекциях
     *
     * @return Boolean - возвращает true/false в зависимости от того, есть ли записи в коллекциях или нет
     */
    private Boolean areCollectionsEmpty() {
        Boolean isEmpty = false;
        if (accountMap.isEmpty() || nameMap.isEmpty() || valueMap.isEmpty()) {
            System.out.println("\nКоллекция пуста!");
            isEmpty = true;
        }

        return isEmpty;
    }

    /**
     * метод, формирующий новую запись на основе того, что ввел пользователь
     *
     * @return возвращает новую запись на основе данных от пользователя
     */
    private Model getModelFromUser() {
        System.out.println("Добавьте новую запись:\n");
        System.out.print("account (long)= ");
        long account = SelectionUtility.getValue(() -> Long.parseLong(scanner.nextLine()));

        System.out.print("name (string)= ");
        String name = SelectionUtility.getValue(() -> scanner.nextLine());

        System.out.print("value (double)= ");
        double value = SelectionUtility.getValue(() -> Double.parseDouble(scanner.nextLine()));

        return new Model(account, name, value);
    }

    /**
     * метод, формирующий общую коллекцию записей
     *
     * @return возвращает List всех записей
     */
    private List<Model> getAllValues() {
        return accountMap.values().stream().flatMap(el -> el.stream()).collect(Collectors.toList());
    }

    /**
     * метод, отображающий все записи
     *
     * @param allValues - List всех записей
     */
    private void displayAllRecords(List<Model> allValues) {
        for (int i = 0; i < allValues.size(); i++) {
            System.out.println(i + 1 + ". " + allValues.get(i));
        }
    }

    /**
     * generic метод, добавляющий записи в коллекции, ассоциированные с соответствующим полями
     *
     * @param model - запись
     * @param key   - одно из полей, которое являются ключом для мапы
     */
    private <T> void fillMap(Model model, T key) {
        if (key instanceof Long) {
            if (!accountMap.containsKey(key)) {
                accountMap.put((Long) key, new ArrayList<>());
            }
            accountMap.get(key).add(model);
        } else if (key instanceof String) {
            if (!nameMap.containsKey(key)) {
                nameMap.put((String) key, new ArrayList<>());
            }
            nameMap.get(key).add(model);
        } else if (key instanceof Double) {
            if (!valueMap.containsKey(key)) {
                valueMap.put((Double) key, new ArrayList<>());
            }
            valueMap.get(key).add(model);
        }
    }
}