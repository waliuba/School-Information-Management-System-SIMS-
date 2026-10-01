import { useCallback } from 'react';
import AnimatedSection from '../../components/common/AnimatedSection.jsx';
import ChartCard from '../../components/dashboard/ChartCard.jsx';
import StudentPerformanceTable from '../../components/dashboard/StudentPerformanceTable.jsx';
import ErrorState from '../../components/dashboard/ErrorState.jsx';
import GradeDistributionChart from '../../components/dashboard/GradeDistributionChart.jsx';
import LoadingState from '../../components/dashboard/LoadingState.jsx';
import MarksDistributionChart from '../../components/dashboard/MarksDistributionChart.jsx';
import StatCard from '../../components/dashboard/StatCard.jsx';
import { getDashboardPerformance, getDashboardSummary } from '../../services/api/dashboardApi.js';
import { useApi } from '../../hooks/useApi.js';

const summaryCards = [
  { key: 'totalStudents', label: 'Total Students', helper: 'Enrolled student records' },
  { key: 'totalTeachers', label: 'Total Teachers', helper: 'Users with teacher role' },
  { key: 'totalClasses', label: 'Total Classes', helper: 'Active class streams' },
  { key: 'totalSubjects', label: 'Total Subjects/Courses', helper: 'Backend course records' },
  { key: 'totalDepartments', label: 'Total Departments', helper: 'Academic departments' },
];

export default function AdminDashboard() {
  const loadDashboard = useCallback(
    async () => {
      const [summary, performance] = await Promise.all([
        getDashboardSummary(),
        getDashboardPerformance(),
      ]);
      return { summary, performance };
    },
    []
  );
  const { data, error, isLoading } = useApi(loadDashboard);

  if (error) {
    return <ErrorState error={error} title="Could not load admin dashboard" />;
  }

  return (
    <div className="dashboard-page">
      {isLoading ? (
        <LoadingState message="Loading school summary..." />
      ) : (
        <AnimatedSection className="stats-grid dashboard-stats-grid">
          {summaryCards.map((stat) => (
            <StatCard
              key={stat.key}
              label={stat.label}
              value={data?.summary?.[stat.key]}
              helper={stat.helper}
            />
          ))}
        </AnimatedSection>
      )}

      <AnimatedSection className="dashboard-section">
        <div className="dashboard-section__header">
          <div>
            <h2>Class/Stream Performance</h2>
            <p>Summary of student performance across all classes/streams</p>
          </div>
        </div>
        <StudentPerformanceTable rows={data?.performance?.rows || []} />
      </AnimatedSection>

      <AnimatedSection className="dashboard-chart-grid">
        <MarksDistributionChart data={data?.performance?.marksDistribution || []} />
        <GradeDistributionChart data={data?.performance?.gradeDistribution || []} />
      </AnimatedSection>

      <AnimatedSection className="dashboard-section">
        <ChartCard
          title="Additional Insights"
          description="Trends and support signals."
          isEmpty
          emptyTitle="No insight data available yet"
          emptyMessage="no data available."
        />
      </AnimatedSection>
    </div>
  );
}
