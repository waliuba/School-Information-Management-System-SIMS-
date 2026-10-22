export default function Input({ id, label, error, ...props }) {
  const errorId = error && id ? `${id}-error` : undefined;
  const describedBy = [props['aria-describedby'], errorId].filter(Boolean).join(' ') || undefined;

  return (
    <label className="field" htmlFor={id}>
      <span>{label}</span>
      <input
        id={id}
        className="input"
        {...props}
        aria-invalid={error ? true : props['aria-invalid']}
        aria-describedby={describedBy}
      />
      {error ? <small id={errorId} className="field__error" role="alert">{error}</small> : null}
    </label>
  );
}
