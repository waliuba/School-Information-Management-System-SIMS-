-- Align the database status check with MarkAttendancePage's supported values.
-- Run once while connected as SIMS_DB. Existing rows are preserved.

DECLARE
    check_constraint_count NUMBER;
    unsupported_status_count NUMBER;
BEGIN
    IF USER <> 'SIMS_DB' THEN
        RAISE_APPLICATION_ERROR(-20011, 'Connect as SIMS_DB before running this migration.');
    END IF;

    SELECT COUNT(*) INTO check_constraint_count
    FROM USER_CONSTRAINTS
    WHERE TABLE_NAME = 'ATTENDANCE'
      AND CONSTRAINT_NAME = 'CHK_ATTENDANCE_STATUS'
      AND CONSTRAINT_TYPE = 'C';

    IF check_constraint_count <> 1 THEN
        RAISE_APPLICATION_ERROR(-20012, 'CHK_ATTENDANCE_STATUS was not found on ATTENDANCE.');
    END IF;

    SELECT COUNT(*) INTO unsupported_status_count
    FROM ATTENDANCE
    WHERE STATUS IS NULL
       OR UPPER(TRIM(STATUS)) NOT IN ('PRESENT', 'ABSENT', 'LATE');

    IF unsupported_status_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20013, 'ATTENDANCE contains statuses outside PRESENT, ABSENT, and LATE.');
    END IF;
END;
/

ALTER TABLE ATTENDANCE
    DROP CONSTRAINT CHK_ATTENDANCE_STATUS;

ALTER TABLE ATTENDANCE
    ADD CONSTRAINT CHK_ATTENDANCE_STATUS
    CHECK (UPPER(TRIM(STATUS)) IN ('PRESENT', 'ABSENT', 'LATE'));
