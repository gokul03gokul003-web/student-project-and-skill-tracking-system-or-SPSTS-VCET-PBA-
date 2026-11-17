package com.vcet.tracking.util;

import java.util.regex.Pattern;
import com.vcet.tracking.exception.InvalidDataException;

/**
 * Utility class providing validation routines for user inputs.
 * Demonstrates Unit 3: Exception Handling, Unit 4: String Handling and Regex.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern REGNO_PATTERN = Pattern.compile("^[A-Za-z0-9]{5,15}$");

    public static void validateRegisterNo(String regNo) throws InvalidDataException {
        if (regNo == null || !REGNO_PATTERN.matcher(regNo.trim()).matches()) {
            throw new InvalidDataException("Invalid Register Number. Must be 5 to 15 alphanumeric characters (e.g., 732925ITR032).");
        }
    }

    public static void validateEmail(String email) throws InvalidDataException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidDataException("Invalid Email format. Example: gokul.s@vcet.ac.in");
        }
    }

    public static void validateNonEmpty(String value, String fieldName) throws InvalidDataException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " cannot be blank or empty.");
        }
    }

    public static void validateRubricMarks(double marks, double maxAllowed, String componentName) throws InvalidDataException {
        if (marks < 0.0 || marks > maxAllowed) {
            throw new InvalidDataException(String.format("%s marks must be between 0.0 and %.1f. Provided: %.2f", 
                componentName, maxAllowed, marks));
        }
    }

    public static void validateProficiency(String level) throws InvalidDataException {
        if (!"Beginner".equalsIgnoreCase(level) && 
            !"Intermediate".equalsIgnoreCase(level) && 
            !"Advanced".equalsIgnoreCase(level)) {
            throw new InvalidDataException("Proficiency level must be one of: Beginner, Intermediate, Advanced.");
        }
    }
}
