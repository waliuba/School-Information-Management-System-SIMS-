export default function StatCard({ label, value, suffix = '', helper }) {
  return (
    <article className="stat-card">
      <span>{label}</span>
      <strong>{value ?? '-'}{value != null ? suffix : ''}</strong>
      {helper ? <small>{helper}</small> : null}
    </article>
  );
}
