import { useMemo } from 'react';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Table from '../../components/common/Table.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useApi } from '../../hooks/useApi.js';

function formatValue(value) {
  if (value === null || value === undefined || value === '') {
    return '—';
  }
  if (typeof value === 'object') {
    return value.name || value.title || value.username || value.id || JSON.stringify(value);
  }
  return String(value);
}

function getColumns(rows) {
  const keys = [...new Set(rows.flatMap((row) => Object.keys(row || {})))].slice(0, 8);
  return keys.map((key) => ({
    key,
    label: key.replace(/([A-Z])/g, ' $1').replace(/^./, (letter) => letter.toUpperCase()),
    render: (row) => formatValue(row[key]),
  }));
}

export default function ResourcePage({ title, description, request }) {
  const { data, error, isLoading } = useApi(request);
  const rows = Array.isArray(data) ? data : [];
  const columns = useMemo(() => getColumns(rows), [rows]);

  return (
    <PageContainer title={title} description={description}>
      <ErrorMessage error={error} />
      {!error ? (
        <Table
          columns={columns}
          rows={rows}
          isLoading={isLoading}
          emptyMessage={`No ${title.toLowerCase()} records were returned by the backend.`}
        />
      ) : null}
    </PageContainer>
  );
}
