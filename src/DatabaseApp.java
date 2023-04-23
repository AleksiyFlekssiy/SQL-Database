// Импортируем необходимые классы и интерфейсы
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Создаем класс DatabaseApp для тестирования работы классов DataElement и Database
public class DatabaseApp {
    public static void main(String[] args) {

        try {
            // Создаем объект Database с параметрами подключения к базе данных (в зависимости от вашего DBMS и JDBC-драйвера)
            Database db = new Database("jdbc:mysql://localhost:3306/mydatabase", "root", "password");

            // Создаем объект Scanner для ввода данных с клавиатуры
            Scanner sc = new Scanner(System.in);

            // Выводим текстовый интерфейс для работы с базой данных
            System.out.println("Добро пожаловать в приложение для работы с базой данных!");
            System.out.println("Выберите одну из следующих операций:");
            System.out.println("1 - добавить новый элемент в базу данных");
            System.out.println("2 - изменить элемент в базе данных по его id");
            System.out.println("3 - удалить элемент из базы данных по его id");
            System.out.println("4 - отобразить данные всех элементов из базы данных");
            System.out.println("5 - удалить таблицу");
            System.out.println("6 - создать таблицу");


            // Объявляем переменную для хранения выбора пользователя
            int choice = 0;

            // Используем цикл do-while для повторения операций до тех пор, пока пользователь не выберет 5 - выйти из приложения
            do {
                // Считываем выбор пользователя с клавиатуры с помощью метода nextInt и присваиваем его переменной choice
                try {
                    choice = sc.nextInt();
                }
                catch (InputMismatchException e) {
                    System.out.println("Введите число");
                }

                // Используем оператор switch-case для выполнения разных действий в зависимости от выбора пользователя
                switch (choice) {
                    case 1:
                        // Добавляем новый элемент в базу данных
                        System.out.println("Введите данные для нового элемента:");
                        // Считываем данные с клавиатуры с помощью метода next и создаем объект DataElement с этими данными
                        DataElement element = new DataElement(sc.next());
                        // Вызываем метод addElement класса Database для добавления нового элемента в базу данных
                        db.addElement(element);
                        // Выводим сообщение об успешном добавлении элемента
                        System.out.println("Элемент успешно добавлен в базу данных!");
                        break;
                    case 2:
                        // Изменяем элемент в базе данных по его id
                        System.out.println("Введите id элемента, который хотите изменить:");
                        // Считываем id с клавиатуры с помощью метода nextInt
                        int id = sc.nextInt();
                        System.out.println("Введите новые данные для этого элемента:");
                        // Считываем данные с клавиатуры с помощью метода next и создаем объект DataElement с этими данными
                        element = new DataElement(sc.next());
                        // Вызываем метод updateElement класса Database для изменения элемента в базе данных по его id
                        db.updateElement(id, element);
                        // Выводим сообщение об успешном изменении элемента
                        System.out.println("Элемент успешно изменен в базе данных!");
                        break;
                    case 3:
                        // Удаляем элемент из базы данных по его id
                        System.out.println("Введите id элемента, который хотите удалить:");
                        // Считываем id с клавиатуры с помощью метода nextInt
                        id = sc.nextInt();
                        // Вызываем метод deleteElement класса Database для удаления элемента из базы данных по его id
                        db.deleteElement(id);
                        // Выводим сообщение об успешном удалении элемента
                        System.out.println("Элемент успешно удален из базы данных!");
                        break;
                    case 4:
                        // Отображаем данные всех элементов из базы данных
                        System.out.println("Данные всех элементов из базы данных:");
                        // Вызываем метод displayElements класса Database для отображения данных всех элементов из базы данных
                        db.displayElements();
                        break;
                    case 5:
                        db.deleteTable();
                        System.out.println("Таблица успешно удалена");
                        break;
                    case 6:
                        db.createTable();
                        break;
                    case 7:
                        //Adding column to the table using method from Database class
                        db.addColumnToTable();
                        break;
                    default:
                        // Выводим сообщение об ошибке при неверном выборе
                        System.out.println("Неверный выбор! Пожалуйста, выберите одну из следующих операций:");
                        System.out.println("1 - добавить новый элемент в базу данных");
                        System.out.println("2 - изменить элемент в базе данных по его id");
                        System.out.println("3 - удалить элемент из базы данных по его id");
                        System.out.println("4 - отобразить данные всех элементов из базы данных");
                        System.out.println("5 - выйти из приложения");
                }
            } while (choice != 0); // Повторяем цикл до тех пор, пока пользователь не выберет 5 - выйти из приложения

            // Закрываем объект Scanner
            sc.close();
            // Закрываем объект Database
            db.close();

        } catch (SQLException | ClassNotFoundException e) {
            // Обрабатываем исключения, связанные с ошибками базы данных
            e.printStackTrace();
        }
    }
}
