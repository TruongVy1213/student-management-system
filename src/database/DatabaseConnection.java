package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Thay đổi password của MySQL nếu cần

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
            return null;
        }
    }

    public static void createDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", USER, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS student_management");
            statement.close();
            conn.close();
            System.out.println("✓ Database đã được tạo hoặc đã tồn tại");
        } catch (SQLException e) {
            System.err.println("Lỗi tạo database: " + e.getMessage());
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id VARCHAR(10) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "date_of_birth DATE NOT NULL," +
                "major VARCHAR(10) NOT NULL," +
                "gpa DECIMAL(3,2) NOT NULL," +
                "class_name VARCHAR(50) NOT NULL" +
                ")";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("✓ Bảng students đã được tạo hoặc đã tồn tại");
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối CSDL");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}