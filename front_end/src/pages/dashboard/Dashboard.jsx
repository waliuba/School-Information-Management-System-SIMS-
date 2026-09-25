import EmptyState from '../../components/dashboard/EmptyState.jsx';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import AdminDashboard from './AdminDashboard.jsx';
import TeacherDashboard from './TeacherDashboard.jsx';

const dashboardCopy = {
  ADMIN: {
    title: 'Admin Dashboard',
    description: 'School performance overview and management workspace.',
  },
  TEACHER: {
    title: 'Teachers Dashboard',
    description: 'Class Performance Hub',
  },
  STUDENT: {
    title: 'Student Dashboard',
    description: 'Student dashboard data will appear when student-facing APIs are available.',
  },
};

export default function Dashboard() {
  const { user } = useAuth();
  const role = user?.role || user?.role_name || user?.roleName;
  const copy = dashboardCopy[role] || dashboardCopy.STUDENT;

  return (
    <PageContainer title={copy.title} description={copy.description}>
      {role === 'ADMIN' ? <AdminDashboard /> : null}
      {role === 'TEACHER' ? <TeacherDashboard /> : null}
      {role !== 'ADMIN' && role !== 'TEACHER' ? (
        <EmptyState
          title="Dashboard not available yet"
          message="This role is authenticated, but a dedicated dashboard has not been connected yet."
        />
      ) : null}
    </PageContainer>
  );
}
