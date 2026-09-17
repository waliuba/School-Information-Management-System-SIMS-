export default function Input({ id, label, error, ...props }) {
  return (
    <label className="field" htmlFor={id}>
      <span>{label}</span>
      <input id={id} className="input" {...props} />
      {error ? <small className="field__error">{error}</small> : null}
    </label>
  );
}
