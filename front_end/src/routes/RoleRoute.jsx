import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth.js';

export default function RoleRoute({ allowedRoles = [] }) {
  const { user } = useAuth();
  const roleName = user?.role || user?.role_name || user?.roleName;

  if (!allowedRoles.length || allowedRoles.includes(roleName)) {
    return <Outlet />;
  }

  return <Navigate to="/unauthorized" replace />;
}
