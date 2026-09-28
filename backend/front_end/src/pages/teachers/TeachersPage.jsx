import { useCallback } from 'react';
import Button from '../../components/common/Button.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Table from '../../components/common/Table.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useApi } from '../../hooks/useApi.js';
import { getTeachers } from '../../services/api/userApi.js';

const columns = [
  { key: 'teacherNo', label: 'Teacher No.' },
  {
    key: 'name',
    label: 'Name',
    render: (teacher) => `${teacher.firstName} ${teacher.lastName}`,
  },
  { key: 'email', label: 'Email' },
  { key: 'phone', label: 'Phone' },
];

export default function TeachersPage() {
  const loadTeachers = useCallback(() => getTeachers(), []);
  const { data, error, isLoading, execute } = useApi(loadTeachers, {
    refetchOnWindowFocus: true,
  });

  return (
    <PageContainer
      title="Teachers"
      description="All teachers seeded and maintained in the teachers register."
      actions={
        <Button type="button" onClick={() => execute()} disabled={isLoading}>
          {isLoading ? 'Refreshing...' : 'Refresh'}
        </Button>
      }
    >
      <ErrorMessage error={error} title="Could not load teachers" />
      <Table
        columns={columns}
        rows={data}
        isLoading={isLoading}
        emptyMessage="No teacher accounts found."
      />
    </PageContainer>
  );
}
