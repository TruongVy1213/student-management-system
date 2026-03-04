package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {
    
    public static String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String[] parts = name.trim().split("\\s+");
        StringBuilder normalized = new StringBuilder();
        
        for (String part : parts) {
            if (part.length() > 0) {
                normalized.append(Character.toUpperCase(part.charAt(0)))
                         .append(part.substring(1).toLowerCase())
                         .append(" ");
            }
        }
        return normalized.toString().trim();
    }

    public static boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.length() != 10) {
            return false;
        }
        return studentId.matches("^(455105|455109)\\d{4}$");
    }

    public static boolean isValidMajor(String major) {
        return major != null && (major.equals("CNTT") || major.equals("KTPM"));
    }

    public static boolean isValidDate(LocalDate date) {
        if (date == null) return false;
        LocalDate today = LocalDate.now();
        return date.isBefore(today);
    }

    public static boolean isValidAge(LocalDate dateOfBirth) {
        if (!isValidDate(dateOfBirth)) return false;
        
        LocalDate today = LocalDate.now();
        int age = today.getYear() - dateOfBirth.getYear();
        
        if (today.getMonthValue() < dateOfBirth.getMonthValue() ||
            (today.getMonthValue() == dateOfBirth.getMonthValue() && 
             today.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        
        return age >= 15 && age <= 110;
    }

    public static boolean isValidGpa(double gpa) {
        return gpa >= 0.0 && gpa <= 10.0;
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}