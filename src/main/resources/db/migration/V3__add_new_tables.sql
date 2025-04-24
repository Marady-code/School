-- Create parents table
CREATE TABLE IF NOT EXISTS parents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    relationship VARCHAR(50),
    occupation VARCHAR(100),
    address TEXT,
    emergency_contact VARCHAR(20)
);

-- Create parent_student table for many-to-many relationship
CREATE TABLE IF NOT EXISTS parent_student (
    parent_id BIGINT REFERENCES parents(id),
    student_id BIGINT REFERENCES students(id),
    PRIMARY KEY (parent_id, student_id)
);

-- Create subjects table
CREATE TABLE IF NOT EXISTS subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    description TEXT,
    credits INTEGER,
    academic_year VARCHAR(20),
    term VARCHAR(20)
);

-- Create time_tables table
CREATE TABLE IF NOT EXISTS time_tables (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES classes(id),
    subject_id BIGINT REFERENCES subjects(id),
    teacher_id BIGINT REFERENCES teachers(id),
    day_of_week VARCHAR(20),
    start_time TIME,
    end_time TIME,
    room_number VARCHAR(20),
    academic_year VARCHAR(20),
    term VARCHAR(20)
);

-- Create leaves table
CREATE TABLE IF NOT EXISTS leaves (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT REFERENCES teachers(id),
    start_date DATE,
    end_date DATE,
    reason TEXT,
    status VARCHAR(20),
    type VARCHAR(20),
    approved_by VARCHAR(100),
    approved_date DATE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_parents_user ON parents(user_id);
CREATE INDEX IF NOT EXISTS idx_parent_student_parent ON parent_student(parent_id);
CREATE INDEX IF NOT EXISTS idx_parent_student_student ON parent_student(student_id);
CREATE INDEX IF NOT EXISTS idx_subjects_academic_year_term ON subjects(academic_year, term);
CREATE INDEX IF NOT EXISTS idx_time_tables_class ON time_tables(class_id);
CREATE INDEX IF NOT EXISTS idx_time_tables_teacher ON time_tables(teacher_id);
CREATE INDEX IF NOT EXISTS idx_time_tables_subject ON time_tables(subject_id);
CREATE INDEX IF NOT EXISTS idx_leaves_teacher ON leaves(teacher_id);
CREATE INDEX IF NOT EXISTS idx_leaves_status ON leaves(status); 