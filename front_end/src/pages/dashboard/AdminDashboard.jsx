import { useCallback } from 'react';
import { Link } from 'react-router-dom';
import AnimatedSection from '../../components/common/AnimatedSection.jsx';
import StudentPerformanceTable from '../../components/dashboard/StudentPerformanceTable.jsx';
import ErrorState from '../../components/dashboard/ErrorState.jsx';
import GradeDistributionChart from '../../components/dashboard/GradeDistributionChart.jsx';
import LoadingState from '../../components/dashboard/LoadingState.jsx';
import MarksDistributionChart from '../../components/dashboard/MarksDistributionChart.jsx';
import StatCard from '../../components/dashboard/StatCard.jsx';
import RecentActivity from '../../components/dashboard/RecentActivity.jsx';
import { getDashboardPerformance, getDashboardSummary } from '../../services/api/dashboardApi.js';
import { useApi } from '../../hooks/useApi.js';

const summaryCards = [
  { key: 'totalStudents', label: 'Students', helper: 'Student records', icon: 'ST' },
  { key: 'totalTeachers', label: 'Teachers', helper: 'Teacher accounts', icon: 'TE' },
  { key: 'totalClasses', label: 'Classes', helper: 'Class streams', icon: 'CL' },
  { key: 'totalSubjects', label: 'Subjects & courses', helper: 'Academic units', icon: 'SU' },
  { key: 'totalDepartments', label: 'Departments', helper: 'Academic departments', icon: 'DE' },
];
const dataLinks = [
  ['Students', 'totalStudents', '/students'], ['Teachers', 'totalTeachers', '/teachers'], ['Courses', 'totalSubjects', '/courses'], ['Departments', 'totalDepartments', '/departments'],
  ['Enrollments', null, '/enrollments'], ['Attendance', null, '/attendance'], ['Examinations & results', null, '/results'], ['Users & roles', null, '/users'],
];
const quickActions = [['Add student', '/students/new'], ['Add teacher', '/teachers/new'], ['Add course', '/courses/new'], ['Create enrollment', '/enrollments/new'],  ['Generate report', '/reports']];

export default function AdminDashboard() {
  const hour = new Date().getHours();
  const greeting = hour < 12 ? 'Good morning' : hour < 17 ? 'Good afternoon' : 'Good evening';
  const loadDashboard = useCallback(async () => {
    const [summary, performance] = await Promise.all([getDashboardSummary(), getDashboardPerformance()]);
    return { summary, performance };
  }, []);
  const { data, error, isLoading } = useApi(loadDashboard);
  if (error) return <ErrorState error={error} title="Could not load admin dashboard" />;
  const summary = data?.summary || {};

  return <div className="dashboard-page">
    <AnimatedSection className="dashboard-welcome">
      <div><p className="dashboard-eyebrow">SCHOOL OVERVIEW</p><h1>{greeting}, {summary.adminName || 'Admin'}</h1><p>Your school information and performance at a glance.</p></div>
      <label className="academic-filter"><span>Academic year</span><select aria-label="Academic year" defaultValue="2026"><option value="2026">2026</option><option value="2025">2025</option></select></label>
    </AnimatedSection>

    {isLoading ? <LoadingState message="Loading school summary..." /> : <AnimatedSection className="stats-grid dashboard-stats-grid">{summaryCards.map((stat) => <StatCard key={stat.key} label={stat.label} value={summary[stat.key]} helper={stat.helper} icon={stat.icon} />)}</AnimatedSection>}

    <div className="dashboard-overview-grid">
      <AnimatedSection className="dashboard-section dashboard-performance">
        <div className="dashboard-section__header"><div><p className="dashboard-eyebrow">ACADEMIC ANALYTICS</p><h2>Student performance</h2><p>Class and stream results from the connected performance service.</p></div><Link className="text-link" to="/results">View results <span aria-hidden="true">→</span></Link></div>
        <StudentPerformanceTable rows={data?.performance?.rows || []} />
      </AnimatedSection>
      <AnimatedSection className="dashboard-section dashboard-quick-actions">
        <div className="dashboard-section__header"><div><p className="dashboard-eyebrow">SHORTCUTS</p><h2>Quick actions</h2></div></div>
        <div className="quick-actions-grid">{quickActions.map(([label, to]) => <Link key={label} to={to}><span aria-hidden="true">＋</span>{label}<b aria-hidden="true">→</b></Link>)}</div>
      </AnimatedSection>
    </div>

    <AnimatedSection className="dashboard-chart-grid"><MarksDistributionChart data={data?.performance?.marksDistribution || []} /><GradeDistributionChart data={data?.performance?.gradeDistribution || []} /></AnimatedSection>

    <div className="dashboard-overview-grid dashboard-lower-grid">
      <AnimatedSection className="dashboard-section"><div className="dashboard-section__header"><div><p className="dashboard-eyebrow">RECORDS</p><h2>Data management</h2><p>Open a module to search and manage school records.</p></div></div><div className="data-overview-list">{dataLinks.map(([label, key, to]) => <Link to={to} key={label}><span>{label}</span><strong>{key && summary[key] != null ? `${Number(summary[key]).toLocaleString()} records` : 'Open module'} <b aria-hidden="true">→</b></strong></Link>)}</div></AnimatedSection>
      <div className="dashboard-side-stack">
        <AnimatedSection className="dashboard-section"><div className="dashboard-section__header"><div><p className="dashboard-eyebrow">SYSTEM</p><h2>Connection status</h2></div></div><p className="status-note">Health endpoints are not connected yet.</p>{['Backend API', 'Database', 'Authentication', 'Frontend'].map((label) => <div className="health-row" key={label}><span className="health-dot" />{label}<strong>Not connected</strong></div>)}</AnimatedSection>
        <RecentActivity />
      </div>
    </div>
  </div>;
}
