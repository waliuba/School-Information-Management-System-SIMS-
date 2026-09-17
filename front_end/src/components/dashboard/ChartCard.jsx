import EmptyState from './EmptyState.jsx';

export default function ChartCard({
  title,
  description,
  children,
  legend = [],
  isEmpty,
  emptyTitle = 'No chart data available',
  emptyMessage = 'Performance data will appear once examination results are available.',
}) {
  return (
    <section className="chart-card">
      <div className="chart-card__header">
        <h2>{title}</h2>
        {description ? <p>{description}</p> : null}
      </div>

      {isEmpty ? (
        <EmptyState title={emptyTitle} message={emptyMessage} />
      ) : (
        <>
          <div className="chart-card__body">{children}</div>
          {legend.length ? (
            <ul className="chart-legend" aria-label={`${title} legend`}>
              {legend.map((item) => (
                <li key={item.label}>
                  <span className="chart-legend__swatch" style={{ background: item.color }} />
                  <span>{item.label}</span>
                  <strong>{item.value}</strong>
                </li>
              ))}
            </ul>
          ) : null}
        </>
      )}
    </section>
  );
}
