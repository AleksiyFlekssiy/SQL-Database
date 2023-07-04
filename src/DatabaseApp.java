// Импортируем необходимые классы и интерфейсы
import java.sql.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

// Создаем класс DatabaseApp для тестирования работы классов DataElement и Database
@SuppressWarnings("LocalVariableUsedAndDeclaredInDifferentSwitchBranches")
public class DatabaseApp {
    private static Database database;
    private static Scanner scanner;

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) throws SQLException {
        setUpDatabaseAndScanner();
        printInstructions();

        int choice = 0;
        do {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                handleUserChoice(choice);
            } catch (InputMismatchException e) {
                System.out.println("Введите число");
            }

        } while (choice != 0);

        closeResources();
    }

    private static void setUpDatabaseAndScanner() {
        try {
            database = new Database(DATABASE_URL, USERNAME, PASSWORD);
            scanner = new Scanner(System.in);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printInstructions() {
        System.out.println("Добро пожаловать в приложение для работы с базой данных!");
        // remaining instructions
    }

    private static void handleUserChoice(int choice) throws SQLException {
        switch (choice) {
            case 1: addNewElement(); break;
            case 2: updateExistingElement(); break;
            case 3: removeExistingElement(); break;
            case 4: displayAllElements(); break;
            case 5: createNewTable(); break;
            case 6: deleteExistingTable(); break;
            case 7: addNewColumn(); break;
            case 8: changeExistingColumn(); break;
            case 9: deleteExistingColumn(); break;
            case 10: 
            // remaining cases
            default: printInstructions(); break;
        }
    }

    private static void deleteExistingColumn() throws SQLException {
        System.out.println("Введите имя таблицы");
        String table_name = scanner.nextLine();
        System.out.println("Введите имя столбца");
        String column_name = scanner.nextLine();
        database.deleteColumnFromTable(table_name, column_name);
        System.out.println("Столбец " + column_name + " был успешно удалён");
    }

    private static void changeExistingColumn() {
        
    }

    private static void addNewColumn() throws SQLException {
        System.out.println("Введите имя таблицы");
        String table_name = scanner.nextLine();
        System.out.println("Введите имя столбца");
        String column_name = scanner.nextLine();
        System.out.println("""
                Введите тип данных:
                1. Целое число
                2. Дробное число
                3. Строка
                4. Дата
                5. Логическое
                """);
        int column_type = scanner.nextInt();
        database.addColumnToTable(column_name,table_name,column_type);
        System.out.println("Столбец " + column_name + " был успешно добавлен");
    }

    private static void deleteExistingTable() throws SQLException {
        System.out.println("Введите имя таблицы");
        String name = scanner.nextLine();
        database.deleteTable(name);
        System.out.println("Таблица" + name + " была успешно удалена");
    }

    private static void createNewTable() throws SQLException {
        System.out.println("Введите имя для таблицы");
        String name = scanner.nextLine();
        database.createTable(name);
        System.out.println("Таблица " + name + " была успешно создана");
    }

    private static void displayAllElements() {
    }

    private static void removeExistingElement() {
        
    }

    private static void updateExistingElement() {
        
    }

    private static void addNewElement() throws SQLException {
        System.out.println("Сколько элементов требуется добавить, один или несколько?");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Один")) {
            System.out.println("Введите имя таблицы");
            String table_name = scanner.nextLine();
            System.out.println("Введите имя столбца");
            String column_name = scanner.nextLine();
            System.out.println("Введите значение элемента");
            DataElement element = new DataElement(scanner.nextLine());
            database.addElement(table_name, column_name, element);
            System.out.println("Элемент был успешно добавлен");
        }
        else if (choice.equalsIgnoreCase("Несколько")){
            System.out.println("Введите имя таблицы");
            String table_name = scanner.nextLine();
            System.out.println("Сколько конкретно элементов будет добавлено?");
            int count = Integer.parseInt(scanner.nextLine());
            String[] column_names = new String[count];
            DataElement[] elements = new DataElement[count];
            for (int i = 0; i < count; i++){
                System.out.println("Введите столбец");
                column_names[i] = scanner.nextLine();
                System.out.println("Введите значение элемента");
                elements[i] = new DataElement(scanner.nextLine());
            }
            database.addElementToMultipleColumns(table_name,column_names,elements);
        }
        else throw new IllegalArgumentException("Неверный выбор");
    }

    // Similar functions for updateExistingElement, removeExistingelement, displayAllElement

    private static void closeResources() throws SQLException {
        scanner.close();
        database.close();
    }
}
