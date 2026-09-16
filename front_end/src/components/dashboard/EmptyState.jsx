export default function EmptyState({
  title = 'No data available',
  message = 'Data will appear here once it is available from the backend.',
}) {
  return (
    <div className="dashboard-empty-state">
      <strong>{title}</strong>
      <p>{message}</p>
    </div>
  );
}
