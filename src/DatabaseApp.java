// ����������� ����������� ������ � ����������
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// ������� ����� DataElement ��� �������� ������ ������ ����
class DataElement {
    // ��������� ���� ��� �������� ������
    private Object data;

    // ������� ����������� � ���������� data
    public DataElement(Object data) {
        this.data = data;
    }

    // ������� ������ � ������ ��� ���� data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // �������������� ����� toString ��� ����������� ������
    @Override
    public String toString() {
        return data.toString();
    }
}

// ������� ����� Database ��� �������� �������� DataElement
class Database {
    // ��������� ���� ��� �������� ����������� � ���� ������
    private Connection conn;

    // ������� ����������� � ����������� url, user � password ��� ����������� � ���� ������
    public Database(String url, String user, String password) throws SQLException, ClassNotFoundException {
        // ���������� ����� DriverManager ��� ��������� ����������� � ���� ������ �� url, user � password
        conn = DriverManager.getConnection(url, user, password);
        // ������� ������� � ���� ������ ��� �������� �������� DataElement
        createTable();
    }

    // ������� ����� createTable ��� �������� ������� � ���� ������
    private void createTable() throws SQLException {
        // ������� ������ Statement ��� ���������� SQL-��������
        Statement stmt = conn.createStatement();
        // ���������� SQL-������ ��� �������� ������� � ������ elements � ����� ���������: id (������������� ��������� ����) � data (��������� ���)
        String sql = "CREATE TABLE IF NOT EXISTS elements (id INTEGER not NULL AUTO_INCREMENT, data VARCHAR(255), PRIMARY KEY(id))";
        // ��������� SQL-������ � ������� ������ executeUpdate
        stmt.executeUpdate(sql);
        // ��������� ������ Statement
        stmt.close();
    }

    public void deleteTable() throws SQLException {
        // ������� ������ Statement ��� ���������� SQL-��������
        Statement stmt = conn.createStatement();
        // ������� SQL-������ ��� �������� �������
        String sql = "DROP TABLE IF EXISTS elements";
        // ����������� SQL-������� � ������� ������ executeUpdate
        stmt.executeUpdate(sql);
        // ��������� ������ � �������� Statement
        stmt.close();
    }

    // ������� ����� addElement ��� ���������� ������ ������� DataElement � ���� ������
    public void addElement(DataElement element) throws SQLException {
        // ������� ������ PreparedStatement ��� ���������� ����������������� SQL-��������
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO elements (id, data) VALUES (NULL, ?)");
        // ������������� �������� ��������� � SQL-������� � ������� ������ setObject
        pstmt.setObject(1, element.getData());
        // ��������� SQL-������ � ������� ������ executeUpdate
        pstmt.executeUpdate();
        // ��������� ������ PreparedStatement
        pstmt.close();
    }

    // ������� ����� updateElement ��� ��������� ������� DataElement � ���� ������ �� ��� id
    public void updateElement(int id, DataElement element) throws SQLException {
        // ������� ������ PreparedStatement ��� ���������� ����������������� SQL-��������
        PreparedStatement pstmt = conn.prepareStatement("UPDATE elements SET data = ? WHERE id = ?");
        // ������������� �������� ���������� � SQL-������� � ������� ������ setObject
        pstmt.setObject(1, element.getData());
        pstmt.setInt(2, id);
        // ��������� SQL-������ � ������� ������ executeUpdate
        pstmt.executeUpdate();
        // ��������� ������ PreparedStatement
        pstmt.close();
    }

    // ������� ����� deleteElement ��� �������� ������� DataElement �� ���� ������ �� ��� id
    public void deleteElement(int id) throws SQLException {
        // ������� ������ PreparedStatement ��� ���������� ����������������� SQL-��������
        PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM elements WHERE id = ?");
        PreparedStatement pstmt2 = conn.prepareStatement("UPDATE elements SET id = id - 1 WHERE id > ?");
        // ������������� �������� ��������� � SQL-������� � ������� ������ setInt
        pstmt1.setInt(1, id);
        pstmt2.setInt(1, id);
        // ��������� SQL-������ � ������� ������ executeUpdate
        pstmt1.executeUpdate();
        pstmt2.executeUpdate();
        // ��������� ������� PreparedStatement
        pstmt1.close();
        pstmt2.close();
    }

    // ������� ����� displayElements ��� ����������� ������ ���� �������� DataElement �� ���� ������
    public void displayElements() throws SQLException {
        // ������� ������ Statement ��� ���������� SQL-��������
        Statement stmt = conn.createStatement();
        // ���������� SQL-������ ��� ������� ���� ������ �� ������� elements
        String sql = "SELECT * FROM elements";
        // ��������� SQL-������ � ������� ������ executeQuery � �������� ������ ResultSet ��� ��������� ����������
        ResultSet rs = stmt.executeQuery(sql);
        // ���������� ��� ������ � ResultSet � ������� ������ next � ������� ������ �� ����� � ������� ������ getObject � getInt
        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + ", data: " + rs.getObject("data"));
        }
        // ��������� ������� ResultSet � Statement
        rs.close();
        stmt.close();
    }

    // ������� ����� close ��� �������� ����������� � ���� ������
    public void close() throws SQLException {
        // ��������� ������ Connection
        conn.close();
    }
}

