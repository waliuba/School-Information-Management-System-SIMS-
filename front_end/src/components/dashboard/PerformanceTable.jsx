import EmptyState from './EmptyState.jsx';

export default function PerformanceTable({
  columns,
  rows,
  emptyTitle = 'No performance data available yet',
  emptyMessage = 'Performance data will appear once examination results are available.',
}) {
  if (!rows?.length) {
    return <EmptyState title={emptyTitle} message={emptyMessage} />;
  }

  return (
    <div className="performance-table-wrap">
      <table className="performance-table">
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key}>{column.label}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, rowIndex) => (
            <tr key={row.id || row.studentId || row.classId || rowIndex}>
              {columns.map((column) => (
                <td key={column.key} data-label={column.label}>
                  {column.render ? column.render(row, rowIndex) : row[column.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
