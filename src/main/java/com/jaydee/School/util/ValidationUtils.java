package com.jaydee.School.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$");
    private static final Pattern ACADEMIC_YEAR_PATTERN = Pattern.compile("^\\d{4}-\\d{4}$");
    
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && 
               password.length() >= 8 && 
               PASSWORD_PATTERN.matcher(password).matches();
    }
    
    public static boolean isValidAcademicYear(String academicYear) {
        return academicYear != null && ACADEMIC_YEAR_PATTERN.matcher(academicYear).matches();
    }
    
    public static boolean isValidDateOfBirth(LocalDate dob) {
        if (dob == null) return false;
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(100);
        return dob.isAfter(minDate) && dob.isBefore(today);
    }
    
    public static boolean isValidRelationship(String relationship) {
        if (relationship == null) return false;
        String[] validRelationships = {"Father", "Mother", "Guardian", "Sibling", "Other"};
        for (String valid : validRelationships) {
            if (valid.equalsIgnoreCase(relationship)) return true;
        }
        return false;
    }
    
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return null;
        // Remove all non-digit characters
        String digits = phoneNumber.replaceAll("\\D", "");
        // Add + prefix if not present
        return digits.startsWith("+") ? digits : "+" + digits;
    }
    
    public static String formatAcademicYear(String academicYear) {
        if (academicYear == null) return null;
        try {
            // Try to parse and format the academic year
            String[] years = academicYear.split("-");
            if (years.length != 2) return null;
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            if (endYear != startYear + 1) return null;
            return String.format("%d-%d", startYear, endYear);
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 