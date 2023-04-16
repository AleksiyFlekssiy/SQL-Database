// Импортируем необходимые классы и интерфейсы
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Создаем класс DataElement для хранения данных любого типа
class DataElement {
    // Объявляем поле для хранения данных
    private Object data;

    // Создаем конструктор с параметром data
    public DataElement(Object data) {
        this.data = data;
    }

    // Создаем геттер и сеттер для поля data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // Переопределяем метод toString для отображения данных
    @Override
    public String toString() {
        return data.toString();
    }
}

// Создаем класс Database для хранения объектов DataElement
class Database {
    // Объявляем поле для хранения подключения к базе данных
    private Connection conn;

    // Создаем конструктор с параметрами url, user и password для подключения к базе данных
    public Database(String url, String user, String password) throws SQLException, ClassNotFoundException {
        // Используем класс DriverManager для получения подключения к базе данных по url, user и password
        conn = DriverManager.getConnection(url, user, password);
        // Создаем таблицу в базе данных для хранения объектов DataElement
        createTable();
    }

    // Создаем метод createTable для создания таблицы в базе данных
    private void createTable() throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Определяем SQL-запрос для создания таблицы с именем elements и двумя столбцами: id (целочисленный первичный ключ) и data (строковый тип)
        String sql = "CREATE TABLE IF NOT EXISTS elements (id INTEGER not NULL AUTO_INCREMENT, data VARCHAR(255), PRIMARY KEY(id))";
        // Выполняем SQL-запрос с помощью метода executeUpdate
        stmt.executeUpdate(sql);
        // Закрываем объект Statement
        stmt.close();
    }

    public void deleteTable() throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Создаем SQL-запрос для удаления таблицы
        String sql = "DROP TABLE IF EXISTS elements";
        // Выполнением SQL-запроса с помощью метода executeUpdate
        stmt.executeUpdate(sql);
        // Завершаем работу с объектом Statement
        stmt.close();
    }

    // Создаем метод addElement для добавления нового объекта DataElement в базу данных
    public void addElement(DataElement element) throws SQLException {
        // Создаем объект PreparedStatement для выполнения параметризованных SQL-запросов
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO elements (id, data) VALUES (NULL, ?)");
        // Устанавливаем значение параметра в SQL-запросе с помощью метода setObject
        pstmt.setObject(1, element.getData());
        // Выполняем SQL-запрос с помощью метода executeUpdate
        pstmt.executeUpdate();
        // Закрываем объект PreparedStatement
        pstmt.close();
    }

    // Создаем метод updateElement для изменения объекта DataElement в базе данных по его id
    public void updateElement(int id, DataElement element) throws SQLException {
        // Создаем объект PreparedStatement для выполнения параметризованных SQL-запросов
        PreparedStatement pstmt = conn.prepareStatement("UPDATE elements SET data = ? WHERE id = ?");
        // Устанавливаем значения параметров в SQL-запросе с помощью метода setObject
        pstmt.setObject(1, element.getData());
        pstmt.setInt(2, id);
        // Выполняем SQL-запрос с помощью метода executeUpdate
        pstmt.executeUpdate();
        // Закрываем объект PreparedStatement
        pstmt.close();
    }

    // Создаем метод deleteElement для удаления объекта DataElement из базы данных по его id
    public void deleteElement(int id) throws SQLException {
        // Создаем объект PreparedStatement для выполнения параметризованных SQL-запросов
        PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM elements WHERE id = ?");
        PreparedStatement pstmt2 = conn.prepareStatement("UPDATE elements SET id = id - 1 WHERE id > ?");
        // Устанавливаем значение параметра в SQL-запросе с помощью метода setInt
        pstmt1.setInt(1, id);
        pstmt2.setInt(1, id);
        // Выполняем SQL-запрос с помощью метода executeUpdate
        pstmt1.executeUpdate();
        pstmt2.executeUpdate();
        // Закрываем объекты PreparedStatement
        pstmt1.close();
        pstmt2.close();
    }

    // Создаем метод displayElements для отображения данных всех объектов DataElement из базы данных
    public void displayElements() throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Определяем SQL-запрос для выборки всех данных из таблицы elements
        String sql = "SELECT * FROM elements";
        // Выполняем SQL-запрос с помощью метода executeQuery и получаем объект ResultSet для обработки результата
        ResultSet rs = stmt.executeQuery(sql);
        // Перебираем все строки в ResultSet с помощью метода next и выводим данные на экран с помощью метода getObject и getInt
        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + ", data: " + rs.getObject("data"));
        }
        // Закрываем объекты ResultSet и Statement
        rs.close();
        stmt.close();
    }

    // Создаем метод close для закрытия подключения к базе данных
    public void close() throws SQLException {
        // Закрываем объект Connection
        conn.close();
    }
}

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
