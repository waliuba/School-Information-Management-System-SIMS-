import Loading from '../common/Loading.jsx';

export default function LoadingState({ message = 'Loading dashboard data...' }) {
  return (
    <div className="dashboard-state-panel">
      <Loading message={message} />
    </div>
  );
}
