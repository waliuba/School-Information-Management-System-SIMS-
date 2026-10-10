-- One-time repair for the existing SIMS_DB.ATTENDANCE table whose legacy
-- shape stores STUDENT_NAME instead of the STUDENT_ID expected by the app.
-- The legacy rows have no student identity and were approved for disposal.
-- Run while connected as SIMS_DB, after confirming STUDENTS.STUDENT_ID exists.

DECLARE
    attendance_columns NUMBER;
    student_id_columns NUMBER;
BEGIN
    IF USER <> 'SIMS_DB' THEN
        RAISE_APPLICATION_ERROR(-20001, 'Connect as SIMS_DB before running this repair.');
    END IF;

    SELECT COUNT(*) INTO attendance_columns
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'ATTENDANCE'
      AND COLUMN_NAME IN ('ATTENDANCE_ID', 'ATTENDANCE_DATE', 'STATUS', 'REMARKS', 'STUDENT_NAME');

    SELECT COUNT(*) INTO student_id_columns
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'ATTENDANCE'
      AND COLUMN_NAME = 'STUDENT_ID';

    IF attendance_columns <> 5 OR student_id_columns <> 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'ATTENDANCE does not have the expected legacy column layout.');
    END IF;

    SELECT COUNT(*) INTO student_id_columns
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'STUDENTS'
      AND COLUMN_NAME = 'STUDENT_ID';

    IF student_id_columns <> 1 THEN
        RAISE_APPLICATION_ERROR(-20003, 'STUDENTS.STUDENT_ID was not found in SIMS_DB.');
    END IF;
END;
/

DELETE FROM attendance;
COMMIT;

ALTER TABLE attendance
    ADD (student_id NUMBER(19));

ALTER TABLE attendance
    MODIFY (student_id NOT NULL);

ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_student
    FOREIGN KEY (student_id) REFERENCES students(student_id);
