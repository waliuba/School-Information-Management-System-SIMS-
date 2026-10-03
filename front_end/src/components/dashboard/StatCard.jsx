export default function StatCard({ label, value, helper, icon }) {
  return (
    <article className="stat-card">
      <div className="stat-card__top"><span className="stat-card__label">{label}</span>{icon ? <span className="stat-card__icon" aria-hidden="true">{icon}</span> : null}</div>
      <strong>{value ?? '-'}</strong>
      {helper ? <small>{helper}</small> : null}
    </article>
  );
}
