import { Link } from 'react-router-dom';

const statusTones = [
  { pattern: /active|complete|success|pass/i, tone: 'success' },
  { pattern: /pending|wait|upcoming|late/i, tone: 'warning' },
  { pattern: /inactive|fail|absent|error/i, tone: 'danger' },
];

const icons = {
  users: <><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" /><circle cx="10" cy="7" r="4" /><path d="M20 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" /></>,
  'graduation-cap': <><path d="m2 10 10-5 10 5-10 5-10-5Z" /><path d="M6 12v5c3.5 3 8.5 3 12 0v-5M22 10v6" /></>,
  'book-open': <><path d="M12 7v14" /><path d="M3 18V5a1 1 0 0 1 1-1h4a4 4 0 0 1 4 4 4 4 0 0 1 4-4h4a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1h-5a3 3 0 0 0-3 2 3 3 0 0 0-3-2H4a1 1 0 0 1-1-1Z" /></>,
  building: <><rect x="4" y="3" width="16" height="18" rx="1" /><path d="M9 21v-4h6v4M8 7h1m6 0h1M8 11h1m6 0h1" /></>,
  'clipboard-list': <><rect x="5" y="4" width="14" height="17" rx="2" /><path d="M9 4.5h6a1.5 1.5 0 0 0-3-1 1.5 1.5 0 0 0-3 1ZM9 10h6m-6 4h6m-6 4h3" /></>,
  'calendar-check': <><rect x="3" y="5" width="18" height="16" rx="2" /><path d="M16 3v4M8 3v4M3 10h18m-13 5 2 2 4-4" /></>,
  'file-text': <><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8Z" /><path d="M14 2v6h6M8 13h8m-8 4h8" /></>,
  chart: <><path d="M3 3v18h18M8 16v-4m5 4V7m5 9v-7" /></>,
};

function getStatusTone(status) {
  return statusTones.find(({ pattern }) => pattern.test(status))?.tone || 'neutral';
}

function formatValue(value) {
  if (typeof value === 'number' && Number.isFinite(value)) {
    return new Intl.NumberFormat().format(value);
  }
  return value;
}

export default function ModuleDataCard({
  title,
  icon,
  description,
  value,
  valueLabel,
  secondaryStats = [],
  action,
  state = 'loaded',
  errorMessage,
  onRetry,
}) {
  const visibleStats = [...secondaryStats]
    .sort((first, second) => second.value - first.value)
    .slice(0, 4);

  return (
    <article className={`module-data-card${state === 'loading' ? ' module-data-card--loading' : ''}`} aria-busy={state === 'loading'}>
      <header className="module-data-card__header">
        <span className="module-data-card__icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round">
            {icons[icon]}
          </svg>
        </span>
        <h2>{title}</h2>
      </header>

      {state === 'loading' ? (
        <div className="module-data-card__skeleton" role="status" aria-label={`Loading ${title} data`}>
          <span />
          <span />
          <span />
        </div>
      ) : state === 'error' ? (
        <div className="module-data-card__message" role="alert">
          <p>Couldn't load {title.toLowerCase()} data.</p>
          {errorMessage ? <span>{errorMessage}</span> : null}
          <button type="button" aria-label={`Retry loading ${title.toLowerCase()} data`} onClick={onRetry}>Retry</button>
        </div>
      ) : value === null || value === undefined ? (
        <div className="module-data-card__metric module-data-card__metric--empty">
          <strong aria-label="Data unavailable">—</strong>
          <span>Data unavailable</span>
        </div>
      ) : (
        <>
          <div className="module-data-card__metric">
            <strong>{valueLabel ?? formatValue(value)}</strong>
            <span>{description}</span>
          </div>
          {visibleStats.length > 0 ? (
            <ul
              className={`module-data-card__stats${visibleStats.length > 3 ? ' module-data-card__stats--wrapped' : ''}`}
              style={{ '--stat-count': visibleStats.length }}
              aria-label={`${title} breakdown`}
            >
              {visibleStats.map((stat) => (
                <li key={stat.label} className={`module-data-card__stat module-data-card__stat--${getStatusTone(stat.label)}`}>
                  <span className="module-data-card__stat-marker" aria-hidden="true" />
                  <span>{stat.label}</span>
                  <strong>{formatValue(stat.value)}{stat.suffix || ''}</strong>
                </li>
              ))}
            </ul>
          ) : null}
        </>
      )}

      <Link className="module-data-card__action" to={action.href}>
        {action.label}<span aria-hidden="true">→</span>
      </Link>
    </article>
  );
}
