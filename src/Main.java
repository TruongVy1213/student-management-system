import models.Student;
import services.StudentService;
import database.DatabaseConnection;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Khởi tạo CSDL
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTable();

        // Thêm dữ liệu mẫu (chỉ khi database rỗng)
        if (StudentService.getAllStudents().isEmpty()) {
            insertSampleData();
        }

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("\nChọn chức năng (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    deleteStudent();
                    break;
                case "3":
                    updateStudent();
                    break;
                case "4":
                    viewAllStudents();
                    break;
                case "5":
                    viewByClass();
                    break;
                case "6":
                    viewByMajor();
                    break;
                case "7":
                    viewByGpa();
                    break;
                case "8":
                    viewByMonth();
                    break;
                case "0":
                    running = false;
                    System.out.println("Tạm biệt!");
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          HỆ THỐNG QUẢN LÝ SINH VIÊN");
        System.out.println("=".repeat(60));
        System.out.println("1. Thêm sinh viên");
        System.out.println("2. Xóa sinh viên");
        System.out.println("3. Sửa thông tin sinh viên");
        System.out.println("4. Xem tất cả sinh viên");
        System.out.println("5. Xem sinh viên theo lớp");
        System.out.println("6. Xem sinh viên theo ngành");
        System.out.println("7. Xem sinh viên theo điểm GPA (từ cao đến thấp)");
        System.out.println("8. Xem sinh viên sinh vào tháng");
        System.out.println("0. Thoát");
        System.out.println("=".repeat(60));
    }

    private static void addStudent() {
        System.out.println("\n--- THÊM SINH VIÊN MỚI ---");
        
        // Nhập mã sinh viên
        String studentId;
        while (true) {
            System.out.print("Mã sinh viên (455105xxxx hoặc 455109xxxx): ");
            studentId = scanner.nextLine().trim();
            
            if (!ValidationUtils.isValidStudentId(studentId)) {
                System.out.println("❌ Mã sinh viên không hợp lệ!");
                continue;
            }
            
            if (StudentService.studentExists(studentId)) {
                System.out.println("❌ Mã sinh viên đã tồn tại!");
                continue;
            }
            break;
        }

        // Nhập họ tên
        String name;
        while (true) {
            System.out.print("Họ tên: ");
            name = scanner.nextLine().trim();
            name = ValidationUtils.normalizeName(name);
            
            if (name == null) {
                System.out.println("❌ Họ tên không được để trống!");
                continue;
            }
            break;
        }

        // Nhập ngày sinh
        LocalDate dateOfBirth;
        while (true) {
            System.out.print("Ngày sinh (dd/MM/yyyy): ");
            String dateStr = scanner.nextLine().trim();
            dateOfBirth = ValidationUtils.parseDate(dateStr);
            
            if (dateOfBirth == null) {
                System.out.println("❌ Định dạng ngày không hợp lệ!");
                continue;
            }
            
            if (!ValidationUtils.isValidAge(dateOfBirth)) {
                System.out.println("❌ Tuổi phải từ 15 đến 110!");
                continue;
            }
            break;
        }

        // Nhập ngành
        String major;
        while (true) {
            System.out.print("Ngành (CNTT/KTPM): ");
            major = scanner.nextLine().trim().toUpperCase();
            
            if (!ValidationUtils.isValidMajor(major)) {
                System.out.println("❌ Ngành chỉ có thể là CNTT hoặc KTPM!");
                continue;
            }
            break;
        }

        // Nhập điểm GPA
        double gpa;
        while (true) {
            System.out.print("Điểm trung bình (0.0-10.0): ");
            try {
                gpa = Double.parseDouble(scanner.nextLine().trim());
                
                if (!ValidationUtils.isValidGpa(gpa)) {
                    System.out.println("❌ Điểm phải trong khoảng [0.0, 10.0]!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập số!");
            }
        }

        // Nhập lớp sinh hoạt
        System.out.print("Lớp sinh hoạt: ");
        String className = scanner.nextLine().trim();

        // Tạo và thêm sinh viên
        Student student = new Student(studentId, name, dateOfBirth, major, gpa, className);
        
        if (StudentService.addStudent(student)) {
            System.out.println("✓ Thêm sinh viên thành công!");
        } else {
            System.out.println("❌ Thêm sinh viên thất bại!");
        }
    }

    private static void deleteStudent() {
        System.out.println("\n--- XÓA SINH VIÊN ---");
        System.out.print("Nhập mã sinh viên cần xóa: ");
        String studentId = scanner.nextLine().trim();

        if (!StudentService.studentExists(studentId)) {
            System.out.println("❌ Sinh viên không tồn tại!");
            return;
        }

        System.out.print("Bạn chắc chắn muốn xóa? (Y/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Đã hủy");
            return;
        }

        if (StudentService.deleteStudent(studentId)) {
            System.out.println("✓ Xóa sinh viên thành công!");
        } else {
            System.out.println("❌ Xóa sinh viên thất bại!");
        }
    }

    private static void updateStudent() {
        System.out.println("\n--- SỬA THÔNG TIN SINH VIÊN ---");
        System.out.print("Nhập mã sinh viên cần sửa: ");
        String studentId = scanner.nextLine().trim();

        Student student = StudentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("❌ Sinh viên không tồn tại!");
            return;
        }

        System.out.println("Để trống để giữ nguyên giá trị cũ");
        
        // Sửa họ tên
        System.out.print("Họ tên mới (" + student.getName() + "): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            newName = ValidationUtils.normalizeName(newName);
            student.setName(newName);
        }

        // Sửa ngày sinh
        System.out.print("Ngày sinh mới (dd/MM/yyyy) (" + student.getDateOfBirth() + "): ");
        String dateStr = scanner.nextLine().trim();
        if (!dateStr.isEmpty()) {
            LocalDate newDate = ValidationUtils.parseDate(dateStr);
            if (newDate != null && ValidationUtils.isValidAge(newDate)) {
                student.setDateOfBirth(newDate);
            } else {
                System.out.println("❌ Ngày sinh không hợp lệ, giữ nguyên!");
            }
        }

        // Sửa ngành
        System.out.print("Ngành mới (CNTT/KTPM) (" + student.getMajor() + "): ");
        String newMajor = scanner.nextLine().trim();
        if (!newMajor.isEmpty()) {
            newMajor = newMajor.toUpperCase();
            if (ValidationUtils.isValidMajor(newMajor)) {
                student.setMajor(newMajor);
            } else {
                System.out.println("❌ Ngành không hợp lệ, giữ nguyên!");
            }
        }

        // Sửa GPA
        System.out.print("Điểm GPA mới (0.0-10.0) (" + student.getGpa() + "): ");
        String gpaStr = scanner.nextLine().trim();
        if (!gpaStr.isEmpty()) {
            try {
                double newGpa = Double.parseDouble(gpaStr);
                if (ValidationUtils.isValidGpa(newGpa)) {
                    student.setGpa(newGpa);
                } else {
                    System.out.println("❌ Điểm không hợp lệ, giữ nguyên!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập số!");
            }
        }

        // Sửa lớp
        System.out.print("Lớp sinh hoạt mới (" + student.getClassName() + "): ");
        String newClass = scanner.nextLine().trim();
        if (!newClass.isEmpty()) {
            student.setClassName(newClass);
        }

        if (StudentService.updateStudent(student)) {
            System.out.println("✓ Sửa sinh viên thành công!");
        } else {
            System.out.println("❌ Sửa sinh viên thất bại!");
        }
    }

    private static void viewAllStudents() {
        System.out.println("\n--- DANH SÁCH TẤT CẢ SINH VIÊN ---");
        List<Student> students = StudentService.getAllStudents();
        displayStudents(students);
    }

    private static void viewByClass() {
        System.out.println("\n--- XEM SINH VIÊN THEO LỚP ---");
        System.out.print("Nhập tên lớp: ");
        String className = scanner.nextLine().trim();
        
        List<Student> students = StudentService.getStudentsByClass(className);
        if (students.isEmpty()) {
            System.out.println("❌ Không tìm thấy sinh viên nào!");
        } else {
            System.out.println("\nSinh viên lớp " + className + ":");
            displayStudents(students);
        }
    }

    private static void viewByMajor() {
        System.out.println("\n--- XEM SINH VIÊN THEO NGÀNH ---");
        System.out.print("Nhập ngành (CNTT/KTPM): ");
        String major = scanner.nextLine().trim().toUpperCase();
        
        if (!ValidationUtils.isValidMajor(major)) {
            System.out.println("❌ Ngành không hợp lệ!");
            return;
        }
        
        List<Student> students = StudentService.getStudentsByMajor(major);
        if (students.isEmpty()) {
            System.out.println("❌ Không tìm thấy sinh viên nào!");
        } else {
            System.out.println("\nSinh viên ngành " + major + ":");
            displayStudents(students);
        }
    }

    private static void viewByGpa() {
        System.out.println("\n--- DANH SÁCH SINH VIÊN SẮP XẾP THEO ĐIỂM (CAO ĐẾN THẤP) ---");
        List<Student> students = StudentService.sortByGpa();
        displayStudents(students);
    }

    private static void viewByMonth() {
        System.out.println("\n--- XEM SINH VIÊN SINH VÀO THÁNG ---");
        System.out.print("Nhập tháng (1-12): ");
        try {
            int month = Integer.parseInt(scanner.nextLine().trim());
            
            if (month < 1 || month > 12) {
                System.out.println("❌ Tháng phải từ 1 đến 12!");
                return;
            }
            
            List<Student> students = StudentService.getStudentsByMonth(month);
            if (students.isEmpty()) {
                System.out.println("❌ Không tìm thấy sinh viên nào!");
            } else {
                System.out.println("\nSinh viên sinh vào tháng " + month + ":");
                displayStudents(students);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Vui lòng nhập số!");
        }
    }

    private static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("Không có dữ liệu");
            return;
        }

        System.out.println("\n" + "=".repeat(130));
        System.out.println(String.format("| %-10s | %-25s | %-10s | %-5s | %6s | %-15s | %-10s |",
                "Mã SV", "Họ Tên", "Ngày Sinh", "Major", "GPA", "Lớp", "Thông Tin"));
        System.out.println("=".repeat(130));
        
        for (Student student : students) {
            System.out.println(student);
        }
        
        System.out.println("=".repeat(130));
        System.out.println("Tổng cộng: " + students.size() + " sinh viên");
    }

    private static void insertSampleData() {
        System.out.println("Đang thêm dữ liệu mẫu...");
        
        Student[] sampleStudents = {
            new Student("4551050001", "Nguyễn Văn An", LocalDate.of(2005, 5, 15), "CNTT", 8.5, "K21.1"),
            new Student("4551050002", "Trương Mỹ Linh", LocalDate.of(2004, 3, 20), "CNTT", 7.8, "K21.1"),
            new Student("4551050003", "Phan Hữu Long", LocalDate.of(2005, 7, 10), "CNTT", 8.2, "K21.2"),
            new Student("4551050004", "Lê Thị Hương", LocalDate.of(2006, 1, 25), "CNTT", 9.0, "K21.2"),
            new Student("4551050005", "Vũ Anh Tuấn", LocalDate.of(2005, 9, 5), "CNTT", 7.5, "K21.3"),
            new Student("4551090006", "Đào Thị Mỹ", LocalDate.of(2004, 11, 12), "KTPM", 8.8, "K21.1"),
            new Student("4551090007", "Hoàng Minh Hiếu", LocalDate.of(2005, 4, 8), "KTPM", 8.0, "K21.2"),
            new Student("4551090008", "Cao Văn Hùng", LocalDate.of(2005, 6, 22), "KTPM", 7.2, "K21.3"),
            new Student("4551090009", "Tạ Quỳnh Anh", LocalDate.of(2006, 2, 14), "KTPM", 9.2, "K21.1"),
            new Student("4551090010", "Bùi Sơn Tùng", LocalDate.of(2005, 8, 30), "KTPM", 8.5, "K21.2")
        };
        
        int count = 0;
        for (Student student : sampleStudents) {
            if (StudentService.addStudent(student)) {
                count++;
            }
        }
        
        System.out.println("✓ Đã thêm " + count + "/10 sinh viên mẫu");
    }
}