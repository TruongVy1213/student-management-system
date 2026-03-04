package services;

import models.Student;
import database.DatabaseConnection;
import utils.ValidationUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    
    public static boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, date_of_birth, major, gpa, class_name) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(4, student.getMajor());
            pstmt.setDouble(5, student.getGpa());
            pstmt.setString(6, student.getClassName());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sinh viên: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa sinh viên: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, date_of_birth = ?, major = ?, gpa = ?, class_name = ? WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(3, student.getMajor());
            pstmt.setDouble(4, student.getGpa());
            pstmt.setString(5, student.getClassName());
            pstmt.setString(6, student.getStudentId());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi sửa sinh viên: " + e.getMessage());
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("major"),
                    rs.getDouble("gpa"),
                    rs.getString("class_name")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy dữ liệu: " + e.getMessage());
        }
        return students;
    }

    public static Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("major"),
                    rs.getDouble("gpa"),
                    rs.getString("class_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy sinh viên: " + e.getMessage());
        }
        return null;
    }

    public static List<Student> getStudentsByClass(String className) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE class_name = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, className);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("major"),
                    rs.getDouble("gpa"),
                    rs.getString("class_name")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy sinh viên theo lớp: " + e.getMessage());
        }
        return students;
    }

    public static List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, major);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("major"),
                    rs.getDouble("gpa"),
                    rs.getString("class_name")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy sinh viên theo ngành: " + e.getMessage());
        }
        return students;
    }

    public static List<Student> getStudentsByMonth(int month) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE MONTH(date_of_birth) = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, month);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("major"),
                    rs.getDouble("gpa"),
                    rs.getString("class_name")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy sinh viên theo tháng: " + e.getMessage());
        }
        return students;
    }

    public static List<Student> sortByGpa() {
        List<Student> students = getAllStudents();
        students.sort((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()));
        return students;
    }

    public static boolean studentExists(String studentId) {
        return getStudentById(studentId) != null;
    }
}