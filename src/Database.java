import java.sql.*;
import java.util.Scanner;

// Создаем класс Database для хранения объектов DataElement
class Database {
    // Объявляем поле для хранения подключения к базе данных
    private final Connection conn;

    Scanner input = new Scanner(System.in);

    // Создаем конструктор с параметрами url, user и password для подключения к базе данных
    public Database(String url, String user, String password) throws SQLException, ClassNotFoundException {
        // Используем класс DriverManager для получения подключения к базе данных по url, user и password
        conn = DriverManager.getConnection(url, user, password);
        // Создаем таблицу в базе данных для хранения объектов DataElement
    }


    // Создаем метод createTable для создания таблицы в базе данных
    public void createTable() throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Определяем SQL-запрос для создания таблицы с именем elements и двумя столбцами: id (целочисленный первичный ключ) и data (строковый тип)
        // Выполняем SQL-запрос с помощью метода executeUpdate
        stmt.executeUpdate("CREATE TABLE "+getTableName()+" (id INTEGER not NULL AUTO_INCREMENT, PRIMARY KEY(id));");
        // Закрываем объект Statement
        stmt.close();
    }

    public boolean isTableExists(String tableName) throws SQLException {
        // Проверяем, существует ли таблица в базе данных
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
        return resultSet.next();
    }

    public String getTableName() {
        System.out.println("Введите имя таблицы");
        String tableName = input.nextLine();
        return "`" + tableName + "`";
    }

    public void deleteTable() throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Создаем SQL-запрос для удаления таблицы
        String sql = "DROP TABLE IF EXISTS " + getTableName();
        // Выполнением SQL-запроса с помощью метода executeUpdate
        stmt.executeUpdate(sql);
        // Завершаем работу с объектом Statement
        stmt.close();
    }

    public void addColumnToTable() throws SQLException {
        Statement stmt = conn.createStatement();
        //We need to add column to table
        //Asking the user the name of the column
        System.out.println("Введите имя столбца");
        String columnName = input.nextLine();
        //Asking the user the type of the column
        //We need most used types: int, double, string, date, boolean
        //Checking the type of the column
        //Displaying the types of the column
            System.out.println("Введите тип данных");
            System.out.println("1. int");
            System.out.println("2. double");
            System.out.println("3. string");
            System.out.println("4. date");
            System.out.println("5. boolean");
            //Getting the type of the column
            int columnType = Integer.parseInt(input.nextLine());
            String sql;
            //Checking the type of the column
            switch (columnType) {
                case 1 -> {
                    //Creating the SQL-query
                    sql = "ALTER TABLE " + getTableName() + " ADD " + columnName + " int;";
                    //Executing the SQL-query
                    stmt.executeUpdate(sql);
                    //Closing the statement
                    stmt.close();
                }
                case 2 -> {
                    //Creating the SQL-query
                    sql = "ALTER TABLE " + getTableName() + " ADD " + columnName + " double;";
                    //Executing the SQL-query
                    stmt.executeUpdate(sql);
                    //Closing the statement
                    stmt.close();
                }
                case 3 -> {
                    //Creating the SQL-query
                    sql = "ALTER TABLE " + getTableName() + " ADD " + columnName + " VARCHAR(100);";
                    //Executing the SQL-query
                    stmt.executeUpdate(sql);
                    //Closing the statement
                    stmt.close();
                }
                case 4 -> {
                    //Creating the SQL-query
                    sql = "ALTER TABLE " + getTableName() + " ADD " + columnName + " date;";
                    //Executing the SQL-query
                    stmt.executeUpdate(sql);
                    //Closing the statement
                    stmt.close();
                }
                case 5 -> {
                    //Creating the SQL-query
                    sql = "ALTER TABLE " + getTableName() + " ADD " + columnName + " boolean;";
                    //Executing the SQL-query
                    stmt.executeUpdate(sql);
                    //Closing the statement
                    stmt.close();
                }
                default -> throw new IllegalStateException("Unexpected value: " + columnType);
            }
        }


    // Создаем метод addElement для добавления нового объекта DataElement в базу данных
    public void addElement(DataElement element) throws SQLException {
        // Создаем объект PreparedStatement для выполнения параметризованных SQL-запросов
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ? (id, data) VALUES (NULL, ?)");
        // Устанавливаем значение параметра в SQL-запросе с помощью метода setObject
        pstmt.setObject(1, getTableName());
        pstmt.setObject(2, element.getData());
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
