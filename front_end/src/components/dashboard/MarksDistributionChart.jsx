import ChartCard from './ChartCard.jsx';

export default function MarksDistributionChart({ data = [] }) {
  return (
    <DonutChart
      title="Marks Distribution"
      description="Distribution by mark ranges from examination results."
      data={data}
      emptyMessage="Marks distribution will appear once examination results are available."
    />
  );
}

function DonutChart({ title, description, data, emptyMessage }) {
  const total = data.reduce((sum, item) => sum + Number(item.value || 0), 0);
  const segments = buildSegments(data, total);

  return (
    <ChartCard
      title={title}
      description={description}
      legend={data.map((item) => ({
        ...item,
        value: `${item.value} (${total ? Math.round((item.value / total) * 100) : 0}%)`,
      }))}
      isEmpty={!total}
      emptyMessage={emptyMessage}
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
