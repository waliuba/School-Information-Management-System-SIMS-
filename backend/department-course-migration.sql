-- Run once against an existing Oracle database before starting the updated API.
ALTER TABLE courses ADD department_id NUMBER(19);

ALTER TABLE courses ADD CONSTRAINT fk_courses_department
    FOREIGN KEY (department_id) REFERENCES departments(department_id);

CREATE INDEX idx_courses_department_id ON courses(department_id);

-- Assign every existing course to its department before creating enrollments.
-- Example (replace IDs with the values in your departments table):
-- UPDATE courses SET department_id = 1 WHERE course_code IN ('BUS101', 'BUS102');
-- UPDATE courses SET department_id = 2 WHERE course_code IN ('ICT101', 'ICT102');
-- COMMIT;
