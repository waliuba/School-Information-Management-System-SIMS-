import { useCallback, useMemo, useState } from 'react';
import AnimatedSection from '../../components/common/AnimatedSection.jsx';
import EmptyState from '../../components/dashboard/EmptyState.jsx';
import ErrorState from '../../components/dashboard/ErrorState.jsx';
import FilterBar from '../../components/dashboard/FilterBar.jsx';
import GradeDistributionChart from '../../components/dashboard/GradeDistributionChart.jsx';
import LoadingState from '../../components/dashboard/LoadingState.jsx';
import MarksDistributionChart from '../../components/dashboard/MarksDistributionChart.jsx';
import StatCard from '../../components/dashboard/StatCard.jsx';
import StudentPerformanceTable from '../../components/dashboard/StudentPerformanceTable.jsx';
import { useApi } from '../../hooks/useApi.js';
import { getClasses, getCourses } from '../../services/api/resourcesApi.js';
import { getDashboardPerformance } from '../../services/api/dashboardApi.js';
import { applyPerformanceFilters, rankStudents } from './dashboardMetrics.js';

const initialFilters = {
  search: '',
  academicYear: '',
  classId: '',
  subjectId: '',
  grade: '',
  performance: 'all',
  sort: 'highest',
  topLimit: 'all',
};

const summaryCards = [
  { label: 'Students', value: null, helper: 'Teacher-scoped roster pending' },
  { label: 'Subjects/Classes Taught', value: null, helper: 'Assignments pending' },
  { label: 'Average Score', value: null, helper: 'Requires results data' },
  { label: 'Highest Score', value: null, helper: 'Requires results data' },
  { label: 'Lowest Score', value: null, helper: 'Requires results data' },
  { label: 'Pass Rate', value: null, helper: 'Requires grading rules' },
];

export default function TeacherDashboard() {
  const [filters, setFilters] = useState(initialFilters);
  const loadFilters = useCallback(
    async () => {
      const [classes, subjects, performance] = await Promise.all([
        getClasses(),
        getCourses(),
        getDashboardPerformance(),
      ]);
      return { classes, subjects, performance };
    },
    []
  );
  const { data, error, isLoading } = useApi(loadFilters);

  const filteredRows = useMemo(() => {
    const rows = applyPerformanceFilters(data?.performance?.rows || [], filters);
    const rankedRows = rankStudents(rows, filters.sort === 'lowest' ? 'asc' : 'desc');
    const limit = Number(filters.topLimit);

    if (filters.performance === 'top' && Number.isFinite(limit)) {
      return rankedRows.slice(0, limit);
    }

    return rankedRows;
  }, [data, filters]);

  if (error) {
    return <ErrorState error={error} title="Could not load teacher dashboard filters" />;
  }

  return (
    <div className="dashboard-page">
      <AnimatedSection className="stats-grid dashboard-stats-grid dashboard-stats-grid--teacher">
        {summaryCards.map((stat) => (
          <StatCard key={stat.label} label={stat.label} value={stat.value} helper={stat.helper} />
        ))}
      </AnimatedSection>

      {isLoading ? (
        <LoadingState message="Loading classes and subjects..." />
      ) : (
        <FilterBar
          filters={filters}
          onChange={setFilters}
          onClear={() => setFilters(initialFilters)}
          classes={data?.classes || []}
          subjects={data?.subjects || []}
          showTopLimit={filters.performance === 'top'}
        />
      )}

      <AnimatedSection className="dashboard-section">
        <div className="dashboard-section__header">
          <div>
            <h2>Student Performance</h2>
            <p>Student-level results will remain empty until real examination data is available.</p>
          </div>
        </div>
        <StudentPerformanceTable rows={filteredRows} />
      </AnimatedSection>

      <AnimatedSection className="dashboard-chart-grid">
        <MarksDistributionChart data={data?.performance?.marksDistribution || []} />
        <GradeDistributionChart data={data?.performance?.gradeDistribution || []} />
      </AnimatedSection>

      <AnimatedSection className="dashboard-insights-grid">
        <EmptyState
          title="Top Performing Students"
          message="Top performers will be ranked from actual calculated averages once results are available."
        />
        <EmptyState
          title="Students Needing Attention"
          message="Support indicators will use respectful labels and real performance data once results are available."
        />
      </AnimatedSection>
    </div>
  );
}