// ������� ����� DatabaseApp ��� ������������ ������ ������� DataElement � Database
public class DatabaseApp {
    public static void main(String[] args) {

        try {
            // ������� ������ Database � ����������� ����������� � ���� ������ (� ����������� �� ������ DBMS � JDBC-��������)
            Database db = new Database("jdbc:mysql://localhost:3306/mydatabase", "root", "password");

            // ������� ������ Scanner ��� ����� ������ � ����������
            Scanner sc = new Scanner(System.in);

            // ������� ��������� ��������� ��� ������ � ����� ������
            System.out.println("����� ���������� � ���������� ��� ������ � ����� ������!");
            System.out.println("�������� ���� �� ��������� ��������:");
            System.out.println("1 - �������� ����� ������� � ���� ������");
            System.out.println("2 - �������� ������� � ���� ������ �� ��� id");
            System.out.println("3 - ������� ������� �� ���� ������ �� ��� id");
            System.out.println("4 - ���������� ������ ���� ��������� �� ���� ������");
            System.out.println("5 - ������� �������");


            // ��������� ���������� ��� �������� ������ ������������
            int choice = 0;

            // ���������� ���� do-while ��� ���������� �������� �� ��� ���, ���� ������������ �� ������� 5 - ����� �� ����������
            do {
                // ��������� ����� ������������ � ���������� � ������� ������ nextInt � ����������� ��� ���������� choice
                try {
                    choice = sc.nextInt();
                }
                catch (InputMismatchException e) {
                    System.out.println("������� �����");
                }

                // ���������� �������� switch-case ��� ���������� ������ �������� � ����������� �� ������ ������������
                switch (choice) {
                    case 1:
                        // ��������� ����� ������� � ���� ������
                        System.out.println("������� ������ ��� ������ ��������:");
                        // ��������� ������ � ���������� � ������� ������ next � ������� ������ DataElement � ����� �������
                        DataElement element = new DataElement(sc.next());
                        // �������� ����� addElement ������ Database ��� ���������� ������ �������� � ���� ������
                        db.addElement(element);
                        // ������� ��������� �� �������� ���������� ��������
                        System.out.println("������� ������� �������� � ���� ������!");
                        break;
                    case 2:
                        // �������� ������� � ���� ������ �� ��� id
                        System.out.println("������� id ��������, ������� ������ ��������:");
                        // ��������� id � ���������� � ������� ������ nextInt
                        int id = sc.nextInt();
                        System.out.println("������� ����� ������ ��� ����� ��������:");
                        // ��������� ������ � ���������� � ������� ������ next � ������� ������ DataElement � ����� �������
                        element = new DataElement(sc.next());
                        // �������� ����� updateElement ������ Database ��� ��������� �������� � ���� ������ �� ��� id
                        db.updateElement(id, element);
                        // ������� ��������� �� �������� ��������� ��������
                        System.out.println("������� ������� ������� � ���� ������!");
                        break;
                    case 3:
                        // ������� ������� �� ���� ������ �� ��� id
                        System.out.println("������� id ��������, ������� ������ �������:");
                        // ��������� id � ���������� � ������� ������ nextInt
                        id = sc.nextInt();
                        // �������� ����� deleteElement ������ Database ��� �������� �������� �� ���� ������ �� ��� id
                        db.deleteElement(id);
                        // ������� ��������� �� �������� �������� ��������
                        System.out.println("������� ������� ������ �� ���� ������!");
                        break;
                    case 4:
                        // ���������� ������ ���� ��������� �� ���� ������
                        System.out.println("������ ���� ��������� �� ���� ������:");
                        // �������� ����� displayElements ������ Database ��� ����������� ������ ���� ��������� �� ���� ������
                        db.displayElements();
                        break;
                    case 5:
                        db.deleteTable();
                        System.out.println("������� ������� �������");
                        break;
                    default:
                        // ������� ��������� �� ������ ��� �������� ������
                        System.out.println("�������� �����! ����������, �������� ���� �� ��������� ��������:");
                        System.out.println("1 - �������� ����� ������� � ���� ������");
                        System.out.println("2 - �������� ������� � ���� ������ �� ��� id");
                        System.out.println("3 - ������� ������� �� ���� ������ �� ��� id");
                        System.out.println("4 - ���������� ������ ���� ��������� �� ���� ������");
                        System.out.println("5 - ����� �� ����������");
                }
            } while (choice != 0); // ��������� ���� �� ��� ���, ���� ������������ �� ������� 5 - ����� �� ����������

            // ��������� ������ Scanner
            sc.close();
            // ��������� ������ Database
            db.close();

        } catch (SQLException | ClassNotFoundException e) {
            // ������������ ����������, ��������� � �������� ���� ������
            e.printStackTrace();
        }
    }
}
