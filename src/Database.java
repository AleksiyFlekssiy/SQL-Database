import java.sql.*;
import java.util.ArrayList;
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
    public void createTable(String table_name) throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Определяем SQL-запрос для создания таблицы с именем elements и двумя столбцами: id (целочисленный первичный ключ) и data (строковый тип)
        String sql_query = "CREATE TABLE " + table_name + " (id INTEGER not NULL AUTO_INCREMENT, PRIMARY KEY(id));";
        // Выполняем SQL-запрос с помощью метода executeUpdate
        stmt.executeUpdate(sql_query);
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
        return input.nextLine();
    }

    public void deleteTable(String table_name) throws SQLException {
        // Создаем объект Statement для выполнения SQL-запросов
        Statement stmt = conn.createStatement();
        // Создаем SQL-запрос для удаления таблицы
        String sql_query = "DROP TABLE IF EXISTS " + table_name;
        // Выполнением SQL-запроса с помощью метода executeUpdate
        stmt.executeUpdate(sql_query);
        // Завершаем работу с объектом Statement
        stmt.close();
    }

    public void addColumnToTable(String column_name, String table_name, int column_type) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql_query;
        switch (column_type) {
            case 1 -> sql_query = "ALTER TABLE " + table_name + " ADD " + column_name + " int;";
            case 2 -> sql_query = "ALTER TABLE " + table_name + " ADD " + column_name + " double;";
            case 3 -> sql_query = "ALTER TABLE " + table_name + " ADD " + column_name + " VARCHAR(100);";
            case 4 -> sql_query = "ALTER TABLE " + table_name + " ADD " + column_name + " date;";
            case 5 -> sql_query = "ALTER TABLE " + table_name + " ADD " + column_name + " boolean;";
            default -> throw new IllegalStateException("Unexpected value: " + column_type);
        }
        stmt.executeUpdate(sql_query);
        stmt.close();
    }

    public void deleteColumnFromTable(String table_name, String column_name) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql_query = "ALTER TABLE " + table_name + " DROP COLUMN " + column_name;
        stmt.executeUpdate(sql_query);
        stmt.close();
    }

    public ArrayList<String> getColumns() throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            columns.add(columnName);
        }
        return columns;
    }

    public ArrayList<String> getColumns(String table) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + table);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            columns.add(columnName);
        }
        return columns;
    }


    // Создаем метод addElement для добавления нового объекта DataElement в базу данных
    public void addElement(String table_name, String column_name, DataElement element) throws SQLException {
        String sql_query = "INSERT INTO " + table_name + "("+ column_name +") VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql_query);
        pstmt.setObject(1,element.getData());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addElementToMultipleColumns(String tableName, String[] columnNames, DataElement[] columnValues) throws SQLException {
        if (columnNames.length != columnValues.length) {
            throw new IllegalArgumentException("Number of column names should be equal to number of column values.");
        }

        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");

        for (String columnName : columnNames) {
            query.append(columnName).append(",");
        }

        query.setLength(query.length() - 1);  //remove last comma
        query.append(") VALUES (");

        for (int i = 0; i < columnValues.length; i++) {
            query.append("?,");
        }

        query.setLength(query.length() - 1); //remove last comma
        query.append(");");

        PreparedStatement pstmt = conn.prepareStatement(query.toString());

        for (int i = 0; i < columnValues.length; i++) {
            pstmt.setObject(i + 1, columnValues[i].getData());
        }

        pstmt.executeUpdate();
        pstmt.close();
    }

    // Создаем метод updateElement для изменения объекта DataElement в базе данных по его id
    public void updateElement(int id, DataElement element) throws SQLException {
        // Создаем объект PreparedStatement для выполнения параметризованных SQL-запросов
        PreparedStatement pstmt = conn.prepareStatement("UPDATE " + getTableName() + " SET data = ? WHERE id = ?");
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
        String sql = "SELECT * FROM ";
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

    //Getting info by id
    public ResultSet getElementById(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + getTableName() + " WHERE id = " + id);
        return rs;
    }

    // Создаем метод close для закрытия подключения к базе данных
    public void close() throws SQLException {
        // Закрываем объект Connection
        conn.close();
    }

}
