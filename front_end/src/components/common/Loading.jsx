export default function Loading({ message = 'Loading...' }) {
  return (
    <div className="loading" role="status" aria-live="polite">
      <span className="loading__spinner" />
      <span>{message}</span>
    </div>
  );
}
