export default function ErrorMessage({ error, title = 'Request failed' }) {
  if (!error) {
    return null;
  }

  return (
    <div className="error-message" role="alert">
      <strong>{title}</strong>
      <span>{error.message || 'An unexpected error occurred.'}</span>
    </div>
  );
}
