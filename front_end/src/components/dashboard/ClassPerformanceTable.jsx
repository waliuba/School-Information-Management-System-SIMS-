import PerformanceTable from './PerformanceTable.jsx';

const columns = [
  { key: 'rank', label: 'Rank' },
  { key: 'className', label: 'Class/Stream' },
  { key: 'students', label: 'Students' },
  { key: 'averageScore', label: 'Average Score' },
  { key: 'passRate', label: 'Pass Rate' },
  { key: 'topScore', label: 'Top Score' },
];

export default function ClassPerformanceTable({ rows }) {
  return (
    <PerformanceTable
      columns={columns}
      rows={rows}
      emptyTitle="No class performance data available yet"
      emptyMessage="Class rankings will appear once examination results are available."
    />
  );
}
