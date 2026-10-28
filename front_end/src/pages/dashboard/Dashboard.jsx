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
  SUPER_ADMIN: {
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

function isPrivilegedRole(role) {
  return ['ADMIN', 'SUPER_ADMIN'].includes(String(role || '').toUpperCase());
}

export default function Dashboard() {
  const { user } = useAuth();
  const role = user?.role || user?.role_name || user?.roleName;
  const copy = dashboardCopy[role] || dashboardCopy.STUDENT;
  const isAdminView = isPrivilegedRole(role);

  return (
    <>
      {isAdminView ? <section className="page-container admin-page-container"><AdminDashboard /></section> : null}
      {!isAdminView ? <PageContainer title={copy.title} description={copy.description}>
      {role === 'TEACHER' ? <TeacherDashboard /> : null}
      {role !== 'TEACHER' ? (
        <EmptyState
          title="Dashboard not available yet"
          message="This role is authenticated, but a dedicated dashboard has not been connected yet."
        />
      ) : null}
      </PageContainer> : null}
    </>
  );
}
