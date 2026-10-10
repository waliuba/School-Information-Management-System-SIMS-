import { useMemo, useState } from 'react';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Modal from '../../components/common/Modal.jsx';
import Table from '../../components/common/Table.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useApi } from '../../hooks/useApi.js';
import { getAttendance, getMyAttendance } from '../../services/api/resourcesApi.js';
import { useAuth } from '../../hooks/useAuth.js';

function formatDate(value) {
  if (!value) return '—';
  return String(value);
}

export default function AttendancePage() {
  const { user } = useAuth();
  const role = user?.role || user?.role_name || user?.roleName;
  const { data, error, isLoading } = useApi(role === 'STUDENT' ? getMyAttendance : getAttendance);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const records = Array.isArray(data) ? data : [];

  const students = useMemo(() => {
    const grouped = new Map();
    records.forEach((record) => {
      const key = record.studentId ?? record.studentName;
      if (key == null) return;
      if (!grouped.has(String(key))) {
        grouped.set(String(key), {
          studentId: record.studentId,
          studentName: record.studentName || `Student ${record.studentId}`,
          attendance: [],
        });
      }
      grouped.get(String(key)).attendance.push(record);
    });
    return [...grouped.values()].map((student) => ({
      ...student,
      attendance: student.attendance.sort((a, b) =>
        String(b.attendanceDate || '').localeCompare(String(a.attendanceDate || ''))),
    })).sort((a, b) => a.studentName.localeCompare(b.studentName));
  }, [records]);

  const summaryColumns = [
    { key: 'studentName', label: 'Student' },
    { key: 'daysRecorded', label: 'Days recorded', render: (student) => student.attendance.length },
    { key: 'presentDays', label: 'Days present', render: (student) => student.attendance.filter((item) => item.status?.toUpperCase() === 'PRESENT').length },
    { key: 'latestDate', label: 'Most recent date', render: (student) => formatDate(student.attendance[0]?.attendanceDate) },
    { key: 'actions', label: 'Actions', render: (student) => <button className="attendance-view" type="button" onClick={() => setSelectedStudent(student)}>View</button> },
  ];
  const detailColumns = [
    { key: 'attendanceDate', label: 'Date', render: (record) => formatDate(record.attendanceDate) },
    { key: 'status', label: 'Status' },
    { key: 'remarks', label: 'Remarks', render: (record) => record.remarks || '—' },
  ];

  return (
    <PageContainer title="Attendance" description={role === 'STUDENT' ? 'Your attendance history.' : 'Attendance summarized by student. View a student to see every recorded day.'}>
      <ErrorMessage error={error} />
      {!error ? <Table columns={role === 'STUDENT' ? detailColumns : summaryColumns} rows={role === 'STUDENT' ? records : students} isLoading={isLoading} emptyMessage="No attendance records were returned by the backend." /> : null}
      <Modal title={selectedStudent ? `${selectedStudent.studentName} attendance` : 'Student attendance'} isOpen={Boolean(selectedStudent)} onClose={() => setSelectedStudent(null)}>
        {selectedStudent ? <Table columns={detailColumns} rows={selectedStudent.attendance} isLoading={false} emptyMessage="No attendance days recorded." /> : null}
      </Modal>
    </PageContainer>
  );
}
