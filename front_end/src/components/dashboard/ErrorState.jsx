import ErrorMessage from '../common/ErrorMessage.jsx';

export default function ErrorState({ error, title = 'Could not load dashboard data' }) {
  return (
    <div className="dashboard-state-panel">
      <ErrorMessage error={error} title={title} />
    </div>
  );
}
