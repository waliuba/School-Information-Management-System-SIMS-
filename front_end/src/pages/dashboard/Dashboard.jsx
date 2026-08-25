import { useCallback } from 'react';
import AnimatedSection from '../../components/common/AnimatedSection.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Loading from '../../components/common/Loading.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import RecentActivity from '../../components/dashboard/RecentActivity.jsx';
import StatCard from '../../components/dashboard/StatCard.jsx';
import { fadeIn } from '../../animations/fadeIn.js';
import { getDashboardSummary } from '../../services/api/dashboardApi.js';
import { useApi } from '../../hooks/useApi.js';

const stats = [
  { key: 'totalStudents', label: 'Total Students' },
  { key: 'totalTeachers', label: 'Total Teachers' },
  { key: 'totalClasses', label: 'Total Classes' },
  { key: 'totalSubjects', label: 'Total Subjects' },
  { key: 'totalDepartments', label: 'Total Departments' },
];

export default function Dashboard() {
  const loadDashboard = useCallback(() => getDashboardSummary(), []);
  const { data, error, isLoading } = useApi(loadDashboard);

  return (
    <PageContainer
      title="Admin Dashboard"
      description="Summary values should come from the backend dashboard endpoint."
    >
      <ErrorMessage error={error} title="Could not load dashboard summary" />

      {isLoading ? (
        <Loading message="Loading dashboard summary..." />
      ) : (
        <AnimatedSection className="stats-grid" variants={fadeIn}>
          {stats.map((stat) => (
            <StatCard
              key={stat.key}
              label={stat.label}
              value={data?.[stat.key]}
              helper={`From backend field: ${stat.key}`}
            />
          ))}
        </AnimatedSection>
      )}

      <RecentActivity />
    </PageContainer>
  );
}
