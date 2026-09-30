import PerformanceTable from './PerformanceTable.jsx';

const columns = [
  { key: 'studentName', label: 'Student' },
  { key: 'admissionNo', label: 'Admission No.' },
  { key: 'className', label: 'Class/Stream' },
  { key: 'averageScore', label: 'Average Score' },
  { key: 'grade', label: 'Grade' },
  { key: 'performance', label: 'Performance' },
];

export default function StudentPerformanceTable({ rows }) {
  return (
    <PerformanceTable
      columns={columns}
      rows={rows}
      emptyTitle="No student performance data available yet"
      emptyMessage="Student averages, grades, and support indicators will appear once examination results are available."
    />
  );
}
