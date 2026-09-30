import ChartCard from './ChartCard.jsx';

export default function GradeDistributionChart({ data = [] }) {
  const total = data.reduce((sum, item) => sum + Number(item.value || 0), 0);
  const segments = buildSegments(data, total);

  return (
    <ChartCard
      title="Grade Distribution"
      description="Distribution by the application's grading scale."
      legend={data.map((item) => ({
        ...item,
        value: `${item.value} (${total ? Math.round((item.value / total) * 100) : 0}%)`,
      }))}
      isEmpty={!total}
      emptyMessage="Grade distribution will appear once grading data is available."
    >
      <div className="donut-chart" style={{ background: `conic-gradient(${segments})` }}>
        <div className="donut-chart__center">
          <strong>{total}</strong>
          <span>records</span>
        </div>
      </div>
    </ChartCard>
  );
}

function buildSegments(data, total) {
  if (!total) {
    return '#e5e7eb 0deg 360deg';
  }

  let start = 0;
  return data
    .map((item) => {
      const degrees = (Number(item.value || 0) / total) * 360;
      const end = start + degrees;
      const segment = `${item.color} ${start}deg ${end}deg`;
      start = end;
      return segment;
    })
    .join(', ');
}
