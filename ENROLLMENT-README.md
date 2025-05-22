# Secure User Enrollment Guide for School Management System

## Overview

This guide explains the new secure user enrollment workflow for the School Management System. The system now follows a more secure procedure that aligns with real-world school enrollment processes:

1. In-person enrollment: Parents/students physically visit the school and fill out forms
2. Admin creation: Administrators create users in the system with appropriate roles (Student, Parent, Teacher)
3. Credential distribution: System sends login credentials to users via email or prints them
4. First login: Users log in with credentials and must change passwords on first login

## Enrollment Process for Administrators

### Creating New User Accounts

#### Creating a Student Account

1. Navigate to the Admin Dashboard
2. Select "Enrollment" → "New Student"
3. Fill in the required fields:
   - First Name & Last Name
   - Email address
   - Grade/Class assignment
   - Student ID
   - Choose delivery method (Email or Print)
4. Submit the form to create the account

#### Creating a Parent Account

1. Navigate to the Admin Dashboard
2. Select "Enrollment" → "New Parent"
3. Fill in the required fields:
   - First Name & Last Name
   - Email address
   - Link to student(s) (optional)
   - Choose delivery method (Email or Print)
4. Submit the form to create the account

#### Creating a Teacher Account

1. Navigate to the Admin Dashboard
2. Select "Enrollment" → "New Teacher"
3. Fill in the required fields:
   - First Name & Last Name
   - Email address
   - Specialization/Subject areas
   - Employee ID
   - Choose delivery method (Email or Print)
4. Submit the form to create the account

### Delivering Credentials

#### Email Delivery

When creating a user with email delivery selected:

- The system automatically generates a secure temporary password
- An email is sent to the provided email address with:
  - Username (email address)
  - Temporary password
  - Link to the login page
  - Instructions to change the password upon first login

#### Printed Credentials

When creating a user with print delivery selected:

- The system generates a secure temporary password
- Click on "Print Credentials" from the user details page
- A printer-friendly page with the credentials is displayed
- Print this page and physically deliver it to the user

## User First Login Process

When users receive their credentials:

1. They visit the School Management System login page
2. Enter their username and temporary password
3. The system detects it's their first login and redirects them to a password change page
4. User must set a new strong password following the security requirements:
   - At least 6 characters
   - Must include one uppercase letter
   - Must include one lowercase letter
   - Must include one number
   - Must include one special character
5. After setting the new password, users gain access to their respective dashboards

## Security Benefits

This new workflow provides several security improvements:

- Eliminates public self-registration, preventing unauthorized access
- Ensures proper role assignment by administrators
- Enforces password change on first login
- Offers multiple credential delivery options
- Maintains a record of user creation and credential distribution
- Better aligns with real-world school enrollment processes

## API Endpoints for Developers

The enrollment APIs are accessible only to administrators:

- `POST /api/enrollment/users` - General user creation
- `POST /api/enrollment/students` - Student account creation
- `POST /api/enrollment/teachers` - Teacher account creation
- `POST /api/enrollment/parents` - Parent account creation
- `POST /api/enrollment/password-change/{userId}` - First login password change
- `POST /api/enrollment/password-change-required/{userId}` - Check if password change is required
- `POST /api/enrollment/print-credentials/{userId}` - Generate printable credentials
