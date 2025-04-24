-- Add role column to users table if not exists
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20);

-- Create exam_results table
CREATE TABLE IF NOT EXISTS exam_results (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES users(id),
    teacher_id BIGINT REFERENCES users(id),
    class_id BIGINT REFERENCES classes(id),
    subject VARCHAR(100) NOT NULL,
    score DECIMAL(5,2),
    remarks TEXT,
    exam_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create performance_reports table
CREATE TABLE IF NOT EXISTS performance_reports (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES users(id),
    teacher_id BIGINT REFERENCES users(id),
    class_id BIGINT REFERENCES classes(id),
    academic_term VARCHAR(50) NOT NULL,
    average_score DECIMAL(5,2),
    overall_grade VARCHAR(10),
    teacher_comments TEXT,
    strengths TEXT,
    areas_for_improvement TEXT,
    report_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_exam_results_student ON exam_results(student_id);
CREATE INDEX IF NOT EXISTS idx_exam_results_teacher ON exam_results(teacher_id);
CREATE INDEX IF NOT EXISTS idx_exam_results_class ON exam_results(class_id);
CREATE INDEX IF NOT EXISTS idx_performance_reports_student ON performance_reports(student_id);
CREATE INDEX IF NOT EXISTS idx_performance_reports_teacher ON performance_reports(teacher_id);
CREATE INDEX IF NOT EXISTS idx_performance_reports_class ON performance_reports(class_id); 