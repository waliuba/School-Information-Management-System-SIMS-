import { useEffect, useMemo, useState } from 'react';
import Button from '../../components/common/Button.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { createAttendance, getAttendanceByDate, getStudents, updateAttendance } from '../../services/api/resourcesApi.js';

const STATUS_OPTIONS = ['Present', 'Absent', 'Late'];

function schoolToday() {
  const parts = new Intl.DateTimeFormat('en-CA', {
    timeZone: 'Africa/Nairobi', year: 'numeric', month: '2-digit', day: '2-digit',
  }).formatToParts(new Date());
  const values = Object.fromEntries(parts.map(({ type, value }) => [type, value]));
  return `${values.year}-${values.month}-${values.day}`;
}

function fullName(student) {
  return [student.firstName, student.middleName, student.lastName].filter(Boolean).join(' ') || `Student ${student.studentId}`;
}

export default function MarkAttendancePage() {
  const today = useMemo(schoolToday, []);
  const [students, setStudents] = useState([]);
  const [records, setRecords] = useState([]);
  const [studentId, setStudentId] = useState('');
  const [attendanceDate, setAttendanceDate] = useState(today);
  const [status, setStatus] = useState('Present');
  const [remarks, setRemarks] = useState('');
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState(false);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [saveError, setSaveError] = useState('');

  async function loadFormData() {
    setLoading(true);
    setLoadError(false);
    try {
      const studentRows = await getStudents();
      setStudents(studentRows);
      try {
        setRecords(await getAttendanceByDate(attendanceDate));
      } catch {
        // The student picker remains usable; attendance lookup is retried on date changes.
        setRecords([]);
      }
    } catch {
      setLoadError(true);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { loadFormData(); }, []);

  useEffect(() => {
    if (!attendanceDate || attendanceDate > today) return;
    getAttendanceByDate(attendanceDate).then(setRecords).catch(() => setRecords([]));
  }, [attendanceDate, today]);

  const existing = records.find((record) => String(record.studentId) === String(studentId) && record.attendanceDate === attendanceDate);

  useEffect(() => {
    if (existing) {
      setStatus(existing.status);
      setRemarks(existing.remarks || '');
    } else {
      setStatus('Present');
      setRemarks('');
    }
  }, [studentId, attendanceDate, existing?.attendanceId]);

  async function handleSubmit(event) {
    event.preventDefault();
    if (!studentId || !attendanceDate || attendanceDate > today || !status || remarks.length > 255) return;
    setSaving(true);
    setSaveError('');
    setMessage('');
    const payload = { studentId: Number(studentId), attendanceDate, status, remarks: remarks.trim() || null };
    try {
      const saved = existing
        ? await updateAttendance(existing.attendanceId, payload)
        : await createAttendance(payload);
      setRecords((current) => [saved, ...current.filter((row) => row.attendanceId !== saved.attendanceId)]);
      setMessage(`Attendance ${existing ? 'updated' : 'saved'} successfully for ${fullName(students.find((student) => String(student.studentId) === String(studentId)) || {})}.`);
      setRemarks('');
    } catch (error) {
      const friendly = error?.status === 403
        ? 'You do not have permission to mark attendance.'
        : error?.status === 409
          ? 'An attendance record already exists for this student and date. Reload the form and try again.'
          : 'Attendance could not be saved. Please check the details and try again.';
      setSaveError(friendly);
    } finally {
      setSaving(false);
    }
  }

  return <PageContainer title="Mark Attendance" description="Record a student's attendance for a selected date.">
    {loading ? <p role="status">Loading students…</p> : loadError ? <div className="attendance-form-notice" role="alert"><p>Unable to load students. Please try again.</p><Button type="button" variant="secondary" onClick={loadFormData}>Retry</Button></div> : students.length === 0 ? <p className="attendance-form-notice">No students are available to mark.</p> : <form className="attendance-mark-form" onSubmit={handleSubmit}>
      <label className="field" htmlFor="attendance-student"><span>Student</span><select id="attendance-student" className="input" value={studentId} onChange={(event) => { setStudentId(event.target.value); setMessage(''); }} required>
        <option value="">Select a student</option>{students.map((student) => <option key={student.studentId} value={student.studentId}>{fullName(student)}{student.admissionNo ? ` · ${student.admissionNo}` : ''}</option>)}
      </select></label>
      <label className="field" htmlFor="attendance-date"><span>Attendance date</span><input id="attendance-date" className="input" type="date" value={attendanceDate} max={today} onChange={(event) => { setAttendanceDate(event.target.value); setMessage(''); }} required /></label>
      <label className="field" htmlFor="attendance-status"><span>Status</span><select id="attendance-status" className="input" value={status} onChange={(event) => setStatus(event.target.value)} required>{STATUS_OPTIONS.map((value) => <option key={value} value={value}>{value}</option>)}</select></label>
      <label className="field attendance-mark-form__remarks" htmlFor="attendance-remarks"><span>Remarks <small>({remarks.length}/255)</small></span><input id="attendance-remarks" className="input" type="text" value={remarks} maxLength={255} onChange={(event) => setRemarks(event.target.value)} /></label>
      {existing && <p className="attendance-form-notice" role="status">A record exists for this student and date. Saving will update it.</p>}
      {saveError && <p className="attendance-form-error" role="alert">{saveError}</p>}
      {message && <p className="attendance-form-success" role="status">{message}</p>}
      <div className="attendance-mark-form__actions"><Button type="submit" disabled={saving || !studentId || !attendanceDate || attendanceDate > today}>{saving ? 'Saving…' : existing ? 'Update attendance' : 'Save attendance'}</Button></div>
    </form>}
  </PageContainer>;
}
