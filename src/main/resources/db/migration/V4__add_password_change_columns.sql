-- Add password_change_required and last_password_change_date columns to users table

ALTER TABLE users 
ADD COLUMN password_change_required BOOLEAN DEFAULT FALSE,
ADD COLUMN last_password_change_date TIMESTAMP;

-- Update existing users to not require password change
UPDATE users SET password_change_required = FALSE;
